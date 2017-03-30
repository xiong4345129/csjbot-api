/**
 * 
 */
package com.csjbot.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.csjbot.service.SnowRobotServiceDAO;
import com.csjbot.util.ResponseUtil;


/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 下午4:49:16
 * 类说明
 */
@Controller
public class SnowRobotController {
	
	@Autowired
	private SnowRobotServiceDAO snowRobotServiceDAO;
	
	// 机器人出库请求相应
	@RequestMapping(value = "sms/outWarehouse", method = RequestMethod.POST)
	@ResponseBody
	public void addRobot(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.addRobot(data));
	}
	
	// 主人注册（机器人端）
	@RequestMapping(value = "sms/masterRegister", method = RequestMethod.POST)
	@ResponseBody
	public void masterRegister(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.addUserFromRobot(data));
	}
	
	// 用户注册（手机端）
	@RequestMapping(value = "sms/userRegister", method = RequestMethod.POST)
	@ResponseBody
	public void userRegister(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.addUserFromMobile(data));
	}
	
	// 验证码获取
	@RequestMapping(value = "sms/getAuthCode", method = RequestMethod.POST)
	@ResponseBody
	public void getAuthCode(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException{
		ResponseUtil.write(response, snowRobotServiceDAO.addAuthCode(data));
	}
	
	// 修改机器人信息
	@RequestMapping(value = "sms/updateRobot", method = RequestMethod.POST)
	@ResponseBody
	public void updateRobot(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.updateRobot(data));
	}
	
	// 用户登录
	@RequestMapping(value = "sms/userLogin", method = RequestMethod.POST)
	@ResponseBody
	public void userLogin(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.userLogin(data));
	}
	
	// 用户登出
	@RequestMapping(value = "sms/userLogout", method = RequestMethod.POST)
	@ResponseBody
	public void userLogout(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.userLogout(data));
	}
	
	// 校验机器人当前注册绑定情况
	@RequestMapping(value = "sms/judgeRobotStatus", method = RequestMethod.POST)
	@ResponseBody
	public void judgeRobotStatus(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.judgeRobotStatus(data));
	}
	
	// 校验用户
	@RequestMapping(value = "sms/judgeUserStatus", method = RequestMethod.POST)
	@ResponseBody
	public void judgeUserStatus(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.judgeUserLogin(data));
	}
	
	// 修改用户昵称
	@RequestMapping(value = "sms/changeUserName", method = RequestMethod.POST)
	@ResponseBody
	public void changeUserName(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.changeUserName(data));
	}
	
	// 修改用户头像
	@RequestMapping(value = "sms/changeUserHead", method = RequestMethod.POST)
	@ResponseBody
	public void changeUserHead(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.changeUserHead(data));
	}
	
	// 忘记用户密码
	@RequestMapping(value = "sms/changeUserPwd", method = RequestMethod.POST)
	@ResponseBody
	public void changeUserPwd(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.changeUserPassword(data));
	}
	
	// 修改用户密码
	@RequestMapping(value = "sms/frogetPassword", method = RequestMethod.POST)
	@ResponseBody
	public void changeUserPwd1(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.ForgetUserPassword(data));
	}
	
	// 修改用户手机号
	@RequestMapping(value = "sms/changeUserMobile", method = RequestMethod.POST)
	@ResponseBody
	public void changeUserMobile(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.changeUserMobile(data));
	}
	
	// 检验手机号和验证码是否一致
	@RequestMapping(value = "sms/judgeMobileCode", method = RequestMethod.POST)
	@ResponseBody
	public void judgeMobileCode(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.judgeMobileCode(data));
	}
	
	// 与服务器端通信
	@RequestMapping(value = "sms/judgeStatus", method = RequestMethod.GET)
	public void judgeStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String type = request.getParameter("type").toString();
		String id = request.getParameter("id").toString();
		if (type.equals("user")) {
			ResponseUtil.write(response, snowRobotServiceDAO.serviceUserJson(type, id));
		}else if(type.equals("robot")){
			ResponseUtil.write(response, snowRobotServiceDAO.serviceRobotJson(type, id));
		}else {
			ResponseUtil.write(response, snowRobotServiceDAO.adminJudgeBindRobot(type, id));
		}
	}
	
	//查询机器人数据（前台）
	@RequestMapping(value = "sms/showInfo", method = RequestMethod.GET)
	@ResponseBody
	public void showInfo(@RequestParam("str") String strss, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.showRobotInfo("%"+strss+"%"));
	}
	
	//删除机器人数据（前台）
	@RequestMapping(value = "sms/deleteInfo", method = RequestMethod.POST)
	@ResponseBody
	public void deleteInfo(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.deleteRobotInfo(data));
	}
	
	//添加组
	@RequestMapping(value = "sms/addGroup", method = RequestMethod.POST)
	@ResponseBody
	public void addGroup(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.addGroup(data));
	}
	
	//删除组
	@RequestMapping(value = "sms/deleteGroup", method = RequestMethod.POST)
	@ResponseBody
	public void deleteGroup(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.deleteGroup(data));
	}
	
	
	//添加组成员
	@RequestMapping(value = "sms/addMember", method = RequestMethod.POST)
	@ResponseBody
	public void addMember(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.addGroupMember(data));
	}
	
	//查询组信息
	@RequestMapping(value = "sms/showGroupInfo", method = RequestMethod.POST)
	@ResponseBody
	public void showGroupInfo(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.showGroupInfo(data));
	}
	
	//删除组关系
	@RequestMapping(value = "sms/deleteUG", method = RequestMethod.POST)
	@ResponseBody
	public void deleteUG(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.deleteGroupRelation(data));
	}
	
	//获得管理员账号
	@RequestMapping(value = "sms/getAdmin", method = RequestMethod.POST)
	@ResponseBody
	public void getAdmin(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.findAdminInfo(data));
	}
	
	//管理员登录
	@RequestMapping(value = "sms/adminLogin", method = RequestMethod.POST)
	@ResponseBody
	public void adminLogin(@RequestBody JSONObject data, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseUtil.write(response, snowRobotServiceDAO.adminLogin(data));
	}
}
