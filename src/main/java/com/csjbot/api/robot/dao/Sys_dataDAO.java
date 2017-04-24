package com.csjbot.api.robot.dao;

import com.csjbot.api.robot.model.Sys_data;

public interface Sys_dataDAO {
	 //根据code查询数据
	public abstract Sys_data findCodeById(String code);

}
