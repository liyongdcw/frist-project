package com.huawei.mail.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.huawei.mail.po.ReceiverPo;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class RichMailTest {
	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	@Before
	public void setMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void test1() {
		ReceiverPo po1 = new ReceiverPo();
		po1.setMail("865503413@qq.com");
		ReceiverPo po2 = new ReceiverPo();
		po2.setMail("1139039950@qq.com");
		ArrayList<ReceiverPo> list = new ArrayList<ReceiverPo>();
		list.add(po1);
		list.add(po2);
		
		JSONObject json = new JSONObject();
		
		try {
			json.put("receiverPos", list);
			json.put("carboncopyPos", list);
			json.put("subject", "liyong的模板邮件测试");
			json.put("content", "这是一封测试邮件！");
			
			String str = JSONObject.toJSONString(list);
			
//			mockMvc.perform(MockMvcRequestBuilders.patch("/richMail/template").param("receiverPos", str).param("carboncopyPos", str).
//					param("subject", "liyong的模板邮件测试").param("content", "这是一封测试邮件！"));
			mockMvc.perform(MockMvcRequestBuilders.post("/richMail/template").content(json.toString()).contentType("application/json;charset=UTF-8")).andReturn();
			//mockMvc.perform(MockMvcRequestBuilders.post("/richMail/template", list,list,"liyong的模板邮件测试","这是一封测试邮件！"));
			System.out.println("测试成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		ReceiverPo po1 = new ReceiverPo();
		po1.setMail("865503413@qq.com");
		ReceiverPo po2 = new ReceiverPo();
		po2.setMail("1139039950@qq.com");
		ArrayList<ReceiverPo> list = new ArrayList<ReceiverPo>();
		list.add(po1);
		list.add(po2);
		
		JSONObject json = new JSONObject();
		
		try {
			json.put("receiverPos", list);
			json.put("carboncopyPos", list);
			json.put("subject", "liyong的附件邮件测试");
			json.put("content", "这是一封测试邮件！");
			
			String str = JSONObject.toJSONString(list);
			
//			mockMvc.perform(MockMvcRequestBuilders.patch("/richMail/template").param("receiverPos", str).param("carboncopyPos", str).
//					param("subject", "liyong的模板邮件测试").param("content", "这是一封测试邮件！"));
			mockMvc.perform(MockMvcRequestBuilders.post("/richMail/attachment").content(json.toString()).contentType("application/json;charset=UTF-8")).andReturn();
			//mockMvc.perform(MockMvcRequestBuilders.post("/richMail/template", list,list,"liyong的模板邮件测试","这是一封测试邮件！"));
			System.out.println("测试成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
