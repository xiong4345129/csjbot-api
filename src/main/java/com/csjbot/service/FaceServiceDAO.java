
package com.csjbot.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.csjbot.model.Frs_person;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 上午9:57:39
 * 类说明
 */
public interface FaceServiceDAO {
	//添加一个个体
	public abstract JSONObject addPerson(Frs_person frs_person,String image);
	
	//根据姓名删除个体
	public abstract JSONObject deletePerson(String name);
	
	//根据姓名查询一个个体信息
	public abstract JSONObject findPersonByName(String name);
	
	//修个个体名称
	public abstract JSONObject updatePersonName(String person_name,String new_name);
	
	//查询一个组的个体信息
	public abstract JSONObject findAllPerson(String group_id);
	
	//添加人脸
	public abstract JSONObject addFace(String person_name,List<String> image);
	
	//删除人脸
	public abstract JSONObject deleteFace(String person_name,List<String> face_id);
	
	//查询一个person里的所有face
	public abstract JSONObject findFaceAll(String person_name);
	
	//查询一个face
	public abstract JSONObject findFaceByFaceId(String face_id);
	
	//查询组列表
	public abstract JSONObject findAllGroup();
	
	//人脸识别（包括检测和检索）
	public abstract JSONObject faceDect(String image,String group_id,int mode);
	
	//人脸检测
	public abstract Map<String, Object> faceDet(String image,int mode);
	
	//人脸检索
	public abstract Map<String, Object> faceIden(String image,String group_id);
	
}
