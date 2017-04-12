/**
 * 
 */
package com.csjbot.api.product.dao;


import com.csjbot.api.common.dao.BaseDAO;
import com.csjbot.api.product.model.Pms_pay_detail_alipay;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年4月6日 下午4:30:21
 * 类说明
 */
public interface Pms_pay_detail_alipayDAO extends BaseDAO<Pms_pay_detail_alipay> {
	//根据out_trade_no来获得明细
	public abstract Pms_pay_detail_alipay findPayByTradeNo(String out_trade_no);
}
