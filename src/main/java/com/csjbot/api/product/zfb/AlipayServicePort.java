/**
 * 
 */
package com.csjbot.api.product.zfb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年4月6日 上午10:33:46 类说明
 */
public class AlipayServicePort {
	// 商户appid
	public static String APPID = "2017032706428521";
	// 私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCBMUSiJhB7F27hccrNmc9u6IHh08sTsiAlFSLwejBxNr6nlEUKafuW46oe3td7D4HY7ATFlQJEbrXyqWaBp3HtbLfRDHTWbO/QLgIp2bVf5AFeinO8K7IhDGNb6GBtceIkGlPF3oJejh5HPce3RqMCKFg0O40T/J6IS62IgEXI+ZgD74VX+emlK73n1SnBdghYMA/XlkIBeEdnFa7UVwpnIM6lu60PlEFQD6LoT3BpW/duNf8tAVXvMLGgAvg+VbF9ZayHU8HyrkDKrnPm2fI5lB/81We3ftz/6oQ/VRqxk/UAzx/XN/2Oiudlg6XeGhYoKmhgLM1bgdHLG0Afa+e5AgMBAAECggEAKt+jzD/flknnhS+TQvn5SfAlMkFsEmQ5oLTL/IxASUIGsWxWISec061IBqijT/aMe+WPi9Zwgy9OeHJZT0u3VjiPI/QDTimmyCqMk0DpnSH4hlxiHh9OJQOMcbLDVW7Gwn5uMKswDpsRV0kcXyk07YFgDC17GxfpsIeU1kjjX9dgIuKMM76v75u0va2U8wS3pctjQBae9Jn/x9aq0iRsvk65qF6NZF3auUVWWS6CpEBq36BeMRyUFLMJ7p4Z7dHBfNHQPqBqEKnF4hWFVBPtbxBexDA8ci+vf5vQI1CgMJa379iOmpaPx/u+dSi8Ayt51DO5c8K0N9V+ct3JRe+6gQKBgQDrlZl0mNv3NarfZbfdM0SXOtw5Bch1pvVT/S1t87bBJY/smnM3I0N8fJWV+323D7U0OOX5n+ejDrpzS3x190M7ltiPcWH4etaL5sfYAaYJ8LgYtbeNbZJWvJ2/6YXSZGlVtKAIUMLSKwojRx8mOU3lJSnsV31VzQGgO8TuBR7RUQKBgQCMY2Gljzw5kuzuc/ZatV6rLFWsweQ4iPiJAkXqotbMIx8btRiIiJFsi3CLVffZ7CL0CEx7GVy6+MRSlGqJDvcRDfq4oTgJsx8c3LRF1UAhjIgC8IscuqlmbnuU8e07mS62VoBBzcAn7veh/CZD35wev1lzgZqA9H8zl4zjSQfV6QKBgBnmT4GfEIfEEa4Pu8AI0+Cgavel6BzvJwd9zRHKemwPmhwT+tiz+sZj1/ouNOr8Qhqkqrvf1ZJ6mwrz4JIDhhO2CMb0WyX7Mz0cORpJTBzJpyOoTq6wviblA57GqJyxx1bRzco8Pr7mVm38crM8F5E6nbpLAA9/w07/Yy4byACRAoGAY2e3a6h5h3bep5Wa2e3U47WX0KTV+aYa/cCLSzIUkCVKUK8xePPirQ4rGa48uqT2tv7tULjLrWSadrK6oOPk08GnR3Z4nH2lySUJzOzL5TWB1TsH9cbCTlJq1TNcvLehmOC1HmMW7e/KjnthiOvbuvXY2+3oPelh7GEiFInQr4kCgYEAnuvm2RJiIKjubz3Lgdn0L+hcqWQNhk8GCbOfSYi4WClr53OyHpR/XoIwuAAMAbXiw5Ba9OPwGV60MgVLOPYidgAymIO8ktiDNaSL50Wo5rxns8v1LuQBRfi7LPVIqBPPVUipFS0Vyx0+i5ETJYvVHu6IB9l8rJuBvjIsYuymc7s=";
	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://120.27.233.57:8080/alipay.trade.wap.pay-java-utf-8/notify_url.jsp";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
	public static String return_url = "http://120.27.233.57:8080/alipay.trade.wap.pay-java-utf-8/return_url.jsp";
	// 请求网关地址
	public static String URL = "https://openapi.alipay.com/gateway.do";
	// 编码
	public static String CHARSET = "UTF-8";
	// 返回格式
	public static String FORMAT = "json";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh0+VmPcma2gnZCPGNAXOcWg6dTqs1LZf4M/Dx+D1zi2ZVEAGdVJ5XTIi6FGte32B8t16lReXGPKTR0oEZu0a5X9jD+GTUIH2t2/IFU6RYeyh9F/9yPuXqV762fnykdEO3H+Y/vy3CCQiehAlolSARdnISiedWVO47G5vcXKoYyc/kJmJB5BjNkDf66VBqxdePjxTy6eK6uDQ020HTcn9bkEcmcnpzi/oGkW5DQNXM54EyWl9R8k+R3J68Z0gON3h+8xNvCL2En45I08rMGXguaFHWFF4Eshgnd/Be/uvytbbsDpk0z3Ccns723adTRgfvQcyrcvqgW4CU70JEw20hwIDAQAB";
	// 日志记录目录
	public static String log_path = "/log";
	// RSA2
	public static String SIGNTYPE = "RSA2";



