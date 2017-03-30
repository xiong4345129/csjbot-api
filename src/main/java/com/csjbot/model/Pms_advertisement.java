/**
 * 
 */
package com.csjbot.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 作者：Zhangyangyang
 * @version 创建时间：2017年3月27日 下午3:39:54
 * 类说明
 */
@Table(name = "pms_advertisement")
public class Pms_advertisement {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private String name;
	private String memo;
	private Integer valid;
	private String updater_fk;
	private String creator_fk;
	private Timestamp date_update;
	private Timestamp date_create;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	public String getUpdater_fk() {
		return updater_fk;
	}
	public void setUpdater_fk(String updater_fk) {
		this.updater_fk = updater_fk;
	}
	public String getCreator_fk() {
		return creator_fk;
	}
	public void setCreator_fk(String creator_fk) {
		this.creator_fk = creator_fk;
	}
	public Timestamp getDate_update() {
		return date_update;
	}
	public void setDate_update(Timestamp date_update) {
		this.date_update = date_update;
	}
	public Timestamp getDate_create() {
		return date_create;
	}
	public void setDate_create(Timestamp date_create) {
		this.date_create = date_create;
	}
	public Pms_advertisement() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Pms_advertisement(String id, String name, String memo, Integer valid, String updater_fk, String creator_fk,
			Timestamp date_update, Timestamp date_create) {
		super();
		this.id = id;
		this.name = name;
		this.memo = memo;
		this.valid = valid;
		this.updater_fk = updater_fk;
		this.creator_fk = creator_fk;
		this.date_update = date_update;
		this.date_create = date_create;
	}
	@Override
	public String toString() {
		return "Pms_advertisement [id=" + id + ", name=" + name + ", memo=" + memo + ", valid=" + valid
				+ ", updater_fk=" + updater_fk + ", creator_fk=" + creator_fk + ", date_update=" + date_update
				+ ", date_create=" + date_create + "]";
	}
}
