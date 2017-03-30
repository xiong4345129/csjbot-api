/**
 * 
 */
package com.csjbot.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.csjbot.dao.Sms_adminDAO;
import com.csjbot.dao.Sms_auth_codeDAO;
import com.csjbot.dao.Sms_groupDAO;
import com.csjbot.dao.Sms_robotDAO;
import com.csjbot.dao.Sms_secretDAO;
import com.csjbot.dao.Sms_ug_relationsDAO;
import com.csjbot.dao.Sms_userDAO;
import com.csjbot.model.Sms_admin;
import com.csjbot.model.Sms_auth_code;
import com.csjbot.model.Sms_group;
import com.csjbot.model.Sms_robot;
import com.csjbot.model.Sms_secret;
import com.csjbot.model.Sms_ug_relations;
import com.csjbot.model.Sms_user;
import com.csjbot.util.CharacterUtil;
import com.csjbot.util.JsonUtil;
import com.csjbot.util.RandomUtil;
import com.csjbot.util.RestTest;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 下午4:50:03 类说明
 */
@Service
public class SnowRobotServiceDAOImpl implements SnowRobotServiceDAO {
	@Autowired
	private Sms_auth_codeDAO sms_auth_codeDAO;

	@Autowired
	private Sms_groupDAO sms_groupDAO;

	@Autowired
	private Sms_robotDAO sms_robotDAO;

	@Autowired
	private Sms_secretDAO secretDAO;

	@Autowired
	private Sms_ug_relationsDAO sms_ug_relationsDAO;

	@Autowired
	private Sms_userDAO sms_userDAO;

	@Autowired
	private Sms_adminDAO sms_adminDAO;

