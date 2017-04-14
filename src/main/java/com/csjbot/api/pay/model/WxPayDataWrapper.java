package com.csjbot.api.pay.model;

import java.util.List;
import java.util.Map;

public class WxPayDataWrapper {
    private boolean empty;
    private PmsOrderPay orderPayData;
    private PmsPayDetailWx wxDetailData;
    private List<PmsOrderItem> items;
    private Map<String, String> wxParams;

    public WxPayDataWrapper() {
    }

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

    public PmsOrderPay getOrderPayData() {
        return orderPayData;
    }

    public void setOrderPayData(PmsOrderPay orderPayData) {
        this.orderPayData = orderPayData;
    }

    public PmsPayDetailWx getWxDetailData() {
        return wxDetailData;
    }

    public void setWxDetailData(PmsPayDetailWx wxDetailData) {
        this.wxDetailData = wxDetailData;
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
}
