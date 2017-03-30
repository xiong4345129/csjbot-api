/**
 * 
 */
package com.csjbot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.csjbot.dao.Pms_advertisementDAO;
import com.csjbot.dao.Pms_productDAO;
import com.csjbot.dao.Pms_secretKeyDAO;
import com.csjbot.dao.Sys_attachmentDAO;
import com.csjbot.dao.Ums_userDAO;
import com.csjbot.model.Pms_advertisement;
import com.csjbot.model.Pms_product;
import com.csjbot.model.Pms_secretKey;
import com.csjbot.model.Sys_attachment;
import com.csjbot.model.Ums_user;
import com.csjbot.util.FileZipUtil;
import com.csjbot.util.JsonUtil;
import com.csjbot.util.RequestCheckUtil;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月21日 上午9:49:51 类说明
 */
@Service
public class ProductSericeDAOImpl implements ProductServiceDAO {
	@Autowired
	private Pms_productDAO pms_productDAO;

	@Autowired
	private Sys_attachmentDAO sys_attachmentDAO;

	@Autowired
	private Ums_userDAO ums_userDAO;

	@Autowired
	private Pms_secretKeyDAO pms_secretKeyDAO;

	@Autowired
	private Pms_advertisementDAO pms_advertisementDAO;

	private JsonUtil sucJson = new JsonUtil("200", "ok", null);
	private JsonUtil falJson = new JsonUtil("500", "", null);

	// 登录
	@Override
	public JSONObject login(String username, String password) {
		JsonUtil jsonUtil = sucJson;
		Map<String, Object> result = new HashMap<>();
		Ums_user ums_user = ums_userDAO.findUserByName(username);
		String newPassword = new SimpleHash("md5", password, ByteSource.Util.bytes(ums_user.getSalt().toString()), 2)
				.toHex();
		if (newPassword.equals(ums_user.getPassword().toString())) {
			String login_time = "0";
			if (ums_user.getLast_login_time() != null) {
				login_time = "1";
			}
			Pms_secretKey pms_secretKey = pms_secretKeyDAO.getSecretKeyByUserId(ums_user.getId().toString());
			result.put("key", pms_secretKey.getKey().toString());
			result.put("secret", pms_secretKey.getSecret().toString());
			result.put("login_time", login_time);

			jsonUtil.setResult(result);
		} else {
			jsonUtil = falJson;
			jsonUtil.setMessage("PASSWORD ERROR");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 获得产品信息
	@Override
	public JSONObject getProductInfo() {
		JsonUtil jsonUtil = sucJson;
		Map<String, Object> result = new HashMap<>();
		List<Object> product = new ArrayList<>();
		List<String> filePath = new ArrayList<>();
		List<Pms_product> list = pms_productDAO.selectAll(); // 获得所有产品
		for (Pms_product pms_product : list) {
			List<Sys_attachment> sList = sys_attachmentDAO.getSystByProId(pms_product.getId());
			for (Sys_attachment sys_attachment : sList) {
				Map<String, Object> param = new HashMap<>();
				param.put("name", pms_product.getName().toString());
				param.put("money", pms_product.getPrice());
				param.put("imgName", sys_attachment.getAlias_name().toString());
				param.put("introduction", pms_product.getMemo().toString());
				param.put("unit", pms_product.getUnit().toString());
				filePath.add(sys_attachment.getLocation().toString());
				product.add(param);
			}

		}
		Map<String, String> zipMap = FileZipUtil.BackZipInfo(filePath);
		// 压缩文件
		result.put("zipName", zipMap.get("zipName").toString());
		result.put("zipUrl", zipMap.get("zipUrl").toString());
		// 使用阿里云存储，默认写死路径
		// result.put("zipName", "1.zip");
		// result.put("zipUrl",
		// "http://product-img256.oss-cn-shanghai.aliyuncs.com/1.zip");
		result.put("product", product);
		jsonUtil.setResult(result);
		return JsonUtil.toJson(jsonUtil);
	}

	// 获得广告信息
	@Override
	public JSONObject getADInfo() {
		JsonUtil jsonUtil = sucJson;
		Map<String, Object> result = new HashMap<>();
		List<Object> video = new ArrayList<>();
		List<Object> image = new ArrayList<>();
		List<Object> audio = new ArrayList<>();
		List<String> filePath = new ArrayList<>();
		List<Pms_advertisement> list = pms_advertisementDAO.selectAll(); // 获得所有广告
		for (Pms_advertisement pms_advertisement : list) {
			List<Sys_attachment> sList = sys_attachmentDAO.getSystByProId(pms_advertisement.getId());
			if (sList.size() > 0) {
				for (Sys_attachment sys_attachment : sList) {
					System.out.println(sys_attachment);
					if (("ADVERTISMENT_VEDIO").equals(sys_attachment.getTransaction_type())) {
						Map<String, Object> param = new HashMap<>();
						param.put("movieName", sys_attachment.getAlias_name().toString());
						filePath.add(sys_attachment.getLocation().toString());
						video.add(param);
					}
					if (("ADVERTISMENT_AUDIO").equals(sys_attachment.getTransaction_type())) {
						Map<String, Object> param = new HashMap<>();
						param.put("audioName", sys_attachment.getAlias_name().toString());
						filePath.add(sys_attachment.getLocation().toString());
						audio.add(param);
					}
					if (("ADVERTISMENT_PIC").equals(sys_attachment.getTransaction_type())) {
						Map<String, Object> param = new HashMap<>();
						param.put("imgName", sys_attachment.getAlias_name().toString());
						filePath.add(sys_attachment.getLocation().toString());
						image.add(param);
					}
				}
			}

		}
		 Map<String, String> zipMap = FileZipUtil.BackZipInfo(filePath);
		// 压缩文件
		 result.put("zipName", zipMap.get("zipName").toString());
		 result.put("zipUrl", zipMap.get("zipUrl").toString());
		// 使用阿里云存储，默认写死路径
		//result.put("zipName", "1.zip");
		//result.put("zipUrl", "http://product-img256.oss-cn-shanghai.aliyuncs.com/1.zip");
		result.put("ADVERTISMENT_VIDEO", video);
		result.put("ADVERTISMENT_PIC", image);
		result.put("ADVERTISMENT_AUDIO", audio);
		jsonUtil.setResult(result);
		return JsonUtil.toJson(jsonUtil);
	}

	// 下载文件
	@Override
	public JSONObject downFile(String fileName) {
		JsonUtil jsonUtil = sucJson;
		Map<String, Object> result = new HashMap<>();
		Sys_attachment sys_attachment = sys_attachmentDAO.getSystByName(fileName);
		if (sys_attachment != null) {
			result.put("fileName", sys_attachment.getAlias_name().toString());
			result.put("fileUrl", sys_attachment.getLocation().toString());
			jsonUtil.setResult(result);
		} else {
			jsonUtil = falJson;
			jsonUtil.setMessage("FILENAME ERROR");
		}
		return JsonUtil.toJson(jsonUtil);
	}

	// 验证http头
	@Override
	public boolean judegHttpHeader(String key, String time, String sign) {
		boolean flag = false;
		Pms_secretKey pms_secretKey = pms_secretKeyDAO.getSecretKeyByKey(key);
		if (pms_secretKey != null) {
			flag = RequestCheckUtil.judgeKeySecret(key, pms_secretKey.getSecret().toString(), time, sign);
		}
		return flag;
	}

}
