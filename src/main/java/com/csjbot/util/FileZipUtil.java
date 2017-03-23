/**
 * 
 */
package com.csjbot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月21日 上午11:18:28 类说明
 */
public class FileZipUtil {

	/**
	 * 功能:压缩多个文件成一个zip文件
	 * 
	 * @param srcfile：源文件列表
	 * @param zipfile：压缩后的文件
	 */
	public static void zipFiles(List<File> srcfile, File zipfile) {
		byte[] buf = new byte[1024];
		try {
			// ZipOutputStream类：完成文件或文件夹的压缩
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
			for (int i = 0; i < srcfile.size(); i++) {
				FileInputStream in = new FileInputStream(srcfile.get(i));
				out.putNextEntry(new ZipEntry(srcfile.get(i).getName()));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
			System.out.println("压缩完成.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 功能:调用压缩方法，返回压缩信息
	 * 
	 * @param Fileurls:需要压缩文件的地址数组
	 */
	public static Map<String, String> BackZipInfo(List<String> Fileurls) {
		List<File> srcfiles = new ArrayList<File>();
		for (String string : Fileurls) {
			File file = new File(getPath()+string);
			srcfiles.add(file);
		}
		Map<String, String> map = new HashMap<>();
		String zipName = RandomUtil.generateString(4);
		String zipUrl = getPath()+"image"+ zipName + ".zip";
		// 压缩后的文件
		File zipfile = new File(zipUrl);
		FileZipUtil.zipFiles(srcfiles, zipfile);
		map.put("zipName", zipName);
		map.put("zipUrl", zipUrl);
		return map;
	}
	//获得当前web-inf绝对路径
	public static String getPath(){
		String path = FileZipUtil.class.getResource("/").getPath();
		path = path.substring(1, path.indexOf("classes"));
		return path;
	}

}
