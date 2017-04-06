package com.csjbot.api.pay.model;

public class WxClientOrderResponse {
    private String id;

    private final String orderStatus;
    private final String orderPseudoNo;
    private String orderId;
    private String codeUrl;
    private String errCode;
    private String errDesc;
    private String remark;

    public WxClientOrderResponse(OrderStatus status, String pseudoNo) {
        this.orderStatus = status.name();
        this.orderPseudoNo = pseudoNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderPseudoNo() {
        return orderPseudoNo;
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
