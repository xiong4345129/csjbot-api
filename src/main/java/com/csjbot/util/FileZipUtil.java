
package com.csjbot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

	// 配置文件读取权限
	public static void assignPermission(File file) {
		Set<PosixFilePermission> perms = new HashSet<>();
		perms.add(PosixFilePermission.OWNER_READ);
		perms.add(PosixFilePermission.OWNER_WRITE);
		perms.add(PosixFilePermission.GROUP_READ);
		perms.add(PosixFilePermission.OTHERS_READ);
		perms.add(PosixFilePermission.GROUP_WRITE);
		try {
			Files.setPosixFilePermissions(file.toPath(), perms);
		} catch (IOException e) {
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
			File file = new File(string);
			srcfiles.add(file);
		}
		Map<String, String> map = new HashMap<>();
		String zipName = RandomUtil.generateString(4);
		String zipUrl = "/opt/pkg/zip/" + zipName + ".zip";
		delAllFile("/opt/pkg/zip"); // 删除所有文件
		// 压缩后的文件
		File zipfile = new File(zipUrl);
		FileZipUtil.zipFiles(srcfiles, zipfile);
		assignPermission(zipfile);
		map.put("zipName", zipName + ".zip");
		map.put("zipUrl", "120.27.233.57:8001/zip/" + zipName + ".zip");
		return map;
	}

	/**
	 * 功能:删除指定文件夹下的所有文件
	 * 
	 * @param path:文件夹路径
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
			}
		}
	}

	/**
	 * 功能:保存文件
	 * 
	 * @param path:指定文件夹路径
	 * @param fileStr:文件加密后的字符串
	 */
	public static Map<String, Object> saveFileFromFaceReg(String path, String fileStr) {
		Map<String, Object> map = new HashMap<>();
		String fileName = RandomUtil.generateString(4) + RandomUtil.getTimestamp() + ".txt";
		File fileText = new File(path + fileName);
		// 向文件写入对象写入信息
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(fileText);
			// 写文件
			fileWriter.write(fileStr);
			// 关闭
			fileWriter.close();
			map.put("picName", fileName);
			map.put("picUrl", path + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 功能：删除文件
	 * 
	 * @param path：指定文件路径（包含文件名）
	 */
	public static boolean deleteFile(String path) {
		boolean flag = false;
		return flag;
	}

	/**
	 * 功能：读取文件
	 * 
	 * @param path：文件路径（包含文件名）
	 */
	public static String readFile(String path) {
		String fileData = "";
		return fileData;
	}
}
