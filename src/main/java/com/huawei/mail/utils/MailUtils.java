package com.huawei.mail.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.mail.po.ReceiverPo;

/**
 * 邮箱工具类
 */
public class MailUtils {
	@Resource(name="myMaileSender")
	static JavaMailSender mailSender;
	@Value("${spring.mail.username}")
	static String sender;
	@Autowired
	static TemplateEngine templateEngine;

	@RequestMapping("send")
	public static void sendSimpleEmail() throws MessagingException {
		String receivers = "865503413@qq.com";
	    // 构造Email消息
	    MimeMessage message = mailSender.createMimeMessage();
	    message.setHeader("Disposition-Notification-To",receivers); // 设置回执
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	    try {
			helper.setFrom("1244935783@qq.com");
			helper.setTo(receivers);
		    helper.setSubject("SimpleTest");
		    helper.setText("邮件内容:this is a simpleMail test!并抄送给程");
		    helper.setCc("1139039950@qq.com");
		  
		    System.out.println("发送成功！");
		} catch (MessagingException e) {
			System.out.println("发送失败！");
			e.printStackTrace();
		}
	    
	    
	    mailSender.send(message);
	}
	/**
	 * 发送模板邮件
	 * @param receivers
	 * @param carboncopys
	 * @param subject
	 * @param content
	 * @return result
	 * @throws MessagingException 
	 */
	@RequestMapping(value="/template")
//	public String sendTemplateMail(@RequestBody ArrayList<ReceiverPo> receiverPos,@RequestBody ArrayList<ReceiverPo> carboncopyPos,
//			String subject,String content) throws MessagingException {	
	public static void sendTemplateMail(String jsonStr) throws MessagingException {
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		List<ReceiverPo> receiverPos = JSON.parseArray(JSON.parseObject(jsonStr).getString("receiverPos"), ReceiverPo.class);
		List<ReceiverPo> carboncopyPos = JSON.parseArray(JSON.parseObject(jsonStr).getString("carboncopyPos"), ReceiverPo.class);
		String subject = jsonObject.getString("subject");
		String content = jsonObject.getString("content");
		//数据有效性验证
		String[] receiver = getValidEmailAddress((ArrayList<ReceiverPo>)receiverPos); // 收件人
		String[] ccs = getValidEmailAddress((ArrayList<ReceiverPo>)carboncopyPos); // 抄送人
		// 构造Email消息
		MimeMessage message = mailSender.createMimeMessage();
		message.setHeader("Disposition-Notification-To",sender); // 设置回执
		MimeMessageHelper helper = new MimeMessageHelper(message,true); // true代表消息是multiparty类型
		try {
			helper.setFrom(sender); // 发件人
			helper.setSubject(subject);
			helper.setCc(ccs);
			//注意：Context 类是在org.thymeleaf.context.Context包下的。
		    Context context = new Context();
			for (int i = 0; i < receiver.length; i++) {
				helper.setTo(receiver[i]);
			    //html中填充动态属性值
			    context.setVariable("username", receiver[i]);
			    context.setVariable("url", "https://www.baidu.com");
			    //注意：process第一个参数名称要和templates下的模板名称一致。要不然会报错
			    String emailContent = templateEngine.process("test", context);

			    // 设置普通内容和模板内容
			    helper.setText(emailContent,true);
			    mailSender.send(message);
			}
			System.out.println("发送成功！");
		} catch (MessagingException e) {
			System.out.println("发送失败！");
			e.printStackTrace();
		}

	}
	
	public static void sendAttachmentMail(String jsonStr) throws MessagingException {
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		List<ReceiverPo> receiverPos = JSON.parseArray(JSON.parseObject(jsonStr).getString("receiverPos"), ReceiverPo.class);
		List<ReceiverPo> carboncopyPos = JSON.parseArray(JSON.parseObject(jsonStr).getString("carboncopyPos"), ReceiverPo.class);
		String subject = jsonObject.getString("subject");
		String content = jsonObject.getString("content");
		System.out.println("#########"+subject+"&&&"+content);
		//数据有效性验证
		String[] receiver = getValidEmailAddress((ArrayList<ReceiverPo>)receiverPos); // 收件人
		String[] ccs = getValidEmailAddress((ArrayList<ReceiverPo>)carboncopyPos); // 抄送人
		// 构造Email消息
		MimeMessage message = mailSender.createMimeMessage();
		message.setHeader("Disposition-Notification-To",sender); // 设置回执
		MimeMessageHelper helper = new MimeMessageHelper(message,true); // true代表消息是multiparty类型
		try {
			helper.setFrom(sender); // 发件人
			helper.setSubject(subject);
			//helper.setCc(ccs);
			//注意：Context 类是在org.thymeleaf.context.Context包下的。
		    Context context = new Context();
			for (int i = 0; i < receiver.length; i++) {
				helper.setTo(receiver[i]);
			    //html中填充动态属性值
			    context.setVariable("username", receiver[i]);
			    context.setVariable("url", "https://www.baidu.com");
			    //注意：process第一个参数名称要和templates下的模板名称一致。要不然会报错
			    String emailContent = templateEngine.process("test", context);

			    // 设置普通内容和模板内容
			    helper.setText(emailContent,true);
			    helper.addAttachment("boot.png", new ClassPathResource("/images/1.jpg"));
			    mailSender.send(message);
			}
			System.out.println("发送成功！");
		} catch (MessagingException e) {
			System.out.println("发送失败！");
			e.printStackTrace();
		}
	}
	/**
	   *   筛选合法邮箱地址
	 * @param receiverPos
	 * @return
	 */
	private static String[] getValidEmailAddress(ArrayList<ReceiverPo> receiverPos) {
		String[] receivers = null;
		for (int i = 0; i < receiverPos.size(); i++) {
			// 若邮箱不合法则删除
			if (!receiverPos.get(i).getMail().matches("[0-9A-Za-z]+@qq.com")) {
				receiverPos.remove(i);
				i--;
			}
		}
		// 返回有效邮箱集合
		receivers = new String[receiverPos.size()];
		for (int i = 0; i < receiverPos.size(); i++) {
			receivers[i] = receiverPos.get(i).getMail();
		}
		return receivers;
	}
}
