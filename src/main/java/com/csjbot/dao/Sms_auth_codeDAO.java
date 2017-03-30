/**
 * 
 */
package com.csjbot.dao;

import com.csjbot.model.Sms_auth_code;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 下午4:40:18
 * 类说明
 */
public interface Sms_auth_codeDAO extends BaseDAO<Sms_auth_code>{
	//根据账号查询验证码
	public abstract Sms_auth_code findCodeByAccount(String account);
}
