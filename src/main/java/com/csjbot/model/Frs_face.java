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
 * @version 创建时间：2017年3月27日 上午9:49:35
 * 类说明
 */
@Table(name = "frs_face")
public class Frs_face {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String face_id;
	private String person_id;
	private String group_id;
	private String pic_name;
	private String pic_url;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFace_id() {
		return face_id;
	}
	public void setFace_id(String face_id) {
		this.face_id = face_id;
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
	public String getPic_name() {
		return pic_name;
	}
	public void setPic_name(String pic_name) {
		this.pic_name = pic_name;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public Frs_face() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Frs_face(String face_id, String person_id, String group_id, String pic_name, String pic_url) {
		super();
		this.face_id = face_id;
		this.person_id = person_id;
		this.group_id = group_id;
		this.pic_name = pic_name;
		this.pic_url = pic_url;
	}
	@Override
	public String toString() {
		return "Frs_face [id=" + id + ", face_id=" + face_id + ", person_id=" + person_id + ", group_id=" + group_id
				+ ", pic_name=" + pic_name + ", pic_url=" + pic_url + "]";
	}
	
	
}
