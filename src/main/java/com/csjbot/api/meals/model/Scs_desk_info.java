package com.csjbot.api.meals.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Zhangyangyang on 2017/4/18.
 */
@Table(name = "scs_desk_info")
public class Scs_desk_info {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String number;
    private String alias;
    private String memo;
    private float deskx;
    private float desky;
    private float deskz;
    private float deskw;
    private float deskv;
    private float deskq;
    private int valid;
    private String updater_fk;
    private String creator_fk;
    private Timestamp date_update;
    private Timestamp date_create;
    private Integer desk_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public float getDeskx() {
        return deskx;
    }

    public void setDeskx(float deskx) {
        this.deskx = deskx;
    }

    public float getDesky() {
        return desky;
    }

    public void setDesky(float desky) {
        this.desky = desky;
    }

    public float getDeskz() {
        return deskz;
    }

    public void setDeskz(float deskz) {
        this.deskz = deskz;
    }

    public float getDeskw() {
        return deskw;
    }

    public void setDeskw(float deskw) {
        this.deskw = deskw;
    }

    public float getDeskv() {
        return deskv;
    }

    public void setDeskv(float deskv) {
        this.deskv = deskv;
    }

    public float getDeskq() {
        return deskq;
    }

    public void setDeskq(float deskq) {
        this.deskq = deskq;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
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

    public Integer getDesk_type() {
        return desk_type;
    }

    public void setDesk_type(Integer desk_type) {
        this.desk_type = desk_type;
    }

    public Scs_desk_info() {
        super();
    }

    public Scs_desk_info(String number, String alias, String memo, float deskx, float desky, float deskz, float deskw, float deskv, float deskq, int valid, String updater_fk, String creator_fk, Timestamp date_update, Timestamp date_create, Integer desk_type) {
        this.number = number;
        this.alias = alias;
        this.memo = memo;
        this.deskx = deskx;
        this.desky = desky;
        this.deskz = deskz;
        this.deskw = deskw;
        this.deskv = deskv;
        this.deskq = deskq;
        this.valid = valid;
        this.updater_fk = updater_fk;
        this.creator_fk = creator_fk;
        this.date_update = date_update;
        this.date_create = date_create;
        this.desk_type = desk_type;
    }

    @Override
    public String toString() {
        return "Scs_desk_info{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", alias='" + alias + '\'' +
                ", memo='" + memo + '\'' +
                ", deskx=" + deskx +
                ", desky=" + desky +
                ", deskz=" + deskz +
                ", deskw=" + deskw +
                ", deskv=" + deskv +
                ", deskq=" + deskq +
                ", valid=" + valid +
                ", updater_fk='" + updater_fk + '\'' +
                ", creator_fk='" + creator_fk + '\'' +
                ", date_update=" + date_update +
                ", date_create=" + date_create +
                ", desk_type=" + desk_type +
                '}';
    }
}
