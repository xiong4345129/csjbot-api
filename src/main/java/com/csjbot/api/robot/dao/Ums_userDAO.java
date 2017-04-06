/**
 * 
 */
package com.csjbot.api.robot.dao;

import com.csjbot.api.common.dao.BaseDAO;
import com.csjbot.api.robot.model.Ums_user;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月21日 上午9:46:09
 * 类说明
 */
public interface Ums_userDAO extends BaseDAO<Ums_user> {
	public abstract Ums_user findUserByName(String username);
}
