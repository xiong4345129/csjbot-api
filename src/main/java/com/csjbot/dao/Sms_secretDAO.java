/**
 * 
 */
package com.csjbot.dao;

import com.csjbot.model.Sms_secret;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 下午4:43:04
 * 类说明
 */
public interface Sms_secretDAO extends BaseDAO<Sms_secret>{
	//根据账号获得密钥
	public abstract Sms_secret findSecretByAccount(String account);
}
