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
 * @version 创建时间：2017年3月27日 下午4:16:53
 * 类说明
 */
@Table(name = "sms_ug_relations")
public class Sms_ug_relations {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String account;
	private Integer group_id;
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
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	public Sms_ug_relations() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Sms_ug_relations(String account, Integer group_id) {
		super();
		this.account = account;
		this.group_id = group_id;
	}
	@Override
	public String toString() {
		return "Sms_ug_relations [id=" + id + ", account=" + account + ", group_id=" + group_id + "]";
	}
	
}
