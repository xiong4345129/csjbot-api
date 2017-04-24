package com.csjbot.api.robot.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "sys_version_robot")
public class Sys_version_robot {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private Integer category;
	private Integer channel;
	private Integer version_code;
	private String version_name;
	private String md5;
	private Byte platform;
	private String memo;
	private Byte valid;
	private String updater_fk;
	private String creator_fk;
	private Date date_update;
	private Date date_create;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public Integer getVersion_code() {
		return version_code;
	}
	public void setVersion_code(Integer version_code) {
		this.version_code = version_code;
	}
	public String getVersion_name() {
		return version_name;
	}
	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public Byte getPlatform() {
		return platform;
	}
	public void setPlatform(Byte platform) {
		this.platform = platform;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Byte getValid() {
		return valid;
	}
	public void setValid(Byte valid) {
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
	public Date getDate_update() {
		return date_update;
	}
	public void setDate_update(Date date_update) {
		this.date_update = date_update;
	}
	public Date getDate_create() {
		return date_create;
	}
	public void setDate_create(Date date_create) {
		this.date_create = date_create;
	}
	
	public Sys_version_robot() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Sys_version_robot(String id, Integer category, Integer channel, Integer version_code, String version_name,
			String md5, Byte platform, String memo, Byte valid, String updater_fk, String creator_fk, Date date_update,
			Date date_create) {
		super();
		this.id = id;
		this.category = category;
		this.channel = channel;
		this.version_code = version_code;
		this.version_name = version_name;
		this.md5 = md5;
		this.platform = platform;
		this.memo = memo;
		this.valid = valid;
		this.updater_fk = updater_fk;
		this.creator_fk = creator_fk;
		this.date_update = date_update;
		this.date_create = date_create;
	}
	
	@Override
	public String toString() {
		return "Sys_version_robot [id=" + id + ", category=" + category + ", channel=" + channel + ", version_code="
				+ version_code + ", version_name=" + version_name + ", md5=" + md5 + ", platform=" + platform
				+ ", memo=" + memo + ", valid=" + valid + ", updater_fk=" + updater_fk + ", creator_fk=" + creator_fk
				+ ", date_update=" + date_update + ", date_create=" + date_create + "]";
	}
	

}
