/**
 *
 */
package com.csjbot.api.product.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.csjbot.api.product.service.ProductServiceDAO;
import com.csjbot.api.common.util.ResponseUtil;

import java.text.ParseException;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月21日 上午10:00:35 类说明
 */
@Controller
public class ProductController {
    @Autowired
    private ProductServiceDAO productServiceDAO;

	// 登录
	@RequestMapping(value = "api/pdt/login", method = RequestMethod.POST)
	@ResponseBody
	public void login(@RequestBody JSONObject data, HttpServletResponse response) {
		ResponseUtil.write(response, productServiceDAO.login(data.getString("account"), data.getString("password")));
	}

	// 获得产品信息
	@RequestMapping(value = "api/pdt/getRobotProductInfo", method = RequestMethod.GET)
	@ResponseBody
	public void getRobotProductInfo(HttpServletRequest request, HttpServletResponse response) {
		if (judgeHead(request)) {
			ResponseUtil.write(response, productServiceDAO.getProductInfo());
		} else {
			ResponseUtil.backErrorInfo(response, "请求授权失败！");
		}

    }

    // 获得广告信息
    @RequestMapping(value = "api/pdt/getADInfo", method = RequestMethod.GET)
    @ResponseBody
    public void getADInfo(HttpServletRequest request, HttpServletResponse response) {
        if (judgeHead(request)) {
            ResponseUtil.write(response, productServiceDAO.getADInfo());
        } else {
            ResponseUtil.backErrorInfo(response, "请求授权失败！");
        }

    }


	// 文件下载
	@RequestMapping(value = "api/pdt/downFile", method = RequestMethod.POST)
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
	//添加订单
	@RequestMapping(value = "api/pdt/addOrder", method = RequestMethod.POST)
	@ResponseBody
	public void addOrder(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response) throws ParseException, ParseException {
		ResponseUtil.write(response, productServiceDAO.addOrder(data));
	}

	//根据order_id查询订单
	@RequestMapping(value = "api/pdt/showOrderInfo", method = RequestMethod.GET)
	@ResponseBody
	public void showOrderInfo( HttpServletRequest request, HttpServletResponse response) {
		String order_id = request.getParameter("orderId");
		ResponseUtil.write(response, productServiceDAO.showOrderInfo(order_id));
	}
}

