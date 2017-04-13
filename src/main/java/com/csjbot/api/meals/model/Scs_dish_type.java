package com.csjbot.api.meals.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Zhangyangyang on 2017/4/13.
 */
@Table(name = "scs_dish_type")
public class Scs_dish_type {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String type_name;
    private String updater_fk;
    private String creator_fk;
    private Timestamp date_update;
    private Timestamp date_create;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
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

    public Scs_dish_type() {
        super();
    }

    public Scs_dish_type(String type_name, String updater_fk, String creator_fk, Timestamp date_update, Timestamp date_create) {
        this.type_name = type_name;
        this.updater_fk = updater_fk;
        this.creator_fk = creator_fk;
        this.date_update = date_update;
        this.date_create = date_create;
    }

    @Override
    public String toString() {
        return "Scs_dish_type{" +
                "id=" + id +
                ", type_name='" + type_name + '\'' +
                ", updater_fk='" + updater_fk + '\'' +
                ", creator_fk='" + creator_fk + '\'' +
                ", date_update=" + date_update +
                ", date_create=" + date_create +
                '}';
    }
}
