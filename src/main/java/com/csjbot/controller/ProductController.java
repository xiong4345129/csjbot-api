/**
 * 
 */
package com.csjbot.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.csjbot.service.ProductServiceDAO;
import com.csjbot.util.ResponseUtil;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月21日 上午10:00:35 类说明
 */
@Controller
public class ProductController {
	@Autowired
	private ProductServiceDAO productServiceDAO;

	// 登录
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public void login(@RequestBody JSONObject data, HttpServletResponse response) {
		ResponseUtil.write(response, productServiceDAO.login(data.getString("account"), data.getString("password")));
	}

	// 获得产品信息
	@RequestMapping(value = "getRobotProductInfo", method = RequestMethod.GET)
	@ResponseBody
	public void getRobotProductInfo(HttpServletRequest request, HttpServletResponse response) {
		if (judgeHead(request)) {
			ResponseUtil.write(response, productServiceDAO.getProductInfo());
		} else {
			ResponseUtil.backErrorInfo(response, "请求授权失败！");
		}

	}

	// 获得广告信息
	@RequestMapping(value = "getADInfo", method = RequestMethod.GET)
	@ResponseBody
	public void getADInfo(HttpServletRequest request, HttpServletResponse response) {
		if (judgeHead(request)) {
			ResponseUtil.write(response, productServiceDAO.getADInfo());
		} else {
			ResponseUtil.backErrorInfo(response, "请求授权失败！");
		}

	}

	// 文件下载
	@RequestMapping(value = "downFile", method = RequestMethod.POST)
	@ResponseBody
	public void downFile(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response) {
		if (judgeHead(request)) {
			ResponseUtil.write(response, productServiceDAO.downFile(data.getString("fileName")));
		} else {
			ResponseUtil.backErrorInfo(response, "请求授权失败！");
		}
	}

	// 验证http 头内容
	public boolean judgeHead(HttpServletRequest request) {
		String key = request.getHeader("key").toString();
		String time = request.getHeader("time").toString();
		String sign = request.getHeader("sign").toString();
		boolean flag = productServiceDAO.judegHttpHeader(key, time, sign);
		return flag;
	}
}
