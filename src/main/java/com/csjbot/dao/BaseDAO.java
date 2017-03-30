package com.csjbot.dao;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/** 
* @author 作者 :ZYY 
* @version 创建时间：2016年12月16日 上午8:49:20 
* 类说明 :
* 通用DAO接口，继承于通用Mappwer
*/
public interface BaseDAO<T> extends Mapper<T>,MySqlMapper<T>{

}
