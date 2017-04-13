package com.csjbot.api.meals.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Zhangyangyang on 2017/4/13.
 */
@Table(name = "scs_dish_info")
public class Scs_dish_info {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private Integer dish_type;
    private String name;
    private double price;
    private String memo;
    private Integer valid;
    private String updater_fk;
    private String creator_fk;
    private Timestamp date_update;
    private Timestamp date_create;

    public String getId() {
        return id;
    }

    public Integer getDish_type() {
        return dish_type;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getMemo() {
        return memo;
    }

    public Integer getValid() {
        return valid;
    }

    public String getUpdater_fk() {
        return updater_fk;
    }

    public String getCreator_fk() {
        return creator_fk;
    }

    public Timestamp getDate_update() {
        return date_update;
    }

    public Timestamp getDate_create() {
        return date_create;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDish_type(Integer dish_type) {
        this.dish_type = dish_type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public void setUpdater_fk(String updater_fk) {
        this.updater_fk = updater_fk;
    }

    public void setCreator_fk(String creator_fk) {
        this.creator_fk = creator_fk;
    }

    public void setDate_update(Timestamp date_update) {
        this.date_update = date_update;
    }

    public void setDate_create(Timestamp date_create) {
        this.date_create = date_create;
    }

    public Scs_dish_info() {
        super();
    }

    public Scs_dish_info(String id, Integer dish_type, String name, double price, String memo, Integer valid, String updater_fk, String creator_fk, Timestamp date_update, Timestamp date_create) {
        this.id = id;
        this.dish_type = dish_type;
        this.name = name;
        this.price = price;
        this.memo = memo;
        this.valid = valid;
        this.updater_fk = updater_fk;
        this.creator_fk = creator_fk;
        this.date_update = date_update;
        this.date_create = date_create;
    }

    @Override
    public String toString() {
        return "Scs_dish_info{" +
                "id='" + id + '\'' +
                ", dish_type=" + dish_type +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", memo='" + memo + '\'' +
                ", valid=" + valid +
                ", updater_fk='" + updater_fk + '\'' +
                ", creator_fk='" + creator_fk + '\'' +
                ", date_update=" + date_update +
                ", date_create=" + date_create +
                '}';
    }
}
