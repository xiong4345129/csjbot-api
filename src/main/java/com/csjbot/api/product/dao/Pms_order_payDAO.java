/**
 * 
 */
package com.csjbot.api.product.dao;

import com.csjbot.api.common.dao.BaseDAO;
import com.csjbot.api.product.model.Pms_order_pay;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年4月5日 下午2:20:50
 * 类说明
 */
public interface Pms_order_payDAO extends BaseDAO<Pms_order_pay> {
	public abstract Pms_order_pay findPayByOrderId(String order_id);
	
}
