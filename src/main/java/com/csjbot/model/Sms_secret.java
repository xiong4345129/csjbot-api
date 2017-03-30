/**
 * 
 */
package com.csjbot.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 下午4:16:08
 * 类说明
 */
@Table(name = "sms_secret")
public class Sms_secret {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String account;
	private String secret;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public Sms_secret() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Sms_secret(String account, String secret) {
		super();
		this.account = account;
		this.secret = secret;
	}
	@Override
	public String toString() {
		return "Sms_secret [id=" + id + ", account=" + account + ", secret=" + secret + "]";
	}
	
}
