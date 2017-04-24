package com.csjbot.api.pay.model;

import java.time.ZonedDateTime;

public class WxClientResponse {
    //
    // public static class Refund {
    //     // private String refundNo;
    //     private Integer refundFee;
    //     private RefundStatus refundStatus;
    //     private ZonedDateTime refundTime;
    //     private String wxRefundId;
    //
    //     public Refund() { }
    //
    //     public Refund(Integer refundFee, RefundStatus refundStatus, ZonedDateTime refundTime, String wxRefundId) {
    //         this.refundFee = refundFee;
    //         this.refundStatus = refundStatus;
    //         this.refundTime = refundTime;
    //         this.wxRefundId = wxRefundId;
    //     }
    //
    //     public Integer getRefundFee() {
    //         return refundFee;
    //     }
    //
    //     public void setRefundFee(Integer refundFee) {
    //         this.refundFee = refundFee;
    //     }
    //
    //     public RefundStatus getRefundStatus() {
    //         return refundStatus;
    //     }
    //
    //     public void setRefundStatus(RefundStatus refundStatus) {
    //         this.refundStatus = refundStatus;
    //     }
    //
    //     public ZonedDateTime getRefundTime() {
    //         return refundTime;
    //     }
    //
    //     public void setRefundTime(ZonedDateTime refundTime) {
    //         this.refundTime = refundTime;
    //     }
    //
    //     public String getWxRefundId() {
    //         return wxRefundId;
    //     }
    //
    //     public void setWxRefundId(String wxRefundId) {
    //         this.wxRefundId = wxRefundId;
    //     }
    // }

    private String id;

    private String orderId;
    private String orderPseudoNo;
    private OrderStatus orderStatus;
    private PayStatus payStatus;
    private ZonedDateTime orderTime;
    private ZonedDateTime closeTime;

    private String codeUrl;
    private Integer orderTotalFee;
    private ZonedDateTime payTimeStart;
    private ZonedDateTime payTimeExpire;
    private ZonedDateTime payTimeEnd;
    private Integer payCashFee;
    private Integer payCouponFee;
    private Integer payRefundFee ; // not use
    private String wxOpenId;
    private String wxTransactionId;

    private String refundStatus;
    private Boolean hasRefund;
    private String refundNo;
    private Integer refundFee;
    private Integer refundTotalFee;
    private String wxRefundId;
    // private List<Refund> refundList;

    private String errCode;
    private String errDesc;
    private String remark;

    public WxClientResponse() { }

    public WxClientResponse(String orderId) {
        this.orderId = orderId;
    }

    public WxClientResponse(String orderId, OrderStatus orderStatus,
                            String orderPseudoNo, String codeUrl, String id) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderPseudoNo = orderPseudoNo;
        this.codeUrl = codeUrl;
        this.id = id;
    }

    public WxClientResponse(String orderId, OrderStatus orderStatus,
                            PayStatus payStatus, String id) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.payStatus = payStatus;
        this.id = id;
    }

    public WxClientResponse(String orderId, String refundNo) {
        this.orderId = orderId;
        this.refundNo = refundNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderPseudoNo() {
        return orderPseudoNo;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PayStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatus payStatus) {
        this.payStatus = payStatus;
    }

    public void setOrderPseudoNo(String orderPseudoNo) {
        this.orderPseudoNo = orderPseudoNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ZonedDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(ZonedDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus.name();
    }

    public Boolean getHasRefund() {
        return hasRefund;
    }

    public void setHasRefund(Boolean hasRefund) {
        this.hasRefund = hasRefund;
    }

    public Integer getRefundTotalFee() {
        return refundTotalFee;
    }

    public void setRefundTotalFee(Integer refundTotalFee) {
        this.refundTotalFee = refundTotalFee;
    }

    // public void setRefundList(List<Refund> refundList) {
    //     this.refundList = refundList;
    // }


    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public Integer getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Integer refundFee) {
        this.refundFee = refundFee;
    }

    public String getWxRefundId() {
        return wxRefundId;
    }

    public void setWxRefundId(String wxRefundId) {
        this.wxRefundId = wxRefundId;
    }
}
