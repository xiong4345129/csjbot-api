/**
 * 
 */
package com.csjbot.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月17日 下午1:45:21
 * 类说明:json工具类
 */
public class JsonUtil {
	/**
	 * object类型数据转成json
	 */
	public static JSONObject toJson(Object object){
		return (JSONObject) JSONObject.toJSON(object);
	}
	
	
	
}
