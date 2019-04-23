package com.huawei.mail.control;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.mail.po.UserPo;

/**
 * 富文本邮件
 * @author liyong
 *
 */
@RestController
@RequestMapping("/richMail")
public class RichMailControl {
	@Autowired
	JavaMailSender mailSender;
	@Value("${spring.mail.username}")
	String sender;
	@Autowired
	TemplateEngine templateEngine;

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
	//	public String sendTemplateMail(@RequestBody ArrayList<UserPo> UserPos,@RequestBody ArrayList<UserPo> carboncopyPos,
	//			String subject,String content) throws MessagingException {	
	public void sendTemplateMail(@RequestBody String jsonStr) throws MessagingException {
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		List<UserPo> UserPos = JSON.parseArray(JSON.parseObject(jsonStr).getString("UserPos"), UserPo.class);
		List<UserPo> carboncopyPos = JSON.parseArray(JSON.parseObject(jsonStr).getString("carboncopyPos"), UserPo.class);
		String subject = jsonObject.getString("subject");
		String content = jsonObject.getString("content");
		System.out.println("#########"+subject+"&&&"+content);
		//数据有效性验证
		String[] receiver = getValidEmailAddress((ArrayList<UserPo>)UserPos); // 收件人
		String[] ccs = getValidEmailAddress((ArrayList<UserPo>)carboncopyPos); // 抄送人
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
				mailSender.send(message);
			}
			System.out.println("发送成功！");
		} catch (MessagingException e) {
			System.out.println("发送失败！");
			e.printStackTrace();
		}

	}

	@RequestMapping(value="/attachment")	
	public void sendAttachmentMail(@RequestBody String jsonStr) throws MessagingException {
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		List<UserPo> UserPos = JSON.parseArray(JSON.parseObject(jsonStr).getString("UserPos"), UserPo.class);
		List<UserPo> carboncopyPos = JSON.parseArray(JSON.parseObject(jsonStr).getString("carboncopyPos"), UserPo.class);
		String subject = jsonObject.getString("subject");
		String content = jsonObject.getString("content");
		System.out.println("#########"+subject+"&&&"+content);
		//数据有效性验证
		String[] receiver = getValidEmailAddress((ArrayList<UserPo>)UserPos); // 收件人
		String[] ccs = getValidEmailAddress((ArrayList<UserPo>)carboncopyPos); // 抄送人
		// 构造Email消息
		MimeMessage message = mailSender.createMimeMessage();
		message.setHeader("Disposition-Notification-To",sender); // 设置回执
		MimeMessageHelper helper = new MimeMessageHelper(message,true); // true代表消息是multiparty类型
		try {
			helper.setFrom(sender); // 发件人
			helper.setSubject(subject);
			//helper.setCc(ccs);
			//注意：Context 类是在org.thymeleaf.context.Context包下的。

			// 设置普通内容和模板内容
			//			    helper.setText(emailContent,true);
			helper.addAttachment("boot.png", new ClassPathResource("/images/1.jpg"));
			mailSender.send(message);

			System.out.println("发送成功！");
		} catch (MessagingException e) {
			System.out.println("发送失败！");
			e.printStackTrace();
		}

	}
	/**
	 *   筛选合法邮箱地址
	 * @param UserPos
	 * @return
	 */
	private static String[] getValidEmailAddress(ArrayList<UserPo> UserPos) {
		String[] receivers = null;
		for (int i = 0; i < UserPos.size(); i++) {
			// 若邮箱不合法则删除
			if (!UserPos.get(i).getMail().matches("[0-9A-Za-z]+@qq.com")) {
				UserPos.remove(i);
				i--;
			}
		}
		// 返回有效邮箱集合
		receivers = new String[UserPos.size()];
		for (int i = 0; i < UserPos.size(); i++) {
			receivers[i] = UserPos.get(i).getMail();
		}
		return receivers;
	}
}
