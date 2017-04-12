package com.csjbot.api.pay.model;

public class WxClientOrderResponse {
    private String id;

    private final OrderStatus orderStatus;
    private String orderPseudoNo;
    private String orderId;
    private String codeUrl;
    private String errCode;
    private String errDesc;
    private String remark;

    public WxClientOrderResponse(OrderStatus status) {
        this.orderStatus = status;
    }

    public WxClientOrderResponse(OrderStatus orderStatus, String orderPseudoNo, String id) {
        this.orderStatus = orderStatus;
        this.orderPseudoNo = orderPseudoNo;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getOrderPseudoNo() {
        return orderPseudoNo;
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
}
