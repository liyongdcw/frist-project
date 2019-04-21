package com.huawei.mail.control;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("simple")
public class SimpleMailControl {
	
	@Resource(name="myMaileSender")
	JavaMailSender mailSender;
	@RequestMapping("send")
	public void sendSimpleEmail() throws MessagingException {
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
}
