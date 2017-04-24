package com.csjbot.api.robot.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "sys_data_dictionary")
public class Sys_data_dictionary {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer data_fk;
	private String name;
	private String memo;
	private Integer rule;
	private Integer min;
	private Integer max;
	private Integer sort;
	private Byte valid;
	
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getData_fk() {
		return data_fk;
	}


	public void setData_fk(Integer data_fk) {
		this.data_fk = data_fk;
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


	public Integer getRule() {
		return rule;
	}


	public void setRule(Integer rule) {
		this.rule = rule;
	}


	public Integer getMin() {
		return min;
	}


	public void setMin(Integer min) {
		this.min = min;
	}


	public Integer getMax() {
		return max;
	}


	public void setMax(Integer max) {
		this.max = max;
	}


	public Integer getSort() {
		return sort;
	}


	public void setSort(Integer sort) {
		this.sort = sort;
	}


	public Byte getValid() {
		return valid;
	}


	public void setValid(Byte valid) {
		this.valid = valid;
	}


	public Sys_data_dictionary() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Sys_data_dictionary(Integer id, Integer data_fk, String name, String memo, Integer rule, Integer min,
			Integer max, Integer sort, Byte valid) {
		super();
		this.id = id;
		this.data_fk = data_fk;
		this.name = name;
		this.memo = memo;
		this.rule = rule;
		this.min = min;
		this.max = max;
		this.sort = sort;
		this.valid = valid;
	}


	@Override
	public String toString() {
		return "Sys_data_dictionary [id=" + id + ", data_fk=" + data_fk + ", name=" + name + ", memo=" + memo
				+ ", rule=" + rule + ", min=" + min + ", max=" + max + ", sort=" + sort + ", valid=" + valid + "]";
	}



}
