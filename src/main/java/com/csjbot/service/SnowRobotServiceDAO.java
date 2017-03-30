/**
 * 
 */
package com.csjbot.service;

import com.alibaba.fastjson.JSONObject;
/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 下午4:49:48 类说明
 */
public interface SnowRobotServiceDAO {

	// 机器人注册(出库)
	public abstract JSONObject addRobot(JSONObject data);

	// 主人注册（机器人端）
	public abstract JSONObject addUserFromRobot(JSONObject data);

	// 用户注册（手机端）
	public abstract JSONObject addUserFromMobile(JSONObject data);

	// 添加验证码
	public abstract JSONObject addAuthCode(JSONObject data);

	// 用户登录
	public abstract JSONObject userLogin(JSONObject data);

	// 用户登出
	public abstract JSONObject userLogout(JSONObject data);

	// 修改机器人信息
	public abstract JSONObject updateRobot(JSONObject data);

	// 校验机器人注册绑定信息
	public abstract JSONObject judgeRobotStatus(JSONObject data);

	// 校验用户（根据账号，取得用户权限，如果是主人还有加上uid）
	public abstract JSONObject judgeUserLogin(JSONObject data);

	// 修改用户昵称
	public abstract JSONObject changeUserName(JSONObject data);

	// 修改用户头像
	public abstract JSONObject changeUserHead(JSONObject data);

	// 用户忘记密码
	public abstract JSONObject ForgetUserPassword(JSONObject data);

	// 修改用户密码
	public abstract JSONObject changeUserPassword(JSONObject data);

	// 修改用户手机号
	public abstract JSONObject changeUserMobile(JSONObject data);

	// 检验手机号和验证码
	public abstract JSONObject judgeMobileCode(JSONObject data);

	// 与服务端通信
	public abstract JSONObject judgeStatus(JSONObject data);
	
	// 返回用户相关信息
	public JSONObject serviceUserJson(String type, String id);

	// 返回机器人相关信息
	public JSONObject serviceRobotJson(String type, String id);

	// 查询机器人数据（前台）
	public abstract JSONObject showRobotInfo(String str);

	// 删除机器人数据（前台）
	public abstract JSONObject deleteRobotInfo(JSONObject data);

	// 添加组
	public abstract JSONObject addGroup(JSONObject data);

	// 删除组
	public abstract JSONObject deleteGroup(JSONObject data);

	// 添加组成员
	public abstract JSONObject addGroupMember(JSONObject data);

	// 查询组信息
	public abstract JSONObject showGroupInfo(JSONObject data);

	// 删除组关系
	public abstract JSONObject deleteGroupRelation(JSONObject data);
	
	//获得管理员账号根据uid 
	public abstract JSONObject findAdminInfo(JSONObject data);
	
	//管理员登录
	public abstract JSONObject adminLogin(JSONObject data);
	
	//管理员绑定机器人查询
	public abstract JSONObject adminJudgeBindRobot(String type, String id);
	
}