	// 机器人出库
	@Override
	public JSONObject addRobot(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "uid" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			// 判断机器人是否出库
			Sms_robot sms_robot = sms_robotDAO.findRobotByUid(data.get("uid").toString());
			if (sms_robot == null) {
				sms_robot = new Sms_robot();
				sms_robot.setUid(data.get("uid").toString());
				sms_robot.setActivate(0);
				sms_robot.setMaster_account("0");
				sms_robot.setOut_time(RandomUtil.getTimeStampFor());
				int n = sms_robotDAO.insert(sms_robot);
				if (n != 1) {
					jsonUtil = getJsonUtilEntity(false);
					jsonUtil.setMessage("Error from Database operations!");
				} else {
					// 管理员添加
					String account = "admin" + RandomUtil.getCreditCode();
					Sms_admin sms_admin = new Sms_admin(account, "123456", "管理员", 3, "", "", 0,
							RandomUtil.getTimeStampFor(), data.getString("uid"));
					// 管理员secret添加
					Sms_secret secret = new Sms_secret(account, RandomUtil.generateString(25));
					int m = sms_adminDAO.insert(sms_admin);
					if (m == 1) {
						secretDAO.insert(secret);
					}
				}
			} else {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The uid already exists");
			}

		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 主人注册
	@Override
	public JSONObject addUserFromRobot(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "account", "password", "uid", "name", "auth_code" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_user sms_user = sms_userDAO.findUserByAccount(data.get("account").toString());
			Sms_auth_code sms_auth_code = sms_auth_codeDAO.findCodeByAccount(data.get("account").toString());
			Sms_robot sms_robot = sms_robotDAO.findRobotByUid(data.get("uid").toString());

			if (sms_user != null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("User already exists!");
			} else if (sms_auth_code == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The auth_code does not exist!");
			} else if ("0".equals(sms_robot.getMaster_account())) {
				boolean b = data.get("auth_code").toString().equals(sms_auth_code.getAuth_code());
				if (b) {
					sms_robot.setMaster_account(data.get("account").toString());
					sms_robot.setName(data.get("name").toString());
					sms_robot.setActivate(1);
					sms_robot.setActivate_time(RandomUtil.getTimeStampFor());
					sms_robotDAO.updateByPrimaryKey(sms_robot);
					sms_user = new Sms_user();
					sms_user.setAccount(data.get("account").toString());
					sms_user.setPassword(data.get("password").toString());
					sms_user.setName(data.get("name").toString() + "的主人");
					sms_user.setRole(1);
					sms_user.setCreat_time(RandomUtil.getTimeStampFor());
					sms_user.setStatus(0);
					sms_user.setHead_name("");
					sms_user.setHead_url("");
					sms_userDAO.insert(sms_user);
				} else {
					jsonUtil = getJsonUtilEntity(false);
					jsonUtil.setMessage("Verification code is not correct!");
				}
			} else {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The master of the robot already exists!");
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 用户注册（手机端）
	@Override
	public JSONObject addUserFromMobile(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "account", "password", "name", "auth_code", "headName", "headData" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_user sms_user = sms_userDAO.findUserByAccount(data.get("account").toString());
			Sms_auth_code sms_auth_code = sms_auth_codeDAO.findCodeByAccount(data.get("account").toString());
			if (sms_user != null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The user already exists!");
			} else if (sms_auth_code == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The auth_code does not exist!");
			} else if (!data.get("auth_code").toString().equals(sms_auth_code.getAuth_code())) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("Verification code is not correct!");
			} else {
				sms_user = new Sms_user();
				sms_user.setAccount(data.get("account").toString());
				sms_user.setPassword(data.get("password").toString());
				sms_user.setHead_name(data.get("headName").toString());
				sms_user.setHead_url(data.get("headData").toString());
				sms_user.setName(data.get("name").toString());
				sms_user.setRole(0);
				sms_user.setStatus(0);
				sms_user.setCreat_time(RandomUtil.getTimeStampFor());
				sms_userDAO.insert(sms_user);
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 获取验证码
	@Override
	public JSONObject addAuthCode(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		Map<String, Object> map = new HashMap<>();
		String[] key = { "account" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_auth_code sms_auth_code = sms_auth_codeDAO.findCodeByAccount(data.get("account").toString());
			if (sms_auth_code == null) {
				String auth_code = RandomUtil.getCreditCode();
				// 发送短信
				String result = RestTest.sendMesg(data.getString("account"), auth_code);
				JSONObject jsonObject = JSONObject.parseObject(result);
				JSONObject resp = jsonObject.getJSONObject("resp");
				if ("000000".equals(resp.get("respCode").toString())) {
					sms_auth_code = new Sms_auth_code();
					sms_auth_code.setAccount(data.get("account").toString());
					sms_auth_code.setAuth_code(auth_code);
					sms_auth_code.setTime(RandomUtil.getTimeStampFor());
					sms_auth_codeDAO.insert(sms_auth_code);
					map.put("auth_code", auth_code);
					jsonUtil.setResult(map);
				} else {
					jsonUtil = getJsonUtilEntity(false);
					jsonUtil.setMessage("Message sent failure!");
				}

			} else {
				String auth_code = RandomUtil.getCreditCode();
				// 发送短信
				String result = RestTest.sendMesg(data.getString("account"), auth_code);
				JSONObject jsonObject = JSONObject.parseObject(result);
				JSONObject resp = jsonObject.getJSONObject("resp");
				if ("000000".equals(resp.get("respCode").toString())) {
					sms_auth_code.setAuth_code(auth_code);
					sms_auth_code.setTime(RandomUtil.getTimeStampFor());
					sms_auth_codeDAO.updateByPrimaryKey(sms_auth_code);
					map.put("auth_code", auth_code);
					jsonUtil.setResult(map);
				} else {
					jsonUtil = getJsonUtilEntity(false);
					jsonUtil.setMessage("Message sent failure!");
				}
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 用户登录
	@Override
	public JSONObject userLogin(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		Map<String, Object> result = new HashMap<>();
		String[] key = { "account", "password" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_user sms_user = sms_userDAO.findUserByAccount(data.getString("account"));
			Sms_secret secret = secretDAO.findSecretByAccount(data.getString("account"));
			if (sms_user == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The user does not exist!");

			} else if (!data.getString("password").equals(sms_user.getPassword())) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("Error from password!");
			} else {
				sms_user.setStatus(1);
				sms_userDAO.updateByPrimaryKey(sms_user);
				if (secret == null) {
					secret = new Sms_secret();
					String str = RandomUtil.generateString(25);
					secret.setAccount(data.getString("account"));
					secret.setSecret(str);
					secretDAO.insert(secret);
					result.put("secret", str);
					jsonUtil.setResult(result);
				} else {
					String str = RandomUtil.generateString(25);
					secret.setSecret(str);
					secretDAO.updateByPrimaryKey(secret);
					result.put("secret", str);
					jsonUtil.setResult(result);
				}
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 用户登出
	@Override
	public JSONObject userLogout(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "account" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_user sms_user = sms_userDAO.findUserByAccount(data.getString("account"));
			Sms_secret secret = secretDAO.findSecretByAccount(data.getString("account"));
			if (sms_user == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The user does not exist!");
			} else {
				sms_user.setStatus(0);
				sms_userDAO.updateByPrimaryKey(sms_user);
				if (secret != null) {
					secret.setSecret("0");
					secretDAO.updateByPrimaryKey(secret);
				}
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	@Override
	public JSONObject updateRobot(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "uid", "name" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_robot sms_robot = sms_robotDAO.findRobotByUid(data.getString("uid"));
			if (sms_robot != null) {
				sms_robot.setName(data.getString("name"));
				sms_robotDAO.updateByPrimaryKey(sms_robot);
			} else {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The robot does not exist!");
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 校验机器人状态
	@Override
	public JSONObject judgeRobotStatus(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "uid" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_robot sms_robot = sms_robotDAO.findRobotByUid(data.getString("uid"));
			if (sms_robot == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The robot does not exist!");
			} else if (sms_robot.getMaster_account() == "" || sms_robot.getMaster_account() == null) {
				jsonUtil.setMessage("master account is null!");
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 用户登录后权限验证（是否是机器人主人）
	@Override
	public JSONObject judgeUserLogin(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		Map<String, Object> result = new HashMap<>();
		String[] key = { "account" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_robot sms_robot = sms_robotDAO.findRobotByAccount(data.getString("account"));
			if (sms_robot == null) {
				result.put("permission", false);
				result.put("activate", 0);
				result.put("uid", 0);
				jsonUtil.setResult(result);
			} else {
				result.put("permission", true);
				result.put("activate", sms_robot.getActivate());
				result.put("uid", sms_robot.getUid());
				jsonUtil.setResult(result);
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 修改用户名称
	@Override
	public JSONObject changeUserName(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "account", "name" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_user sms_user = sms_userDAO.findUserByAccount(data.getString("account"));
			if (sms_user == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The user does not exist!");
			} else {
				sms_user.setName(data.getString("name"));
				sms_userDAO.updateByPrimaryKey(sms_user);
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 修改用户头像
	@Override
	public JSONObject changeUserHead(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "account", "headName", "headData" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_user sms_user = sms_userDAO.findUserByAccount(data.getString("account"));
			if (sms_user == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The user does not exist!");
			} else {
				sms_user.setHead_name(data.getString("headName"));
				sms_user.setHead_url(data.getString("headData"));
				sms_userDAO.updateByPrimaryKey(sms_user);
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 用户忘记密码
	@Override
	public JSONObject ForgetUserPassword(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "account", "auth_code", "newPassword" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_user sms_user = sms_userDAO.findUserByAccount(data.getString("account"));
			Sms_auth_code sms_auth_code = sms_auth_codeDAO.findCodeByAccount(data.getString("account"));

			if (sms_user == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The user does not exist!");
			} else if (sms_auth_code == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The code does not exist!");
			} else if (data.getString("auth_code").equals(sms_auth_code.getAuth_code())) {
				sms_user.setPassword(data.getString("newPassword"));
				sms_userDAO.updateByPrimaryKey(sms_user);
			} else {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("Verification code is not correct!");
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 修改用户密码
	@Override
	public JSONObject changeUserPassword(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "account", "password", "newPassword" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_user sms_user = sms_userDAO.findUserByAccount(data.getString("account"));
			if (sms_user == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The user does not exist!");
			} else if (data.getString("password").equals(sms_user.getPassword())) {
				sms_user.setPassword(data.getString("newPassword"));
				sms_userDAO.updateByPrimaryKey(sms_user);
			} else {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("Password is not correct!");
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 修改手机号
	@Override
	public JSONObject changeUserMobile(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "mobile", "password", "newMobile", "auth_code" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			// 先验证原来账号是否正确
			Sms_user sms_user = sms_userDAO.findUserByAccount(data.getString("mobile"));
			Sms_user sms_user2 = sms_userDAO.findUserByAccount(data.getString("newMobile"));
			Sms_auth_code sms_auth_code = sms_auth_codeDAO.findCodeByAccount(data.getString("newMobile"));
			Sms_robot sms_robot = sms_robotDAO.findRobotByAccount(data.getString("mobile"));
			Sms_secret secret = secretDAO.findSecretByAccount(data.getString("mobile"));
			if (sms_user == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The user does not exist!");
			} else if (!data.getString("password").equals(sms_user.getPassword())) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("Password is not correct!");
			} else if (sms_auth_code == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("Verification code is not correct!");
			} else if (sms_user2 != null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The mobile already exists!");
			} else if (data.getString("auth_code").equals(sms_auth_code.getAuth_code())) {
				sms_user.setAccount(data.getString("newMobile"));
				sms_userDAO.updateByPrimaryKey(sms_user);
				if (sms_robot != null) {
					sms_robot.setMaster_account(data.getString("newMobile"));
					sms_robotDAO.updateByPrimaryKey(sms_robot);
				}
				if (secret != null) {
					secret.setAccount(data.getString("newMobile"));
					secretDAO.updateByPrimaryKey(secret);
				}
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 校验手机号和验证码是否一致（备用）
	@Override
	public JSONObject judgeMobileCode(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "account", "auth_code" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_auth_code sms_auth_code = sms_auth_codeDAO.findCodeByAccount(data.getString("account"));
			if (sms_auth_code == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("Verification code is not correct!");
			} else if (!data.getString("auth_code").equals(sms_auth_code.getAuth_code())) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("Verification code is not correct!");
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	@Override
	public JSONObject judgeStatus(JSONObject data) {

		return null;
	}

	// 前台模糊查询用户或机器人信息
	@Override
	public JSONObject showRobotInfo(String str) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		List<Object> result = new ArrayList<>();
		Map<String, Object> member = new HashMap<>();
		str = str.replace("\"", "");
		List<Sms_user> userList = sms_userDAO.findUserByStr(str);
		List<Sms_robot> robotList = sms_robotDAO.findRobotByStr(str);
		if (userList != null) {
			for (Sms_user sms_user : userList) {
				member.put("id", sms_user.getAccount().toString());
				member.put("type", "user");
				member.put("creatTime", sms_user.getCreat_time().toString());
				Sms_robot sms_robot = sms_robotDAO.findRobotByAccount(sms_user.getAccount().toString());
				if (sms_robot != null) {
					member.put("bind", sms_robot.getUid().toString());
				} else {
					member.put("bind", "");
				}
				result.add(member);
			}
			jsonUtil.setResult(result);

		}
		if (robotList != null) {
			for (Sms_robot sms_robot : robotList) {
				member.put("id", sms_robot.getUid().toString());
				member.put("type", "robot");
				member.put("creatTime", sms_robot.getOut_time().toString());
				Sms_user sms_user = sms_userDAO.findUserByAccount(sms_robot.getMaster_account().toString());
				if (sms_user != null) {
					member.put("bind", sms_user.getAccount().toString());
				} else {
					member.put("bind", "");
				}
				result.add(member);
			}
			jsonUtil.setResult(result);
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Query information does not exist");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 前台删除机器人或用户信息
	@Override
	public JSONObject deleteRobotInfo(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "id", "type" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			if (data.getString("type").equals("user")) {
				Sms_user sms_user = sms_userDAO.findUserByAccount(data.getString("id"));
				if (sms_user != null) {
					Sms_secret secret = secretDAO.findSecretByAccount(sms_user.getAccount().toString());
					Sms_auth_code sms_auth_code = sms_auth_codeDAO.findCodeByAccount(sms_user.getAccount().toString());
					Sms_robot sms_robot = sms_robotDAO.findRobotByAccount(sms_user.getAccount().toString());
					sms_userDAO.delete(sms_user);
					sms_auth_codeDAO.delete(sms_auth_code);
					secretDAO.delete(secret);
					sms_robotDAO.delete(sms_robot);
				} else {
					jsonUtil = getJsonUtilEntity(false);
					jsonUtil.setMessage("The user does not exist");
				}
			} else {
				Sms_robot sms_robot = sms_robotDAO.findRobotByAccount(data.getString("id"));
				if (sms_robot != null) {
					Sms_user sms_user = sms_userDAO.findUserByAccount(sms_robot.getMaster_account().toString());
					if (sms_user != null) {
						Sms_secret secret = secretDAO.findSecretByAccount(sms_user.getAccount().toString());
						Sms_auth_code sms_auth_code = sms_auth_codeDAO
								.findCodeByAccount(sms_user.getAccount().toString());
						sms_userDAO.delete(sms_user);
						sms_auth_codeDAO.delete(sms_auth_code);
						secretDAO.delete(secret);
					}
					sms_robotDAO.delete(sms_robot);
				} else {
					jsonUtil = getJsonUtilEntity(false);
					jsonUtil.setMessage("The robot does not exist");
				}
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 添加组
	@Override
	public JSONObject addGroup(JSONObject data) {
		boolean flag = true;
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "group_name", "master" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			List<Sms_group> list = sms_groupDAO.findGroupByAccount(data.getString("master"));
			for (Sms_group sms_group : list) {
				if (sms_group.getName().equals(data.getString("group_name"))) {
					flag = false;
				}
			}
			if (flag) {
				Sms_group sms_group = new Sms_group();
				sms_group.setCreat_time(RandomUtil.getTimeStampFor());
				sms_group.setMaster_account(data.getString("master"));
				sms_group.setName(data.getString("group_name"));
				sms_group.setUser_numbers(1);
				sms_groupDAO.insert(sms_group);

			} else {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("Group already exists");
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 删除组
	@Override
	public JSONObject deleteGroup(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "group_name", "master" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_group sms_group = sms_groupDAO.findGroupByNA(data.getString("master"), data.getString("group_name"));
			if (sms_group != null) {
				List<Sms_ug_relations> list = sms_ug_relationsDAO.findRelationByGid(sms_group.getId());
				for (Sms_ug_relations sms_ug_relations : list) {
					sms_ug_relationsDAO.delete(sms_ug_relations);
				}
				sms_groupDAO.delete(sms_group);

			} else {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The group does not exist");
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 添加组成员
	@Override
	public JSONObject addGroupMember(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "group_name", "add_account", "add_password", "master" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_group sms_group = sms_groupDAO.findGroupByNA(data.getString("master"), data.getString("group_name"));
			Sms_user sms_user = sms_userDAO.findUserByAccount(data.getString("add_account"));
			if (sms_user == null) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The user does not exist");
			} else if (!sms_user.getPassword().equals(data.getString("add_password"))) {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The password is not correct");
			} else if (sms_group != null) {
				sms_group.setUser_numbers(sms_group.getUser_numbers() + 1);
				Sms_ug_relations sms_ug_relations = sms_ug_relationsDAO
						.findRelationByGidAcc(data.getString("add_account"), sms_group.getId());
				if (sms_ug_relations != null) {
					jsonUtil = getJsonUtilEntity(false);
					jsonUtil.setMessage("The member already exists");
				} else {
					sms_ug_relations = new Sms_ug_relations();
					sms_ug_relations.setAccount(data.getString("add_account"));
					sms_ug_relations.setGroup_id(sms_group.getId());
					int n = sms_ug_relationsDAO.insert(sms_ug_relations);
					if (n == 1) {
						sms_groupDAO.updateByPrimaryKey(sms_group);
					}

				}
			} else {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The group does not exist");
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 查询组信息
	@Override
	public JSONObject showGroupInfo(JSONObject data) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> person = new HashMap<>();
		List<Object> member = new ArrayList<>();
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "group_name", "master" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_group sms_group = sms_groupDAO.findGroupByNA(data.getString("master"), data.getString("group_name"));
			if (sms_group != null) {
				List<Sms_ug_relations> ugList = sms_ug_relationsDAO.findRelationByGid(sms_group.getId());
				if (ugList.size() > 0) {
					for (Sms_ug_relations sms_ug_relations : ugList) {
						Sms_user sms_user = sms_userDAO.findUserByAccount(sms_ug_relations.getAccount().toString());
						if (sms_user != null) {
							person = new HashMap<>();
							person.put("name", sms_user.getName().toString());
							person.put("account", sms_user.getAccount().toString());
							person.put("headName", sms_user.getHead_name().toString());
							person.put("headUrl", sms_user.getHead_url().toString());
							member.add(person);
						}
					}
				}
				Sms_user sms_user = sms_userDAO.findUserByAccount(data.getString("master"));
				person = new HashMap<>();
				person.put("name", sms_user.getName().toString());
				person.put("account", sms_user.getAccount().toString());
				person.put("headName", sms_user.getHead_name().toString());
				person.put("headUrl", sms_user.getHead_url().toString());
				member.add(person);

				result.put("result", member);
				jsonUtil.setResult(result);
			} else {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The group does not exist");
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 删除组关系
	@Override
	public JSONObject deleteGroupRelation(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		String[] key = { "group_name", "master", "account" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			if (data.getString("master").equals(data.getString("account"))) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("group_name", data.getString("group_name"));
				jsonObject.put("master", data.getString("master"));
				deleteGroup(jsonObject);
			} else {
				Sms_group sms_group = sms_groupDAO.findGroupByNA(data.getString("master"),
						data.getString("group_name"));
				if (sms_group != null) {
					Sms_ug_relations sms_ug_relations = sms_ug_relationsDAO
							.findRelationByGidAcc(data.getString("account"), sms_group.getId());
					if (sms_ug_relations != null) {
						int n = sms_ug_relationsDAO.delete(sms_ug_relations);
						if (n == 1) {
							sms_group.setUser_numbers(sms_group.getUser_numbers() - 1);
							sms_groupDAO.updateByPrimaryKey(sms_group);
						}
					} else {
						jsonUtil = getJsonUtilEntity(false);
						jsonUtil.setMessage("The member does not exist");
					}
				} else {
					jsonUtil = getJsonUtilEntity(false);
					jsonUtil.setMessage("The group does not exist");
				}
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 返回用户数据
	@Override
	public JSONObject serviceUserJson(String type, String id) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> content = new HashMap<>();
		List<Object> contacts = new ArrayList<>();
		Map<String, Object> member = new HashMap<>();
		Sms_user sms_user = sms_userDAO.findUserByAccount(id);
		if (sms_user != null) {
			Sms_robot sms_robot = sms_robotDAO.findRobotByAccount(id);
			Sms_secret secret = secretDAO.findSecretByAccount(id);
			content.put("id", id);
			content.put("type", "user");
			content.put("exits", true);
			content.put("login", false);
			if (sms_user.getStatus() == 1) {
				content.put("login", true);
			}
			content.put("loginKey", secret.getSecret().toString());
			if (sms_robot != null) {
				member.put("id", sms_robot.getUid().toString());
				member.put("type", "robot");
				member.put("role", "master");
				contacts.add(member);
			}
			result.put("service", "ClientInfo");
			result.put("timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z").format(new Date()));
			result.put("content", content);
			result.put("contacts", contacts);
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("The user does not exist");
		}
		jsonUtil.setResult(result);
		return JsonUtil.toJson(jsonUtil);
	}

	// 返回机器人数据
	@Override
	public JSONObject serviceRobotJson(String type, String id) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> content = new HashMap<>();
		List<Object> contacts = new ArrayList<>();
		Map<String, Object> member = new HashMap<>();
		Sms_robot sms_robot = sms_robotDAO.findRobotByUid(id);
		if (sms_robot != null) {
			Sms_user sms_user = sms_userDAO.findUserByAccount(sms_robot.getMaster_account());
			content.put("id", id);
			content.put("type", "robot");
			content.put("exits", true);
			if (sms_user != null) {
				member.put("id", sms_user.getAccount().toString());
				member.put("type", "user");
				member.put("role", "master");
				contacts.add(member);
			}
			result.put("service", "ClientInfo");
			result.put("timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z").format(new Date()));
			result.put("content", content);
			result.put("contacts", contacts);
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("The user does not exist");
		}
		jsonUtil.setResult(result);
		return JsonUtil.toJson(jsonUtil);
	}

	//
	public JsonUtil getJsonUtilEntity(boolean flag) {
		JsonUtil jsonUtil;
		if (flag) {
			jsonUtil = new JsonUtil("200", "ok", null);
		} else {
			jsonUtil = new JsonUtil("500", "", null);
		}
		return jsonUtil;
	}

	// 管理员登录
	@Override
	public JSONObject adminLogin(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		Map<String, Object> result = new HashMap<>();
		String[] key = { "account", "password" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_admin sms_admin = sms_adminDAO.findAdminByAccount(data.getString("account"));
			if (sms_admin != null) {
				if (data.getString("password").equals(sms_admin.getPassword().toString())) {
					Sms_secret secret = secretDAO.findSecretByAccount(data.getString("account"));
					if (secret != null) {
						result.put("secret", secret.getSecret());
						jsonUtil.setResult(result);
					}
				} else {
					jsonUtil = getJsonUtilEntity(false);
					jsonUtil.setMessage("Password is not correct!");
				}
			} else {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The user does not exist!");
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 管理员绑定机器人查询
	@Override
	public JSONObject adminJudgeBindRobot(String type, String id) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);

		Sms_admin sms_admin = sms_adminDAO.findAdminByAccount(id);
		Sms_secret secret = secretDAO.findSecretByAccount(id);
		if (sms_admin != null) {
			Map<String, Object> result = new HashMap<>();
			Map<String, Object> content = new HashMap<>();
			List<Object> contacts = new ArrayList<>();
			Map<String, Object> member = new HashMap<>();

			content.put("id", id);
			content.put("type", "admin");
			content.put("exits", true);
			content.put("login", false);

			if (sms_admin.getStatus() == 1) {
				content.put("login", true);
			}

			content.put("loginKey", secret.getSecret().toString());
			member.put("id", sms_admin.getUid().toString());
			member.put("type", "robot");
			member.put("role", "master");
			contacts.add(member);

			result.put("service", "ClientInfo");
			result.put("timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z").format(new Date()));
			result.put("content", content);
			result.put("contacts", contacts);
			jsonUtil.setResult(result);

		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("The user does not exist!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 获得管理员账号
	@Override
	public JSONObject findAdminInfo(JSONObject data) {
		JsonUtil jsonUtil = getJsonUtilEntity(true);
		Map<String, Object> result = new HashMap<>();
		String[] key = { "uid" };
		if (CharacterUtil.judgeJsonFormat(key, data)) {
			Sms_admin sms_admin = sms_adminDAO.findAdminByUid(data.getString("uid"));
			if (sms_admin != null) {
				result.put("account", sms_admin.getAccount());
				result.put("password", sms_admin.getPassword());
				jsonUtil.setResult(result);
			} else {
				jsonUtil = getJsonUtilEntity(false);
				jsonUtil.setMessage("The user does not exist!");
			}
		} else {
			jsonUtil = getJsonUtilEntity(false);
			jsonUtil.setMessage("Error from json format!");
		}
		return JsonUtil.toJson(jsonUtil);
	}

}
