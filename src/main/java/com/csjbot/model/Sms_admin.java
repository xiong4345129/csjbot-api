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
 * @version 创建时间：2017年3月30日 下午1:49:41
 * 类说明
 */
@Table(name = "sms_admin")
public class Sms_admin {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String account;
	private String password;
	private String name;
	private Integer role;
	private String head_name;
	private String head_url;
	private Integer status;
	private Timestamp creat_time;
	private String uid;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public String getHead_name() {
		return head_name;
	}
	public void setHead_name(String head_name) {
		this.head_name = head_name;
	}
	public String getHead_url() {
		return head_url;
	}
	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Timestamp getCreat_time() {
		return creat_time;
	}
	public void setCreat_time(Timestamp creat_time) {
		this.creat_time = creat_time;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Sms_admin() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Sms_admin(String account, String password, String name, Integer role, String head_name, String head_url,
			Integer status, Timestamp creat_time, String uid) {
		super();
		this.account = account;
		this.password = password;
		this.name = name;
		this.role = role;
		this.head_name = head_name;
		this.head_url = head_url;
		this.status = status;
		this.creat_time = creat_time;
		this.uid = uid;
	}
	@Override
	public String toString() {
		return "Sms_admin [id=" + id + ", account=" + account + ", password=" + password + ", name=" + name + ", role="
				+ role + ", head_name=" + head_name + ", head_url=" + head_url + ", status=" + status + ", creat_time="
				+ creat_time + ", uid=" + uid + "]";
	}
	
}
