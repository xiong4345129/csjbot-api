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
 * @version 创建时间：2017年3月27日 上午9:49:44
 * 类说明
 */
@Table(name = "frs_group")
public class Frs_group {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String group_id;
	private String group_name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public Frs_group() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Frs_group(String group_id, String group_name) {
		super();
		this.group_id = group_id;
		this.group_name = group_name;
	}
	@Override
	public String toString() {
		return "Frs_group [id=" + id + ", group_id=" + group_id + ", group_name=" + group_name + "]";
	}
	
}
