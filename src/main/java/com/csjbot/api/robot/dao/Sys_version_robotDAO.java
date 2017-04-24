package com.csjbot.api.robot.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.csjbot.api.robot.model.Sys_version_robot;

public interface Sys_version_robotDAO {
	 //根据category,channel查询数据
	public abstract List<Sys_version_robot> findSysVersionBycach(@Param("category")int category,@Param("channel")int channel); 
	
	public abstract Sys_version_robot findSysByVersionCode(@Param("category")int category,@Param("channel")int channel); 
	
	public abstract Sys_version_robot findSysByDateUpdate(@Param("category")int category,@Param("channel")int channel);

}
