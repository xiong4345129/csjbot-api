/**
 * 
 */
package com.csjbot.dao;

import com.csjbot.model.Frs_group;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 上午9:54:51
 * 类说明
 */
public interface Frs_groupDAO extends BaseDAO<Frs_group>{
	
	//根据group_id查询group
	public abstract Frs_group findGroupByGroupId(String group_id);
}
