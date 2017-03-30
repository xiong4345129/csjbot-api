/**
 * 
 */
package com.csjbot.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月17日 下午1:45:21 类说明:json工具类
 */
public class JsonUtil {
	private String status;
	private String message;
	private Object result;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "JsonUtil [status=" + status + ", message=" + message + ", result=" + result + "]";
	}

	public JsonUtil(String status, String message, Object result) {
		super();
		this.status = status;
		this.message = message;
		this.result = result;
	}

	public JsonUtil() {
		super();
	}

	/**
	 * object类型数据转成json
	 */
	public static JSONObject toJson(Object object) {
		return (JSONObject) JSONObject.toJSON(object);
	}

}
