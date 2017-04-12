/**
 * 
 */
package com.csjbot.api.product.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年4月6日 下午4:20:37
 * 类说明
 */
@Table(name = "pms_pay_detail_alipay")
public class Pms_pay_detail_alipay {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String buyer_logon_id;
	private String buyer_pay_amount;
	private String buyer_user_id;
	private String code;
	private String invoice_amount;
	private String msg;
	private String open_id;
	private String out_trade_no;
	private String point_amount;
	private String receipt_amount;
	private String send_pay_date;
	private String total_amount;
	private String trade_no;
	private String trade_status;
	private String sign;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBuyer_logon_id() {
		return buyer_logon_id;
	}
	public void setBuyer_logon_id(String buyer_logon_id) {
		this.buyer_logon_id = buyer_logon_id;
	}
	public String getBuyer_pay_amount() {
		return buyer_pay_amount;
	}
	public void setBuyer_pay_amount(String buyer_pay_amount) {
		this.buyer_pay_amount = buyer_pay_amount;
	}
	public String getBuyer_user_id() {
		return buyer_user_id;
	}
	public void setBuyer_user_id(String buyer_user_id) {
		this.buyer_user_id = buyer_user_id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getInvoice_amount() {
		return invoice_amount;
	}
	public void setInvoice_amount(String invoice_amount) {
		this.invoice_amount = invoice_amount;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getPoint_amount() {
		return point_amount;
	}
	public void setPoint_amount(String point_amount) {
		this.point_amount = point_amount;
	}
	public String getReceipt_amount() {
		return receipt_amount;
	}
	public void setReceipt_amount(String receipt_amount) {
		this.receipt_amount = receipt_amount;
	}
	public String getSend_pay_date() {
		return send_pay_date;
	}
	public void setSend_pay_date(String send_pay_date) {
		this.send_pay_date = send_pay_date;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public String getTrade_status() {
		return trade_status;
	}
	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Pms_pay_detail_alipay() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Pms_pay_detail_alipay(String buyer_logon_id, String buyer_pay_amount, String buyer_user_id, String code,
			String invoice_amount, String msg, String open_id, String out_trade_no, String point_amount,
			String receipt_amount, String send_pay_date, String total_amount, String trade_no, String trade_status,
			String sign) {
		super();
		this.buyer_logon_id = buyer_logon_id;
		this.buyer_pay_amount = buyer_pay_amount;
		this.buyer_user_id = buyer_user_id;
		this.code = code;
		this.invoice_amount = invoice_amount;
		this.msg = msg;
		this.open_id = open_id;
		this.out_trade_no = out_trade_no;
		this.point_amount = point_amount;
		this.receipt_amount = receipt_amount;
		this.send_pay_date = send_pay_date;
		this.total_amount = total_amount;
		this.trade_no = trade_no;
		this.trade_status = trade_status;
		this.sign = sign;
	}
	@Override
	public String toString() {
		return "Pms_pay_detail_alipay [id=" + id + ", buyer_logon_id=" + buyer_logon_id + ", buyer_pay_amount="
				+ buyer_pay_amount + ", buyer_user_id=" + buyer_user_id + ", code=" + code + ", invoice_amount="
				+ invoice_amount + ", msg=" + msg + ", open_id=" + open_id + ", out_trade_no=" + out_trade_no
				+ ", point_amount=" + point_amount + ", receipt_amount=" + receipt_amount + ", send_pay_date="
				+ send_pay_date + ", total_amount=" + total_amount + ", trade_no=" + trade_no + ", trade_status="
				+ trade_status + ", sign=" + sign + "]";
	}
	

}
