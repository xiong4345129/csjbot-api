/**
 * 
 */
package com.csjbot.dao;

import java.util.List;

import com.csjbot.model.Sys_attachment;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月21日 上午9:45:45
 * 类说明
 */
public interface Sys_attachmentDAO extends BaseDAO<Sys_attachment>{
	//根据产品id获得文件
	public abstract List<Sys_attachment> getSystByProId(String transaction_id);
	
	//根据文件名来获得文件
	public abstract Sys_attachment getSystByName(String fileName);
}
