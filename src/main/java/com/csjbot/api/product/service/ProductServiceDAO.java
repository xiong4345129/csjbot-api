/**
 * 
 */
package com.csjbot.api.product.service;

import java.text.ParseException;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月21日 上午9:49:37
 * 类说明
 */
public interface ProductServiceDAO {
	//登录
	public abstract JSONObject login(String account, String password);
	
	//获得产品信息
	public abstract JSONObject getProductInfo();
	
	//获得广告信息
	public abstract JSONObject getADInfo();
	
	//文件下载
	public abstract JSONObject downFile(String fileName);
	
	//验证http头
	public abstract boolean judegHttpHeader(String key, String time, String sign);
	
	//生成订单信息
	public abstract JSONObject addOrder(JSONObject json) throws ParseException;
	
	//查询订单信息
	public abstract JSONObject showOrderInfo(String order_id);

	//根据key查询跳转支付路径
	public abstract  String findRedirectUrl(String key);
}
