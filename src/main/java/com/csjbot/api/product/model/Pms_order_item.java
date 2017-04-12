/**
 * 
 */
package com.csjbot.api.product.model;

import javax.persistence.Table;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年4月5日 下午2:13:32
 * 类说明
 */
@Table(name = "pms_order_item")
public class Pms_order_item {
	private String order_id;
	private String item_id;
	private Integer item_qty;
	private Double unit_price;
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public Integer getItem_qty() {
		return item_qty;
	}
	public void setItem_qty(Integer item_qty) {
		this.item_qty = item_qty;
	}
	public Double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(Double unit_price) {
		this.unit_price = unit_price;
	}
	public Pms_order_item() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Pms_order_item(String order_id, String item_id, Integer item_qty, Double unit_price) {
		super();
		this.order_id = order_id;
		this.item_id = item_id;
		this.item_qty = item_qty;
		this.unit_price = unit_price;
	}
	@Override
	public String toString() {
		return "Pms_order_detail [order_id=" + order_id + ", item_id=" + item_id + ", item_qty=" + item_qty
				+ ", unit_price=" + unit_price + "]";
	}
	
	
}
