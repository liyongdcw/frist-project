package com.huawei.mail.po;

import lombok.Getter;
import lombok.Setter;

/**
 * 邮件接受对象
 * @author liyong
 *
 */
@Getter
@Setter
public class UserPo {
	private String name;
	private String mail;
	@Override
	public String toString() {
		return "ReceiverPo [name=" + name + ", mail=" + mail + "]";
	}
	
}