	// 订单生成
	public static String addOrder(JSONObject json) throws AlipayApiException, UnsupportedEncodingException {
		String out_trade_no = json.getString("out_trade_no");
		// 订单名称，必填
		String subject = json.getString("subject");
		// 付款金额，必填
		String total_amount = json.getString("total_amount");
		// 商品描述，可空
		String body = json.getString("body");
		// 超时时间 可空
		String timeout_express = "50m";
		// 销售产品码 必填
		String product_code = "QUICK_WAP_PAY";
		/**********************/
		// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
		// 调用RSA签名方式
		AlipayClient client = new DefaultAlipayClient(URL, APPID,
				RSA_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY,
				SIGNTYPE);

		AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

		// 封装请求支付信息
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setOutTradeNo(out_trade_no);
		model.setSubject(subject);
		model.setTotalAmount(total_amount);
		model.setBody(body);
		model.setTimeoutExpress(timeout_express);
		model.setProductCode(product_code);
		alipay_request.setBizModel(model);
		// 设置异步通知地址
		alipay_request.setNotifyUrl(notify_url);
		// 设置同步地址
		alipay_request.setReturnUrl(return_url);
		AlipayTradeWapPayResponse response = null;
		try {
			response = client.sdkExecute(alipay_request);// 通过alipayClient调用API，获得对应的response类
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}

 		return URL + "?" + response.getBody();
	}

	// 查询订单
	public static JSONObject showOrderInfo(JSONObject json) throws AlipayApiException {
		JSONObject result = new JSONObject();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APPID,
				RSA_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2"); // 获得初始化的AlipayClient
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();// 创建API对应的request类
		request.setBizContent(json.toString());// 设置业务参数
		AlipayTradeQueryResponse response = alipayClient.execute(request);// 通过alipayClient调用API，获得对应的response类
		result = JSONObject.parseObject(response.getBody());
		// 根据response中的结果继续业务逻辑处理
		return result;
	}

	// 查询订单下载地址
	public static String showOrderDownUrl(JSONObject bill_json) throws AlipayApiException {
		String result = "";
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APPID,
				RSA_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2"); // 获得初始化的AlipayClient
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();// 创建API对应的request类
		request.setBizContent(bill_json.toString());// 设置业务参数
		AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
		JSONObject json = JSONObject.parseObject(response.getBody());
		result = json.getJSONObject("alipay_data_dataservice_bill_downloadurl_query_response")
				.getString("bill_download_url");
		return result;

	}

	// 下载账单
	public static String downOrder(String time) throws AlipayApiException {
		JSONObject json = new JSONObject();
		json.put("bill_type", "trade");
		json.put("bill_date", time);
		String filePath = "D://20170406.zip";
		String fileUrl = showOrderDownUrl(json);
		if (fileUrl != null && fileUrl != "") {
			URL url = null;
			HttpURLConnection httpUrlConnection = null;
			InputStream fis = null;
			FileOutputStream fos = null;
			try {
				url = new URL(fileUrl);
				httpUrlConnection = (HttpURLConnection) url.openConnection();
				httpUrlConnection.setConnectTimeout(5 * 1000);
				httpUrlConnection.setDoInput(true);
				httpUrlConnection.setDoOutput(true);
				httpUrlConnection.setUseCaches(false);
				httpUrlConnection.setRequestMethod("GET");
				httpUrlConnection.setRequestProperty("Charsert", "UTF-8");
				httpUrlConnection.connect();
				fis = httpUrlConnection.getInputStream();
				byte[] temp = new byte[1024];
				int b;
				fos = new FileOutputStream(new File(filePath));
				while ((b = fis.read(temp)) != -1) {
					fos.write(temp, 0, b);
					fos.flush();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null)
						fis.close();
					if (fos != null)
						fos.close();
					if (httpUrlConnection != null)
						httpUrlConnection.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return filePath;
	}

	// 交易关闭接口
	public static JSONObject dealClose(JSONObject json) throws AlipayApiException {
		JSONObject result = new JSONObject();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "app_id",
				"your private_key", "json", "GBK", "alipay_public_key", "RSA2");
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizContent("{" + "    \"trade_no\":\"2013112611001004680073956707\","
				+ "    \"out_trade_no\":\"HZ0120131127001\"," + "    \"operator_id\":\"YX01\"" + "  }");
		AlipayTradeCloseResponse response = alipayClient.execute(request);
		if (response.isSuccess()) {
			System.out.println("调用成功");
		} else {
			System.out.println("调用失败");
		}
		return result;
	}

	// 退款接口
	public static JSONObject refund(JSONObject json) throws AlipayApiException {
		JSONObject result = new JSONObject();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent("{" +
		"    \"out_trade_no\":\"20150320010101001\"," +
		"    \"trade_no\":\"2014112611001004680073956707\"," +
		"    \"refund_amount\":200.12," +
		"    \"refund_reason\":\"正常退款\"," +
		"    \"out_request_no\":\"HZ01RF001\"," +
		"    \"operator_id\":\"OP001\"," +
		"    \"store_id\":\"NJ_S_001\"," +
		"    \"terminal_id\":\"NJ_T_001\"" +
		"  }");
		AlipayTradeRefundResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
		System.out.println("调用成功");
		} else {
		System.out.println("调用失败");
		}
		return result;
	}

	// 查询退款接口
	public static JSONObject showRefund(JSONObject json) throws AlipayApiException {
		JSONObject result = new JSONObject();
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizContent("{" +
		"    \"trade_no\":\"20150320010101001\"," +
		"    \"out_trade_no\":\"2014112611001004680073956707\"," +
		"    \"out_request_no\":\"2014112611001004680073956707\"" +
		"  }");
		AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
		System.out.println("调用成功");
		} else {
		System.out.println("调用失败");
		}
		return result;
	}

	public static void main(String[] args) throws UnsupportedEncodingException, AlipayApiException {

		
	}

}
