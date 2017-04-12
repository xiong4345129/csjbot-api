
package com.csjbot.api.product.service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.csjbot.api.common.util.FileZipUtil;
import com.csjbot.api.common.util.JsonUtil;
import com.csjbot.api.common.util.RandomUtil;
import com.csjbot.api.pay.util.OrderIdGen;
import com.csjbot.api.product.dao.*;
import com.csjbot.api.product.model.*;
import com.csjbot.api.product.util.RequestCheckUtil;
import com.csjbot.api.product.zfb.AlipayServicePort;
import com.csjbot.api.robot.dao.Sys_attachmentDAO;
import com.csjbot.api.robot.dao.Ums_userDAO;
import com.csjbot.api.robot.model.Sys_attachment;
import com.csjbot.api.robot.model.Ums_user;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


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

	@Autowired
	private Pms_order_detailDAO pms_order_detailDAO;

	@Autowired
	private Pms_order_payDAO pms_order_payDAO;

	@Autowired
	private Pms_pay_detail_alipayDAO pay_detail_alipayDAO;

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
				param.put("product_id", pms_product.getId().toString());
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
		// result.put("zipName", "1.zip");
		// result.put("zipUrl",
		// "http://product-img256.oss-cn-shanghai.aliyuncs.com/1.zip");
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

	// 生成订单信息
	@Override
	public JSONObject addOrder(JSONObject json) throws ParseException {
		JSONObject backJson = new JSONObject();
		JSONObject backData = new JSONObject();

		String order_id = OrderIdGen.next(); // 订单号
		Double price = 0.00;
		String subject = "";

		JSONObject data = json.getJSONObject("data");
		Pms_order_pay pms_order_pay = new Pms_order_pay();
		pms_order_pay.setOrder_id(order_id);
		pms_order_pay.setCreate_time(RandomUtil.getTimeStampFor());
		pms_order_pay.setUpdate_time(RandomUtil.getTimeStampFor());
		pms_order_pay.setOrder_time(RandomUtil.getTimeStampByStr(data.getString("orderTime")));
		pms_order_pay.setOrder_status("wait");
		pms_order_pay.setPay_service("alipay");
		pms_order_pay.setPay_status("doing");
		pms_order_pay.setOrder_device_group(data.getString("robotModel"));
		pms_order_pay.setOrder_device_id(data.getString("robotUid"));
		pms_order_pay.setOrder_pseudo_no(data.getString("orderPseudoNo"));

		int n = pms_order_payDAO.insert(pms_order_pay);
		if (n == 1) {
			Pms_order_item pms_order_detail;
			JSONArray jsonArray = data.getJSONArray("orderList");
			for (Object object : jsonArray) {
				JSONObject jsonObject = (JSONObject) JSONObject.toJSON(object);
				pms_order_detail = new Pms_order_item();
				pms_order_detail.setOrder_id(order_id);
				pms_order_detail.setItem_id(jsonObject.getString("objectId"));
				pms_order_detail.setItem_qty(jsonObject.getInteger("qty"));
				Pms_product pms_product = pms_productDAO.selectByPrimaryKey(jsonObject.getString("objectId"));
				if (pms_product != null) {
					System.out.println(pms_product.getPrice());
					price = pms_product.getPrice() * 100;

					subject = pms_product.getName();

					pms_order_detail.setUnit_price(price);
				}
				pms_order_detailDAO.insert(pms_order_detail);
			}
			// 获取订单支付链接
			String orderUrl = "";
			try {
				JSONObject param = new JSONObject();
				param.put("out_trade_no", order_id);
				param.put("total_amount", (double)price/100);
				param.put("subject", subject);
				param.put("body", "");
				orderUrl = AlipayServicePort.addOrder(param);

			} catch (AlipayApiException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			backJson.put("id", "");
			backJson.put("status", "SUCCESS");
			backData.put("orderPseudoNo", data.getString("orderPseudoNo"));
			backData.put("orderId", order_id);
			backData.put("codeUrl", orderUrl);
			backData.put("code", 200);
			backJson.put("data", backData);

		} else {
			backJson.put("id", "");
			backJson.put("status", "SUCCESS");
			backData.put("orderPseudoNo", "");
			backData.put("codeUrl", "");
			backData.put("code", 404);
			backJson.put("error", backData);

		}
		return backJson;
	}

	// 根据order_id查询订单信息
	@Override
	public JSONObject showOrderInfo(String order_id) {
		JSONObject data = new JSONObject();
		JSONObject result = new JSONObject();
		JSONObject param = new JSONObject();
		param.put("out_trade_no", order_id);
		param.put("trade_no", "");
		try {
			result = AlipayServicePort.showOrderInfo(param);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		Pms_order_pay pay = pms_order_payDAO.findPayByOrderId(order_id);
		if (pay != null) {
			JSONObject alipay_trade_query_response = result.getJSONObject("alipay_trade_query_response");
			String status = pay.getOrder_status();
			if (alipay_trade_query_response != null) {
				status = alipay_trade_query_response.getString("trade_status");
				pay.setOrder_status(status);
				pms_order_payDAO.updateByPrimaryKey(pay);

				Pms_pay_detail_alipay ppdy = pay_detail_alipayDAO.findPayByTradeNo(order_id);
				if (ppdy == null) {
					ppdy = new Pms_pay_detail_alipay();
					ppdy.setBuyer_logon_id(alipay_trade_query_response.getString("buyer_logon_id"));
					ppdy.setBuyer_pay_amount(alipay_trade_query_response.getString("buyer_pay_amount"));
					ppdy.setBuyer_user_id(alipay_trade_query_response.getString("buyer_user_id"));
					ppdy.setCode(alipay_trade_query_response.getString("code"));
					ppdy.setInvoice_amount(alipay_trade_query_response.getString("invoice_amount"));
					ppdy.setMsg(alipay_trade_query_response.getString("msg"));
					ppdy.setOpen_id(alipay_trade_query_response.getString("open_id"));
					ppdy.setOut_trade_no(alipay_trade_query_response.getString("out_trade_no"));
					ppdy.setPoint_amount(alipay_trade_query_response.getString("point_amount"));
					ppdy.setReceipt_amount(alipay_trade_query_response.getString("receipt_amount"));
					ppdy.setSend_pay_date(alipay_trade_query_response.getString("send_pay_date"));
					ppdy.setSign(result.getString("sign"));
					ppdy.setTotal_amount(alipay_trade_query_response.getString("total_amount"));
					ppdy.setTrade_no(alipay_trade_query_response.getString("trade_no"));
					ppdy.setTrade_status(alipay_trade_query_response.getString("trade_status"));
					pay_detail_alipayDAO.insert(ppdy);

				} else {
					ppdy.setMsg(alipay_trade_query_response.getString("msg"));
					pay_detail_alipayDAO.updateByPrimaryKey(ppdy);
				}
				data.put("orderId", order_id);
				data.put("orderTime", pay.getOrder_time().toString());
				data.put("payTimeEnd", alipay_trade_query_response.getString("send_pay_date"));
				data.put("status", alipay_trade_query_response.getString("msg"));
				data.put("remark", "");
			}

		} else {
			data.put("orderId", order_id);
			data.put("orderTime", "");
			data.put("payTimeEnd", "");
			data.put("status", "");
			data.put("remark", "");
		}
		return data;
	}

}
