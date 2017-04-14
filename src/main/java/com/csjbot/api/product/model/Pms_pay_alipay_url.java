package com.csjbot.api.product.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Zhangyangyang on 2017/4/13.
 */
@Table(name="pms_pay_alipay_url")
public class Pms_pay_alipay_url {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String redirect_key;
    private String url;
    private Timestamp creat_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRedirect_key() {
        return redirect_key;
    }

    public void setRedirect_key(String redirect_key) {
        this.redirect_key = redirect_key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getCreat_time() {
        return creat_time;
    }

    public void setCreat_time(Timestamp creat_time) {
        this.creat_time = creat_time;
    }

    public Pms_pay_alipay_url() {
        super();
    }

    public Pms_pay_alipay_url(String redirect_key, String url, Timestamp creat_time) {
        this.redirect_key = redirect_key;
        this.url = url;
        this.creat_time = creat_time;
    }

    @Override
    public String toString() {
        return "Pms_pay_alipay_url{" +
                "id=" + id +
                ", redirect_key='" + redirect_key + '\'' +
                ", url='" + url + '\'' +
                ", creat_time=" + creat_time +
                '}';
    }
}
