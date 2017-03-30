/**
 * 
 */
package com.csjbot.dao;

import java.util.List;

import com.csjbot.model.Sms_user;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 下午4:39:30
 * 类说明
 */
public interface Sms_userDAO extends BaseDAO<Sms_user>{
	//根据账号查询用户
	public abstract Sms_user findUserByAccount(String account);
	
	//模糊查询
	public abstract List<Sms_user> findUserByStr(String str);
}
