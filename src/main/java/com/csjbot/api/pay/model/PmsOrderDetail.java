package com.csjbot.api.pay.model;

import java.io.Serializable;

/**
 * @author
 */
public class PmsOrderDetail implements Serializable {
    private final String orderId;

    private final String itemId;

    private final Integer itemQty;

    private Integer unitPrice;

    private static final long serialVersionUID = 1L;

    public PmsOrderDetail(String orderId, String itemId, Integer itemQty) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemQty = itemQty;
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