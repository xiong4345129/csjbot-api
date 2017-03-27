/**
 * 
 */
package com.csjbot.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csjbot.model.Frs_person;
import com.csjbot.service.FaceServiceDAO;
import com.csjbot.util.RandomUtil;
import com.csjbot.util.ResponseUtil;
import com.youtu.sign.Base64Util;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 上午9:58:05 类说明
 */
@Controller
public class FaceController {
	@Autowired
	private FaceServiceDAO faceServiceDAO;

	// 添加用户
	@RequestMapping(value = "addPerson", method = RequestMethod.POST)
	@ResponseBody
	public void addPerson(@RequestBody String data, HttpServletRequest request, HttpServletResponse response)
			throws IOException, JSONException {
		JSONObject json = JSONObject.parseObject(data);
		Map<String, Object> detMap = faceServiceDAO.faceDet(json.getString("image").toString(), 1);
		String person_id = "per" + RandomUtil.generateString();
		String name = detMap.get("name").toString();
		String age = detMap.get("age").toString();
		String sex = detMap.get("sex").toString();
		String beauty = detMap.get("beauty").toString();
		Frs_person frs_person = new Frs_person(person_id, json.getString("group_id"), name, age, sex, beauty);

		ResponseUtil.write(response, faceServiceDAO.addPerson(frs_person, json.get("image").toString()));
	}

	// 添加人脸
	@RequestMapping(value = "addFace", method = RequestMethod.POST)
	@ResponseBody
	public void addFace(@RequestBody String data, HttpServletRequest request, HttpServletResponse response)
			throws IOException, JSONException {
		JSONObject json = JSONObject.parseObject(data);
		JSONArray images = json.getJSONArray("images");
		List<String> list = new ArrayList<>();
		for (int i = 0; i < images.size(); i++) {
			list.add(images.get(i).toString());
		}
		ResponseUtil.write(response, faceServiceDAO.addFace(json.getString("person_name"), list));
	}

	// 获得所有组
	@RequestMapping(value = "getAllGroup", method = RequestMethod.GET)
	public void getAllGroup(HttpServletRequest request, HttpServletResponse response)
			throws IOException, JSONException {
		ResponseUtil.write(response, faceServiceDAO.findAllGroup());
	}

	// 删除人脸
	@RequestMapping(value = "deleteFace", method = RequestMethod.POST)
	@ResponseBody
	public void deleteFace(@RequestBody String data, HttpServletRequest request, HttpServletResponse response)
			throws IOException, JSONException {
		JSONObject json = JSONObject.parseObject(data);
		JSONArray images = json.getJSONArray("images");
		List<String> list = new ArrayList<>();
		for (int i = 0; i < images.size(); i++) {
			list.add(images.get(i).toString());
		}
		ResponseUtil.write(response, faceServiceDAO.deleteFace(json.getString("person_name"), list));
	}

	// 删除person
	@ResponseBody
	@RequestMapping(value = "deletePerson", method = RequestMethod.POST)
	public void deletePerson(@RequestBody String data, HttpServletRequest request, HttpServletResponse response)
			throws IOException, JSONException {
		JSONObject json = JSONObject.parseObject(data);
		ResponseUtil.write(response, faceServiceDAO.deletePerson(json.get("person_name").toString()));
	}

	// 查询person信息
	@RequestMapping(value = "findPerson", method = RequestMethod.POST)
	@ResponseBody
	public void findPerson(@RequestBody String data, HttpServletRequest request, HttpServletResponse response)
			throws IOException, JSONException {
		JSONObject json = JSONObject.parseObject(data);
		ResponseUtil.write(response, faceServiceDAO.findPersonByName(json.get("person_name").toString()));
	}

	// 查询所有person信息
	@RequestMapping(value = "findAllPerson", method = RequestMethod.POST)
	public void findAllPerson(@RequestBody String data, HttpServletRequest request, HttpServletResponse response)
			throws IOException, JSONException {
		JSONObject json = JSONObject.parseObject(data);
		ResponseUtil.write(response, faceServiceDAO.findAllPerson(json.get("group_id").toString()));
	}

	// 修改person的name
	@RequestMapping(value = "changePerName", method = RequestMethod.POST)
	@ResponseBody
	public void changePerName(@RequestBody String data, HttpServletRequest request, HttpServletResponse response)
			throws IOException, JSONException {
		JSONObject json = JSONObject.parseObject(data);
		ResponseUtil.write(response, faceServiceDAO.updatePersonName(json.get("person_name").toString(),json.get("new_name").toString()));
	}

	// 人脸检索
	@RequestMapping(value = "FaceIdentify", method = RequestMethod.POST)
	@ResponseBody
	public void FaceIdentify(@RequestBody String data, HttpServletRequest request, HttpServletResponse response)
			throws IOException, JSONException {
		JSONObject json = JSONObject.parseObject(data);
		ResponseUtil.write(response, faceServiceDAO.faceDect(json.get("image").toString(), json.get("group_id").toString(), 1));
	}

	/**
	 * 用来本地测试添加图片
	 */
	public String readFile(String filePath) throws IOException {
		File imageFile = new File(filePath);
		StringBuffer str = new StringBuffer("");
		if (imageFile.exists()) {
			InputStream in = new FileInputStream(imageFile);
			byte data[] = new byte[(int) imageFile.length()]; // 创建合适文件大小的数组
			in.read(data); // 读取文件中的内容到b[]数组
			in.close();
			str.append(Base64Util.encode(data));
		} else {
			throw new FileNotFoundException(filePath + " not exist");
		}
		return str.toString();
	}
}
