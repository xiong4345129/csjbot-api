package com.csjbot.api.robot.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "sys_data")
public class Sys_data {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String code;
	private String name;
	private String memo;
	private Byte valid;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Byte getValid() {
		return valid;
	}
	public void setValid(Byte valid) {
		this.valid = valid;
	}
	public Sys_data() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Sys_data(Integer id, String code, String name, String memo, Byte valid) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.memo = memo;
		this.valid = valid;
	}
	@Override
	public String toString() {
		return "Sys_data [id=" + id + ", code=" + code + ", name=" + name + ", memo=" + memo + ", valid=" + valid + "]";
	}
	
	

}
