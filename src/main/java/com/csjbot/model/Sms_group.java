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
 * @version 创建时间：2017年3月27日 下午4:13:07
 * 类说明
 */
@Table(name = "sms_group")
public class Sms_group {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String master_account;
	private Integer user_numbers;
	private Timestamp creat_time;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMaster_account() {
		return master_account;
	}
	public void setMaster_account(String master_account) {
		this.master_account = master_account;
	}
	public Integer getUser_numbers() {
		return user_numbers;
	}
	public void setUser_numbers(Integer user_numbers) {
		this.user_numbers = user_numbers;
	}
	public Timestamp getCreat_time() {
		return creat_time;
	}
	public void setCreat_time(Timestamp creat_time) {
		this.creat_time = creat_time;
	}
	public Sms_group() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Sms_group(String name, String master_account, Integer user_numbers, Timestamp creat_time) {
		super();
		this.name = name;
		this.master_account = master_account;
		this.user_numbers = user_numbers;
		this.creat_time = creat_time;
	}
	@Override
	public String toString() {
		return "Sms_group [id=" + id + ", name=" + name + ", master_account=" + master_account + ", user_numbers="
				+ user_numbers + ", creat_time=" + creat_time + "]";
	}
	
}
