package com.csjbot.pay.model;

public class OrderItem {
    private String objectId;
    private Integer amount;
    private String state;

    public String getObjectId() {
        return objectId;
    }

    public Integer getQty() {
        return amount;
    }

    public String getState() {
        return state;
    }
}
