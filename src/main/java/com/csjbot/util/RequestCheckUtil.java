/**
 * 
 */
package com.csjbot.util;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月17日 下午1:47:31 类说明：请求鉴权
 */
public class RequestCheckUtil {
	/**
	 *签名比较，正确返回true 
	 */
	public static boolean judgeKeySecret(String key, String secret, String time, String sign) {
		boolean flag = false;
		String newSign = MD5.stringToMD5(secret + time).toUpperCase();  //MD5加密并转大写
		if (newSign.equals(sign)) {
			flag = true;
		}
		return flag;
	}
public static void main(String[] args) {
	System.out.println(MD5.stringToMD5("ybSD2Dxmhxk3IPCkhKAK"+"20170330174452").toUpperCase());
}
}
