/**
 * 
 */
package com.csjbot.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 下午4:12:00
 * 类说明
 */
@Table(name = "sms_auth_code")
public class Sms_auth_code {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String account;
	private String auth_code;
	private Timestamp time;
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
	public String getAuth_code() {
		return auth_code;
	}
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public Sms_auth_code() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Sms_auth_code(String account, String auth_code, Timestamp time) {
		super();
		this.account = account;
		this.auth_code = auth_code;
		this.time = time;
	}
	@Override
	public String toString() {
		return "Sms_auth_code [id=" + id + ", account=" + account + ", auth_code=" + auth_code + ", time=" + time + "]";
	}
	
}
