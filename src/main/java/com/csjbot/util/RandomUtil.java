package com.csjbot.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author 作者 :ZYY
 * @version 创建时间：2016年12月19日 下午2:21:43 类说明 随机生成25位密钥，用来保证用户登录状态以及权限的使用
 * 
 */
public class RandomUtil {

	public RandomUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	// 定义所有的字符组成的串
	public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String mathChar = "0123456789";

	// 产生随机字符串（包括数字和字母），长度可以设定
	public static String generateString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(allChar.charAt(random.nextInt(allChar.length())));
		}
		return sb.toString();
	}

	// 产生随机字符串（包括数字和字母），长度为6
	public static String generateString() {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			sb.append(allChar.charAt(random.nextInt(allChar.length())));
		}
		String time = getTimestamp();
		return "x" + time + sb.toString();
	}

	// 产生随机字符串（4位数验证码）
	public static String getCreditCode() {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			sb.append(mathChar.charAt(random.nextInt(mathChar.length())));
		}
		return sb.toString();
	}

	// 获得当前时间戳
	public static String getTimestamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(date);
		return time;
	}

	// 获得格式化时间
	public static Timestamp getTimeStampFormat() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Timestamp time = Timestamp.valueOf(sdf.format(date));
		return time;
	}

	// 获得格式化时间
	public static Timestamp getTimeStampFor() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp time = Timestamp.valueOf(sdf.format(date));
		return time;
	}
}
