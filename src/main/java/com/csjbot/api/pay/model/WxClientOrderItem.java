package com.csjbot.api.pay.model;

// todo use PmsOrderItem?
public class WxClientOrderItem {
    private String itemId;
    private Integer itemQty;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getItemQty() {
        return itemQty;
    }

    public void setItemQty(Integer itemQty) {
        this.itemQty = itemQty;
    }

}
