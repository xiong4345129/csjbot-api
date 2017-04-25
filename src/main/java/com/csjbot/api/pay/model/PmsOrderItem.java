package com.csjbot.api.pay.model;

public class PmsOrderItem {
    private String orderId;

    private String itemId;

    private Integer itemQty;

    private Integer unitPrice;

    public PmsOrderItem() {
    }

    public PmsOrderItem(String orderId, String itemId, Integer itemQty) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemQty = itemQty;
    }

    public PmsOrderItem(String orderId, String itemId, Integer itemQty, Integer unitPrice) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemQty = itemQty;
        this.unitPrice = unitPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public Integer getItemQty() {
        return itemQty;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }
}