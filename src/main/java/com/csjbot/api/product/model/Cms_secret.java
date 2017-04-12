/**
 * 
 */
package com.csjbot.api.product.model;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年4月12日 上午11:08:10
 * 类说明
 */
public class Cms_secret {
	private String customer;
	private String value;
	private String code;
	private String code_group;
	private String remark;
	
	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode_group() {
		return code_group;
	}

	public void setCode_group(String code_group) {
		this.code_group = code_group;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Cms_secret() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Cms_secret(String customer, String value, String code, String code_group, String remark) {
		super();
		this.customer = customer;
		this.value = value;
		this.code = code;
		this.code_group = code_group;
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Cms_secret [customer=" + customer + ", value=" + value + ", code=" + code + ", code_group=" + code_group
				+ ", remark=" + remark + "]";
	}
	
	
}
