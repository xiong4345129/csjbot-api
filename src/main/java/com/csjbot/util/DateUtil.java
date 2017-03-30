/**
 * 
 */
package com.csjbot.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月21日 上午11:48:53
 * 类说明
 */
public class DateUtil {
	/**
	 * 获得时间戳
	 */
	public static Timestamp getTimestramp(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Timestamp time = Timestamp.valueOf(dateFormat.format(new Date()));
		return time;
	}
	/**
	 * 获得格式化时间
	 */
	public static Timestamp getFormatTime(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp time = Timestamp.valueOf(dateFormat.format(new Date()));
		return time;
	}

}
