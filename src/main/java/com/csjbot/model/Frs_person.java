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
 * @version 创建时间：2017年3月27日 上午9:49:23
 * 类说明
 */
@Table(name = "frs_person")
public class Frs_person {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String person_id;
	private String group_id;
	private String name;
	private String age;
	private String sex;
	private String beauty;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPerson_id() {
		return person_id;
	}
	public void setPerson_id(String person_id) {
		this.person_id = person_id;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBeauty() {
		return beauty;
	}
	public void setBeauty(String beauty) {
		this.beauty = beauty;
	}
	public Frs_person() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Frs_person(String person_id, String group_id, String name, String age, String sex, String beauty) {
		super();
		this.person_id = person_id;
		this.group_id = group_id;
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.beauty = beauty;
	}
	@Override
	public String toString() {
		return "Frs_person [id=" + id + ", person_id=" + person_id + ", group_id=" + group_id + ", name=" + name
				+ ", age=" + age + ", sex=" + sex + ", beauty=" + beauty + "]";
	}

}
