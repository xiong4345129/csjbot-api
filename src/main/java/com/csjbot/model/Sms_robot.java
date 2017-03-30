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
 * @version 创建时间：2017年3月27日 下午4:14:34
 * 类说明
 */
@Table(name = "sms_robot")
public class Sms_robot {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String uid;
	private String name;
	private String master_account;
	private Integer activate;
	private Timestamp activate_time;
	private Timestamp out_time;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
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
	public Integer getActivate() {
		return activate;
	}
	public void setActivate(Integer activate) {
		this.activate = activate;
	}
	public Timestamp getActivate_time() {
		return activate_time;
	}
	public void setActivate_time(Timestamp activate_time) {
		this.activate_time = activate_time;
	}
	public Timestamp getOut_time() {
		return out_time;
	}
	public void setOut_time(Timestamp out_time) {
		this.out_time = out_time;
	}
	public Sms_robot() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Sms_robot(String uid, String name, String master_account, Integer activate, Timestamp activate_time,
			Timestamp out_time) {
		super();
		this.uid = uid;
		this.name = name;
		this.master_account = master_account;
		this.activate = activate;
		this.activate_time = activate_time;
		this.out_time = out_time;
	}
	@Override
	public String toString() {
		return "Sms_robot [id=" + id + ", uid=" + uid + ", name=" + name + ", master_account=" + master_account
				+ ", activate=" + activate + ", activate_time=" + activate_time + ", out_time=" + out_time + "]";
	}
	
}
