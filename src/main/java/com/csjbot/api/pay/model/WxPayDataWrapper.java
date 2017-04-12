package com.csjbot.api.pay.model;

import java.util.List;
import java.util.Map;

public class WxPayDataWrapper {
    private PmsOrderPay orderPayData;
    private PmsPayDetailWx wxDetailData;
    private List<PmsOrderItem> items;
    private Map<String, String> wxParams;

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
