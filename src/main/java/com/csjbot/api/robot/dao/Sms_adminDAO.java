/**
 * 
 */
package com.csjbot.api.robot.dao;

import com.csjbot.api.common.dao.BaseDAO;
import com.csjbot.api.robot.model.Sms_admin;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月30日 下午1:50:59
 * 类说明
 */
public interface Sms_adminDAO extends BaseDAO<Sms_admin> {
	//根据uid查询用户
	public abstract Sms_admin findAdminByUid(String uid);
	
	//根据账号查询用户
	public abstract Sms_admin findAdminByAccount(String account);

}
