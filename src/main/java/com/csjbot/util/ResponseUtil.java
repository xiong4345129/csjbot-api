/**
 * 
 */
package com.csjbot.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月17日 下午1:42:35 类说明:将后台处理后的数据返回给前台
 */
public class ResponseUtil {
	/**
	 * 输入json数据
	 */
	public static void write(HttpServletResponse response, Object o) {
		response.setContentType("text/html;charset=utf-8");
		try {
			PrintWriter out = response.getWriter();
			out.println(o.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			backErrorInfo(response,"json数据异常！");
			e.printStackTrace();
			
		}
	}

	/**
	 * 统一返回错误信息 500
	 * 
	 * @throws Exception
	 */
	public static void backErrorInfo(HttpServletResponse response,String message) {
		JSONObject data = new JSONObject();
		JSONObject result = new JSONObject();
		data.put("status", "500");
		data.put("message", message);
		data.put("result", result);
		try {
			write(response, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
