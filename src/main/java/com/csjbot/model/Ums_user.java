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
 * @version 创建时间：2017年3月21日 上午9:31:49
 * 类说明
 */
@Table(name = "ums_user")
public class Ums_user {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private String username;
	private String password;
	private String salt;
	private String phone;
	private String email;
	private String realName;
	private Integer sex;
	private String idCard;
	private Integer is_super_admin;
	private Integer password_changed;
	private Timestamp last_login_time;
	private Integer status;
	private Timestamp date_effect;
	private Timestamp date_expire;
	private Timestamp date_create;
	private Timestamp date_update;
	private String creator_fk;
	private String updater_fk;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public Integer getIs_super_admin() {
		return is_super_admin;
	}
	public void setIs_super_admin(Integer is_super_admin) {
		this.is_super_admin = is_super_admin;
	}
	public Integer getPassword_changed() {
		return password_changed;
	}
	public void setPassword_changed(Integer password_changed) {
		this.password_changed = password_changed;
	}
	public Timestamp getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(Timestamp last_login_time) {
		this.last_login_time = last_login_time;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Timestamp getDate_effect() {
		return date_effect;
	}
	public void setDate_effect(Timestamp date_effect) {
		this.date_effect = date_effect;
	}
	public Timestamp getDate_expire() {
		return date_expire;
	}
	public void setDate_expire(Timestamp date_expire) {
		this.date_expire = date_expire;
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
	public Ums_user() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Ums_user(String id, String username, String password, String salt, String phone, String email,
			String realName, Integer sex, String idCard, Integer is_super_admin, Integer password_changed,
			Timestamp last_login_time, Integer status, Timestamp date_effect, Timestamp date_expire,
			Timestamp date_create, Timestamp date_update, String creator_fk, String updater_fk) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.phone = phone;
		this.email = email;
		this.realName = realName;
		this.sex = sex;
		this.idCard = idCard;
		this.is_super_admin = is_super_admin;
		this.password_changed = password_changed;
		this.last_login_time = last_login_time;
		this.status = status;
		this.date_effect = date_effect;
		this.date_expire = date_expire;
		this.date_create = date_create;
		this.date_update = date_update;
		this.creator_fk = creator_fk;
		this.updater_fk = updater_fk;
	}
	@Override
	public String toString() {
		return "Ums_user [id=" + id + ", username=" + username + ", password=" + password + ", salt=" + salt
				+ ", phone=" + phone + ", email=" + email + ", realName=" + realName + ", sex=" + sex + ", idCard="
				+ idCard + ", is_super_admin=" + is_super_admin + ", password_changed=" + password_changed
				+ ", last_login_time=" + last_login_time + ", status=" + status + ", date_effect=" + date_effect
				+ ", date_expire=" + date_expire + ", date_create=" + date_create + ", date_update=" + date_update
				+ ", creator_fk=" + creator_fk + ", updater_fk=" + updater_fk + "]";
	}
	
	

}
