/**
 * 
 */
package com.csjbot.dao;

import java.util.List;

import com.csjbot.model.Frs_face;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 上午9:54:30 类说明
 */
public interface Frs_faceDAO extends BaseDAO<Frs_face> {
	
	// 根据face_id查询
	public abstract Frs_face findFaceByFaceId(String face_id);

	// 根据person_id查询
	public abstract List<Frs_face> findFaceByPerId(String person_id);
}
