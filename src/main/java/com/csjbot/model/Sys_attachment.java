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
 * @version 创建时间：2017年3月21日 上午9:40:39
 * 类说明
 */
@Table(name = "sys_attachment")
public class Sys_attachment {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String transaction_id;
	private String transaction_type;
	private String file_type;
	private String name;
	private String original_name;
	private String alias_name;
	private String server_host;
	private String directory;
	private String location;
	private String memo;
	private Integer size;
	private String suffix;
	private String owner_fk;
	private String creator_fk;
	private String updater_fk;
	private Timestamp date_create;
	private Timestamp date_update;
	private Integer is_valid;
	private Integer sort ;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOriginal_name() {
		return original_name;
	}
	public void setOriginal_name(String original_name) {
		this.original_name = original_name;
	}
	public String getAlias_name() {
		return alias_name;
	}
	public void setAlias_name(String alias_name) {
		this.alias_name = alias_name;
	}
	public String getServer_host() {
		return server_host;
	}
	public void setServer_host(String server_host) {
		this.server_host = server_host;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getOwner_fk() {
		return owner_fk;
	}
	public void setOwner_fk(String owner_fk) {
		this.owner_fk = owner_fk;
	}
	public String getCreator_fk() {
		return creator_fk;
	}
	public void setCreator_fk(String creator_fk) {
		this.creator_fk = creator_fk;
	}
	public String getUpdater_fk() {
		return updater_fk;
	}
	public void setUpdater_fk(String updater_fk) {
		this.updater_fk = updater_fk;
	}
	public Timestamp getDate_create() {
		return date_create;
	}
	public void setDate_create(Timestamp date_create) {
		this.date_create = date_create;
	}
	public Timestamp getDate_update() {
		return date_update;
	}
	public void setDate_update(Timestamp date_update) {
		this.date_update = date_update;
	}
	public Integer getIs_valid() {
		return is_valid;
	}
	public void setIs_valid(Integer is_valid) {
		this.is_valid = is_valid;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Sys_attachment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Sys_attachment(Integer id, String transaction_id, String transaction_type, String file_type, String name,
			String original_name, String alias_name, String server_host, String directory, String location, String memo,
			Integer size, String suffix, String owner_fk, String creator_fk, String updater_fk, Timestamp date_create,
			Timestamp date_update, Integer is_valid, Integer sort) {
		super();
		this.id = id;
		this.transaction_id = transaction_id;
		this.transaction_type = transaction_type;
		this.file_type = file_type;
		this.name = name;
		this.original_name = original_name;
		this.alias_name = alias_name;
		this.server_host = server_host;
		this.directory = directory;
		this.location = location;
		this.memo = memo;
		this.size = size;
		this.suffix = suffix;
		this.owner_fk = owner_fk;
		this.creator_fk = creator_fk;
		this.updater_fk = updater_fk;
		this.date_create = date_create;
		this.date_update = date_update;
		this.is_valid = is_valid;
		this.sort = sort;
	}
	@Override
	public String toString() {
		return "Sys_attachment [id=" + id + ", transaction_id=" + transaction_id + ", transaction_type="
				+ transaction_type + ", file_type=" + file_type + ", name=" + name + ", original_name=" + original_name
				+ ", alias_name=" + alias_name + ", server_host=" + server_host + ", directory=" + directory
				+ ", location=" + location + ", memo=" + memo + ", size=" + size + ", suffix=" + suffix + ", owner_fk="
				+ owner_fk + ", creator_fk=" + creator_fk + ", updater_fk=" + updater_fk + ", date_create="
				+ date_create + ", date_update=" + date_update + ", is_valid=" + is_valid + ", sort=" + sort + "]";
	}
	
	
}
