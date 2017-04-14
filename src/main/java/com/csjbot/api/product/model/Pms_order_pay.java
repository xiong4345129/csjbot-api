/**
 * 
 */
package com.csjbot.api.product.model;

import java.sql.Timestamp;

import javax.persistence.Table;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年4月5日 下午2:05:44
 * 类说明
 */
@Table(name = "pms_order_pay")
public class Pms_order_pay {
	private Timestamp create_time;
	private Timestamp update_time;
	private String order_id;
	private Timestamp order_time;
	private String order_pseudo_no;
	private String order_device_group;
	private String order_device_id;
	private Integer order_total_fee;
	private String order_status;
	private String order_err_code;
	private String order_err_desc;
	private String pay_service;
	private String pay_status;
	private String pay_err_code;
	private String pay_err_desc;
	private Integer is_closed;
	private String remark;
	private Timestamp close_time;

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public Timestamp getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Timestamp update_time) {
		this.update_time = update_time;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public Timestamp getOrder_time() {
		return order_time;
	}

	public void setOrder_time(Timestamp order_time) {
		this.order_time = order_time;
	}

	public String getOrder_pseudo_no() {
		return order_pseudo_no;
	}

	public void setOrder_pseudo_no(String order_pseudo_no) {
		this.order_pseudo_no = order_pseudo_no;
	}

	public String getOrder_device_group() {
		return order_device_group;
	}

	public void setOrder_device_group(String order_device_group) {
		this.order_device_group = order_device_group;
	}

	public String getOrder_device_id() {
		return order_device_id;
	}

	public void setOrder_device_id(String order_device_id) {
		this.order_device_id = order_device_id;
	}

	public Integer getOrder_total_fee() {
		return order_total_fee;
	}

	public void setOrder_total_fee(Integer order_total_fee) {
		this.order_total_fee = order_total_fee;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getOrder_err_code() {
		return order_err_code;
	}

	public void setOrder_err_code(String order_err_code) {
		this.order_err_code = order_err_code;
	}

	public String getOrder_err_desc() {
		return order_err_desc;
	}

	public void setOrder_err_desc(String order_err_desc) {
		this.order_err_desc = order_err_desc;
	}

	public String getPay_service() {
		return pay_service;
	}

	public void setPay_service(String pay_service) {
		this.pay_service = pay_service;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}

	public String getPay_err_code() {
		return pay_err_code;
	}

	public void setPay_err_code(String pay_err_code) {
		this.pay_err_code = pay_err_code;
	}

	public String getPay_err_desc() {
		return pay_err_desc;
	}

	public void setPay_err_desc(String pay_err_desc) {
		this.pay_err_desc = pay_err_desc;
	}

	public Integer getIs_closed() {
		return is_closed;
	}

	public void setIs_closed(Integer is_closed) {
		this.is_closed = is_closed;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getClose_time() {
		return close_time;
	}

	public void setClose_time(Timestamp close_time) {
		this.close_time = close_time;
	}

	public Pms_order_pay() {
		super();
	}

	public Pms_order_pay(Timestamp create_time, Timestamp update_time, String order_id, Timestamp order_time, String order_pseudo_no, String order_device_group, String order_device_id, Integer order_total_fee, String order_status, String order_err_code, String order_err_desc, String pay_service, String pay_status, String pay_err_code, String pay_err_desc, Integer is_closed, String remark, Timestamp close_time) {
		this.create_time = create_time;
		this.update_time = update_time;
		this.order_id = order_id;
		this.order_time = order_time;
		this.order_pseudo_no = order_pseudo_no;
		this.order_device_group = order_device_group;
		this.order_device_id = order_device_id;
		this.order_total_fee = order_total_fee;
		this.order_status = order_status;
		this.order_err_code = order_err_code;
		this.order_err_desc = order_err_desc;
		this.pay_service = pay_service;
		this.pay_status = pay_status;
		this.pay_err_code = pay_err_code;
		this.pay_err_desc = pay_err_desc;
		this.is_closed = is_closed;
		this.remark = remark;
		this.close_time = close_time;
	}

	@Override
	public String toString() {
		return "Pms_order_pay{" +
				"create_time=" + create_time +
				", update_time=" + update_time +
				", order_id='" + order_id + '\'' +
				", order_time=" + order_time +
				", order_pseudo_no='" + order_pseudo_no + '\'' +
				", order_device_group='" + order_device_group + '\'' +
				", order_device_id='" + order_device_id + '\'' +
				", order_total_fee=" + order_total_fee +
				", order_status='" + order_status + '\'' +
				", order_err_code='" + order_err_code + '\'' +
				", order_err_desc='" + order_err_desc + '\'' +
				", pay_service='" + pay_service + '\'' +
				", pay_status='" + pay_status + '\'' +
				", pay_err_code='" + pay_err_code + '\'' +
				", pay_err_desc='" + pay_err_desc + '\'' +
				", is_closed=" + is_closed +
				", remark='" + remark + '\'' +
				", close_time=" + close_time +
				'}';
	}
}
