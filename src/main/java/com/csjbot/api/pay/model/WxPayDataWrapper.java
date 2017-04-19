package com.csjbot.api.pay.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class WxPayDataWrapper {
    private boolean empty;
    private PmsOrderPay orderPay;
    private PmsPayDetailWx wxDetail;
    private List<PmsOrderItem> items;
    private Map<String, String> wxParams;

    public WxPayDataWrapper(boolean empty) {
        this.empty = empty;
    }

    public WxPayDataWrapper(Map<String, String> wxParams) {
        if (wxParams == null) throw new NullPointerException("param map");
        this.empty = false;
        this.wxParams = wxParams;
    }

    public boolean isEmpty() { return empty; }

    public void setEmpty(boolean empty) { this.empty = empty; }

    public PmsOrderPay getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(PmsOrderPay orderPay) {
        this.orderPay = orderPay;
    }

    public PmsPayDetailWx getWxDetail() {
        return wxDetail;
    }

    public void setWxDetail(PmsPayDetailWx wxDetail) {
        this.wxDetail = wxDetail;
    }

    public List<PmsOrderItem> getItems() {
        return items;
    }

    public void setItems(List<PmsOrderItem> items) {
        this.items = items;
    }

    public Map<String, String> getWxParams() {
        return wxParams;
    }

    public void setWxParams(Map<String, String> wxParams) {
        this.wxParams = wxParams;
    }

    public void syncRemote() {
        // todo
    }

    public boolean isClosed() {
        return orderPay.isClosed();
    }

    public ZonedDateTime lastSync() {
        return wxDetail.getSyncTime();
    }

    public boolean shouldSync(){
        return false; // todo
    }

}
