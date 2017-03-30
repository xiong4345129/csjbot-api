/**
 * 
 */
package com.csjbot.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.csjbot.dao.Frs_faceDAO;
import com.csjbot.dao.Frs_personDAO;
import com.csjbot.model.Frs_face;
import com.csjbot.model.Frs_person;
import com.csjbot.util.FileZipUtil;
import com.csjbot.util.JsonUtil;
import com.csjbot.youtu.Youtu;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 上午9:57:50 类说明
 */
@Service
public class FaceServiceDAOImpl implements FaceServiceDAO {

	private static final String APP_ID = "1252828273";
	private static final String SECRET_ID = "AKIDUvAE6lSVZbUHtMeiaIoESTIxRp9KAXfb";
	private static final String SECRET_KEY = "PhtthIhuuiMqBxGRQCl7KL9k8KQhrHWZ";
	private static final String USER_ID = "0";
	private static final String BUCKET = "csjrobot";
	private static final String PATH = "/opt/pkg/zip/";

	@Autowired
	private Frs_personDAO frs_personDAO;

	@Autowired
	private Frs_faceDAO frs_faceDAO;

	//@Autowired
	//private Frs_groupDAO frs_groupDAO;

	/**
	 * 先请求优图服务，解析返回码，添加成功后在进行数据库操作
	 */
	// 添加个体
	@Override
	public JSONObject addPerson(Frs_person frs_person, String image) {
		Map<String, Object> dat = new HashMap<>();
		JSONObject json = new JSONObject();
		Youtu youtu = getYoutu();
		// 判断用户名是否重复（不能重名，因为有接口根据名字来查询用户信息）
		Frs_person frs_person2 = frs_personDAO.findPersonByName(frs_person.getName().toString());
		if (frs_person2 == null) {
			// 准备优图个体数据
			List<String> group_ids = new ArrayList<>();
			group_ids.add(frs_person.getGroup_id().toString());
			try {
				json = youtu.NewPerson(image, frs_person.getPerson_id(), group_ids, frs_person.getName());
				System.out.println(json);
				if ("OK".equals(json.get("message").toString())) { // 判断返回数据message参数
					// 图片保存本地，返回图片名和路径
					JSONObject data = (JSONObject) (json.get("data")); // 将返回数据中的data字符串转换为json对象
					
					Map<String, Object> picMap = FileZipUtil.saveFileFromFaceReg(PATH, image);
					
					Frs_face frs_face = new Frs_face(data.get("face_id").toString(), data.get("person_id").toString(),
							frs_person.getGroup_id(), picMap.get("picName").toString(),
							picMap.get("picUrl").toString());

					int n = frs_faceDAO.insert(frs_face);
					n += frs_personDAO.insert(frs_person);
					if (n == 2) {
						dat.put("status", "200");
						dat.put("message", "ok");
					} else {
						dat.put("status", "500");
						dat.put("message", "Error from dataBase operations!");
					}
				} else {
					dat.put("status", "500");
					dat.put("message", "Error from youtu!");
				}
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			dat.put("status", "500");
			dat.put("message", "User name already exists！");
		}
		return JsonUtil.toJson(dat);
	}

	// 删除个体
	@Override
	public JSONObject deletePerson(String name) {
		Map<String, Object> dat = new HashMap<>();
		JSONObject json = new JSONObject();
		Frs_person frs_person = frs_personDAO.findPersonByName(name);
		if (frs_person != null) {
			Youtu youtu = getYoutu();
			try {
				json = youtu.DelPerson(frs_person.getPerson_id().toString());
				if ("OK".equals(json.get("message").toString())) { // 判断返回数据message参数
					List<Frs_face> list = frs_faceDAO.findFaceByPerId(frs_person.getPerson_id()); // 获得数据库中该用户的所有人脸
					int n = 0; // 定义标志
					for (Frs_face frs_face : list) {
						n += frs_faceDAO.delete(frs_face); // 循环删除数据库中该用户的人脸数据
					}
					int m = frs_personDAO.delete(frs_person); // 删除数据库中该用户的信息
					if (m != 1 || n != list.size()) { // 删除失败
						dat.put("status", "500");
						dat.put("message", "Error from dataBase operations!");
					} else {
						dat.put("status", "200");
						dat.put("message", "ok");
					}
				} else {
					dat.put("status", "500");
					dat.put("message", "Error from youtu!");
				}
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} // 向优图中删除个体数据
		} else {
			dat.put("status", "500");
			dat.put("message", "The user does not exist！");
		}
		return JsonUtil.toJson(dat);
	}

	// 根据人名查询个体(直接从数据库中查询)
	@Override
	public JSONObject findPersonByName(String name) {
		Map<String, Object> dat = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		Frs_person frs_person = frs_personDAO.findPersonByName(name);
		if (frs_person != null) {
			result.put("name", frs_person.getName().toString());
			result.put("age", frs_person.getAge().toString());
			result.put("sex", frs_person.getSex().toString());
			result.put("beauty", frs_person.getBeauty().toString());
			dat.put("status", "200");
			dat.put("result", result);
			dat.put("message", "ok");
		} else {
			dat.put("status", "500");
			dat.put("result", result);
			dat.put("message", "The user does not exist！");
		}
		return JsonUtil.toJson(dat);
	}

	// 修改姓名
	@Override
	public JSONObject updatePersonName(String person_name, String new_name) {
		Map<String, Object> dat = new HashMap<>();
		Frs_person frs_person = frs_personDAO.findPersonByName(person_name);
		if (frs_person != null) {
			frs_person.setName(new_name);
			int n = frs_personDAO.updateByPrimaryKey(frs_person);
			if (n == 1) {
				dat.put("status", "200");
				dat.put("message", "ok");
			} else {
				dat.put("status", "500");
				dat.put("message", "Error from dataBase operations!");
			}

		} else {
			dat.put("status", "500");
			dat.put("message", "The user does not exist！");
		}
		return JsonUtil.toJson(dat);
	}

	// 查询一个组的所有人信息
	@Override
	public JSONObject findAllPerson(String group_id) {
		Map<String, Object> dat = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		List<Object> per = new ArrayList<>();
		List<Frs_person> frs_persons = frs_personDAO.findPersonByGroupId(group_id);
		if (frs_persons != null) {
			for (Frs_person frs_person : frs_persons) {
				JSONObject person = new JSONObject();
				person.put("name", frs_person.getName().toString());
				person.put("age", frs_person.getAge().toString());
				person.put("sex", frs_person.getSex().toString());
				person.put("beauty", frs_person.getBeauty().toString());
				per.add(person);
			}
			result.put("persons", per);
			dat.put("status", "200");
			dat.put("result", result);
			dat.put("message", "ok");
		}
		return JsonUtil.toJson(dat);
	}

	// 添加人脸
	@Override
	public JSONObject addFace(String person_name, List<String> image) {
		Map<String, Object> dat = new HashMap<>();
		JSONObject json = new JSONObject();
		Frs_person frs_person = frs_personDAO.findPersonByName(person_name);
		if (frs_person != null) {
			Youtu youtu = getYoutu();
			String[] picName = new String[image.size()];
			String[] picUrl = new String[image.size()];
			int i_ = 0;
			for (String string : image) {
				
				Map<String, Object> picMap = FileZipUtil.saveFileFromFaceReg(PATH, string);
				picName[i_] = picMap.get("picName").toString();
				picUrl[i_] = picMap.get("picUrl").toString();
				i_ ++;
			}
			// 图片保存本地并返回图片名称和地址字符串数组
			try {
				json = youtu.AddFace(frs_person.getPerson_id().toString(), image);
				if ("OK".equals(json.get("message").toString())) { // 优图添加成功，进行数据操作
					JSONObject data = (JSONObject) (json.get("data")); // 将返回数据中data字符串转换为json对象
					String face_ids = data.get("face_ids").toString(); // 从json中获得face_ids字符串
					face_ids = face_ids.substring(1, face_ids.length() - 1); // 以下是对face_ids字符串截取操作
					String[] face_id = face_ids.split(","); // 目的是获得face_id字符串数组
					if (face_id.length == image.size()) {
						for (int i = 0; i < face_id.length; i++) {
							face_id[i] = face_id[i].replace("\"", "");
							Frs_face frs_face = new Frs_face(face_id[i], frs_person.getPerson_id().toString(),
									frs_person.getGroup_id().toString(), picName[i], picUrl[i]);
							frs_faceDAO.insert(frs_face); // 数据库插入人脸
							dat.put("status", "200");
							dat.put("message", "ok");
						}
					} else {
						dat.put("status", "500");
						dat.put("message", "Error from dataBase operations!");
					}
				} else {
					dat.put("status", "500");
					dat.put("message", "Error from youtu!");
				}
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			dat.put("status", "500");
			dat.put("message", "The user does not exist！");
		}
		return JsonUtil.toJson(dat);
	}

	// 删除人脸
	@Override
	public JSONObject deleteFace(String person_name, List<String> face_id) {
		Map<String, Object> dat = new HashMap<>();
		JSONObject json = new JSONObject();
		Frs_person frs_person = frs_personDAO.findPersonByName(person_name);
		if (frs_person != null) {
			Youtu youtu = getYoutu();
			try {
				json = youtu.DelFace(frs_person.getPerson_id().toString(), face_id);
				if ("OK".equals(json.get("message").toString())) { // 优图添加成功，进行数据操作
					for (String string : face_id) {
						Frs_face frs_face = frs_faceDAO.findFaceByFaceId(string);
						frs_faceDAO.delete(frs_face);
						dat.put("status", "200");
						dat.put("message", "ok");
					}
				} else {
					dat.put("status", "500");
					dat.put("message", "Error from dataBase operations!");
				}
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			dat.put("status", "500");
			dat.put("message", "The user does not exist！");
		}
		return JsonUtil.toJson(dat);
	}

	// 根据人名查询所有人脸
	@Override
	public JSONObject findFaceAll(String person_name) {
		Map<String, Object> dat = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		List<Object> fac = new ArrayList<>();
		Frs_person frs_person = frs_personDAO.findPersonByName(person_name);
		if (frs_person != null) {
			List<Frs_face> list = frs_faceDAO.findFaceByPerId(frs_person.getPerson_id());
			JSONObject face = new JSONObject();
			for (Frs_face frs_face : list) {
				face.put("face_id", frs_face.getFace_id().toString());
				face.put("person_id", frs_face.getPerson_id().toString());
				face.put("group_id", frs_face.getGroup_id().toString());
				face.put("picName", frs_face.getPic_name().toString());
				face.put("picUrl", frs_face.getPic_url().toString());
				fac.add(face);
			}
			result.put("faces", fac);
			dat.put("status", "200");
			dat.put("message", "ok");
			dat.put("result", result);
		} else {
			dat.put("status", "500");
			dat.put("message", "The user does not exist！");
		}
		return JsonUtil.toJson(dat);
	}

	// 根据人脸标识查询人脸信息
	@Override
	public JSONObject findFaceByFaceId(String face_id) {
		Map<String, Object> dat = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		Frs_face frs_face = frs_faceDAO.findFaceByFaceId(face_id);
		result.put("face_id", frs_face.getFace_id().toString());
		result.put("person_id", frs_face.getPerson_id().toString());
		result.put("group_id", frs_face.getGroup_id().toString());
		result.put("picName", frs_face.getPic_name().toString());
		result.put("picUrl", frs_face.getPic_url().toString());
		dat.put("status", "200");
		dat.put("message", "ok");
		dat.put("result", result);
		return JsonUtil.toJson(dat);
	}

	// 查询所有组信息
	@Override
	public JSONObject findAllGroup() {
		JSONObject json = new JSONObject();
		Youtu youtu = getYoutu();
		try {
			json = youtu.GetGroupIds();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 人脸检测&检索
	 * 客户端向后台传人脸图片数据，组标识，后台分别向优图和数据库检测和查询
	 * 
	 */
	// 人脸识别
	@Override
	public JSONObject faceDect(String image, String group_id, int mode) {
		Map<String, Object> dat = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> detMap = faceDet(image, mode);
		Map<String, Object> idenMap = faceIden(image, group_id);
		dat.put("status", "200");
		dat.put("message", "ok");
		result.put("age", detMap.get("age"));
		result.put("sex", detMap.get("sex"));
		result.put("beauty", detMap.get("beauty"));
		result.put("name", "");
		if (idenMap.get("name") != null) {
			result.put("name", idenMap.get("name").toString());
		}
		dat.put("result", result);
		return JsonUtil.toJson(dat);
	}

	// 获得youtu
	public Youtu getYoutu() {
		Youtu youtu = new Youtu(APP_ID, BUCKET, SECRET_ID, SECRET_KEY, Youtu.API_YOUTU_END_POINT, USER_ID);
		return youtu;
	}

	//人脸检测
	@Override
	public Map<String, Object> faceDet(String image, int mode) {
		Map<String, Object> map = new HashMap<>();
		Youtu youtu = getYoutu();
		try {
			JSONObject json = youtu.DetectFace(image, mode);
			JSONObject data = json.getJSONObject("data");
			JSONArray face = data.getJSONArray("face");
			JSONObject mem = face.getJSONObject(0);
			
			map.put("age", mem.get("age"));
			map.put("sex", mem.get("gender"));
			map.put("beauty", mem.get("beauty"));
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	//人脸检索(返回用户姓名)
	@Override
	public Map<String, Object> faceIden(String image, String group_id) {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = new JSONObject();
		Youtu youtu = getYoutu();
		try {
			json = youtu.FaceIdentify(image, group_id);
			if ("0".equals(json.get("code").toString())) { // 搜索成功
				JSONObject jsonObject = JSONObject.parseObject(json.get("data").toString());
				JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("candidates").toString());// 获得候选人字符串并转换为json数组
				JSONObject job = jsonArray.getJSONObject(0);
				Frs_person frs_person = frs_personDAO.findPersonByperId(job.get("person_id").toString());
				if (frs_person != null) {
					map.put("name", frs_person.getName().toString());
				}
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
 		return map;
	}

}
