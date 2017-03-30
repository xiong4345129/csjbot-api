/**
 * 
 */
package com.csjbot.dao;

import com.csjbot.model.Pms_secretKey;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月21日 下午12:02:48
 * 类说明
 */
public interface Pms_secretKeyDAO extends BaseDAO<Pms_secretKey>{
	//根据key获得密钥
	public abstract Pms_secretKey getSecretKeyByKey(String key);
	
	//根据user_id获得密钥
	public abstract Pms_secretKey getSecretKeyByUserId(String user_id);
}
