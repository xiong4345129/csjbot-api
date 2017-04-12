package com.csjbot.api.pay.model;

import java.time.ZonedDateTime;
import java.util.List;

public class WxClientOrderRequest {
    private String id;

    public String getId() {
        return id;
    }

    private String orderPseudoNo;
    private ZonedDateTime orderTime;
    private List<WxClientOrderItem> orderList; // todo
    private String orderDesc;

    private WxTradeType payMethod;

    private String robotUid;
    private String robotModel;
    private String venderCode;
    private String venderUser;

    public String getOrderPseudoNo() {
        return orderPseudoNo;
    }

    public ZonedDateTime getOrderTime() {
        return orderTime;
    }

    public List<WxClientOrderItem> getOrderList() {
        return orderList;
    }

    public String getOrderDesc() { return orderDesc; }

    public WxTradeType getPayMethod() { return payMethod; }

    public String getRobotUid() {
        return robotUid;
    }

    public String getRobotModel() {
        return robotModel;
    }

    public String getVenderCode() {
        return venderCode;
    }

    public String getVenderUser() {
        return venderUser;
    }

}
