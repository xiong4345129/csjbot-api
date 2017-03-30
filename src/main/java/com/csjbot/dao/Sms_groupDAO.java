/**
 * 
 */
package com.csjbot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.csjbot.model.Sms_group;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 下午4:40:50
 * 类说明
 */
public interface Sms_groupDAO extends BaseDAO<Sms_group>{
	
	//根据master账号查询组
	public abstract List<Sms_group> findGroupByAccount(String account);
	
	//根据name查询组
	public abstract List<Sms_group> findGroupByName(String name);
	
	//根据账号和名称查询组
	public abstract Sms_group findGroupByNA(@Param("account")String account,@Param("name")String name);
}
