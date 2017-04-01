package com.csjbot.pay.model;

import com.csjbot.pay.controller.OrderStatus;
import com.csjbot.pay.controller.PayServiceProvider;
import com.csjbot.pay.controller.PayStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class PmsOrderPay implements Serializable {

    private Date createTime;
    private Date updateTime;

    private String orderId;
    private Date orderTime;
    private String orderPseudoNo;
    private Integer orderTotalFee;
    private String orderStatus;
    private String orderErrCode;
    private String orderErrDesc;

    private String payService;
    private String payMethod;
    private String payCodeUrl;
    private String payProductId;
    private String payStatus;
    private String payErrCode;
    private String payErrDesc;

    private String prePayId;

    private String robotUid;
    private String robotModel;

    private String orderRequest;
    private String payRequest;

    private static final long serialVersionUID = 1L;

    public PmsOrderPay(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderPseudoNo() {
        return orderPseudoNo;
    }

    public void setOrderPseudoNo(String orderPseudoNo) {
        this.orderPseudoNo = orderPseudoNo;
    }

    public Integer getOrderTotalFee() {
        return orderTotalFee;
    }

    public void setOrderTotalFee(Integer orderTotalFee) {
        this.orderTotalFee = orderTotalFee;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    // public void setOrderStatus(String orderStatus) {
    //     this.orderStatus = orderStatus;
    // }

    public void setOrderStatus(OrderStatus status) {
        this.orderStatus = status.name();
    }

    public String getOrderErrCode() {
        return orderErrCode;
    }

    public void setOrderErrCode(String orderErrCode) {
        this.orderErrCode = orderErrCode;
    }

    public String getOrderErrDesc() {
        return orderErrDesc;
    }

    public void setOrderErrDesc(String orderErrDesc) {
        this.orderErrDesc = orderErrDesc;
    }

    public String getPayService() {
        return payService;
    }

    public void setPayService(PayServiceProvider sp) {
        this.payService = sp.name();
    }

    // public void setPayService(String payService) {
    //     this.payService = payService;
    // }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayCodeUrl() {
        return payCodeUrl;
    }

    public void setPayCodeUrl(String payCodeUrl) {
        this.payCodeUrl = payCodeUrl;
    }

    public String getPayProductId() {
        return payProductId;
    }

    public void setPayProductId(String payProductId) {
        this.payProductId = payProductId;
    }

    public String getPrePayId() {
        return prePayId;
    }

    public void setPrePayId(String prePayId) {
        this.prePayId = prePayId;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatus status) {
        this.payStatus = status.name();
    }

    // public void setPayStatus(String payStatus) {
    //     this.payStatus = payStatus;
    // }


    public String getPayErrCode() {
        return payErrCode;
    }

    public void setPayErrCode(String payErrCode) {
        this.payErrCode = payErrCode;
    }

    public String getPayErrDesc() {
        return payErrDesc;
    }

    public void setPayErrDesc(String payErrDesc) {
        this.payErrDesc = payErrDesc;
    }

    public String getRobotUid() {
        return robotUid;
    }

    public void setRobotUid(String robotUid) {
        this.robotUid = robotUid;
    }

    public String getRobotModel() {
        return robotModel;
    }

    public void setRobotModel(String robotModel) {
        this.robotModel = robotModel;
    }


    public String getOrderRequestText() {
        return orderRequest;
    }

    public void setOrderRequestText(String orderRequest) {
        this.orderRequest = orderRequest;
    }

    public String getPayRequestText() {
        return payRequest;
    }

    public void setPayRequestText(String payRequest) {
        this.payRequest = payRequest;
    }

    private transient List<PmsOrderDetail> orderDetails;

    public List<PmsOrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<PmsOrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    // private transient Map<String, String> map;
    //
    // public Map<String, String> getMap() {
    //     return map;
    // }
    //
    // public void setMap(Map<String, String> map) {
    //     this.map = map;
    // }


}