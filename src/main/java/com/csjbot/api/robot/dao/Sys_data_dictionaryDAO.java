package com.csjbot.api.robot.dao;

import org.apache.ibatis.annotations.Param;

import com.csjbot.api.robot.model.Sys_data_dictionary;

public interface Sys_data_dictionaryDAO {
	//根据data_fk,memo查询数据字典
	public abstract Sys_data_dictionary findDataDictionaryById(@Param("data_fk")int data_fk,@Param("memo")String memo);
	
	

}
