/**
 * 
 */
package com.csjbot.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月17日 下午3:08:47
 * 类说明:字符校验&随机字符生成
 */
public class CharacterUtil {
	/**
	 * 功能：校验json数据是否完整
	 * @param key: 参数名数组
	 * @param json: json数据
	 */
	public static boolean judgeJsonFormat(String[] key,JSONObject json){
		boolean flag = true;
		for (String string : key) {
			if (json.get(string) == null) {
				flag = false;
			}
		}
		return flag;
	}
	
}
