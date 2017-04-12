package com.csjbot.api.pay.model;

import java.time.ZonedDateTime;

public class WxClientQueryResponse {
    private String orderId;
    private ZonedDateTime orderTime;
    private Integer orderTotalFee;
    private String payStatus;
    private ZonedDateTime payTimeStart;
    private ZonedDateTime payTimeExpire;
    private ZonedDateTime payTimeEnd;
    private Integer payCashFee;
    private Integer payCouponFee;
    private Integer payRefundFee;
    private String wxOpenId;
    private String wxTransactionId;
    private String remark;

    public WxClientQueryResponse() { }

    public WxClientQueryResponse(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public ZonedDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(ZonedDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getOrderTotalFee() {
        return orderTotalFee;
    }

    public void setOrderTotalFee(Integer orderTotalFee) {
        this.orderTotalFee = orderTotalFee;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public ZonedDateTime getPayTimeStart() {
        return payTimeStart;
    }

    public void setPayTimeStart(ZonedDateTime payTimeStart) {
        this.payTimeStart = payTimeStart;
    }

    public ZonedDateTime getPayTimeExpire() {
        return payTimeExpire;
    }

    public void setPayTimeExpire(ZonedDateTime payTimeExpire) {
        this.payTimeExpire = payTimeExpire;
    }

    public ZonedDateTime getPayTimeEnd() {
        return payTimeEnd;
    }

    public void setPayTimeEnd(ZonedDateTime payTimeEnd) {
        this.payTimeEnd = payTimeEnd;
    }

    public Integer getPayCashFee() {
        return payCashFee;
    }

    public void setPayCashFee(Integer payCashFee) {
        this.payCashFee = payCashFee;
    }

    public Integer getPayCouponFee() {
        return payCouponFee;
    }

    public void setPayCouponFee(Integer payCouponFee) {
        this.payCouponFee = payCouponFee;
    }

    public Integer getPayRefundFee() {
        return payRefundFee;
    }

    public void setPayRefundFee(Integer payRefundFee) {
        this.payRefundFee = payRefundFee;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getWxTransactionId() {
        return wxTransactionId;
    }

    public void setWxTransactionId(String wxTransactionId) {
        this.wxTransactionId = wxTransactionId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
