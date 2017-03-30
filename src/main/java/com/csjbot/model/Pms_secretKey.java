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
 * @version 创建时间：2017年3月21日 下午12:01:45
 * 类说明
 */
@Table(name = "pms_secret_key")
public class Pms_secretKey {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String key_;
	private String secret;
	private String user_id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getKey() {
		return key_;
	}
	public void setKey(String key) {
		this.key_ = key;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Pms_secretKey() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Pms_secretKey(String key, String secret, String user_id) {
		super();
		this.key_ = key;
		this.secret = secret;
		this.user_id = user_id;
	}
	@Override
	public String toString() {
		return "Pms_secretKey [id=" + id + ", key=" + key_ + ", secret=" + secret + ", user_id=" + user_id + "]";
	}
	
	
}
