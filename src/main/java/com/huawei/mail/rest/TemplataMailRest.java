package com.huawei.mail.rest;

import javax.mail.MessagingException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huawei.mail.utils.MailUtils;

/**
 * 模板邮件接口
 * @author liyong
 *
 */
@RestController
@RequestMapping("template")
public class TemplataMailRest {
	@RequestMapping(value = "jsonData",method = RequestMethod.GET)
	public void sendSimpleMail(@PathVariable("jsonData") String jsonData) {
		try {
			MailUtils.sendTemplateMail(jsonData);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
