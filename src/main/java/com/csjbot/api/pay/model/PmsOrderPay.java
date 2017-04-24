package com.csjbot.api.pay.model;

import java.time.ZonedDateTime;

public class PmsOrderPay {
    /**
     * 后台生成的正式订单流水号，全局唯一
     */
    private String orderId;

    /**
     * 数据库记录产生时间
     */
    private ZonedDateTime createTime;

    /**
     * 数据库记录更新时间
     */
    private ZonedDateTime updateTime;

    /**
     * 设备端顾客下单时间
     */
    private ZonedDateTime orderTime;

    /**
     * 设备端自建的非正式订单号
     */
    private String orderPseudoNo;

    /**
     * 下单请求的来源设备id、在各自group中唯一
     */
    private String orderDeviceId;

    /**
     * 下单请求的来源设备所属类别或型号
     */
    private String orderDeviceGroup;

    /**
     * 订单总金额，单位为分，取整
     */
    private Integer orderTotalFee;

    /**
     * 下单状态
     */
    private String orderStatus;

    /**
     * 下单错误代码
     */
    private String orderErrCode;

    /**
     * 下单错误描述
     */
    private String orderErrDesc;

    /**
     * 提供支付服务的三方名称
     */
    private String payService;

    /**
     * 支付状态
     */
    private String payStatus;

    /**
     * 支付错误代码
     */
    private String payErrCode;

    /**
     * 支付错误描述
     */
    private String payErrDesc;

    /**
     * 上次向三方查询、同步信息的时间
     */
    private ZonedDateTime syncTime;

    /**
     * 订单是否处理完成
     */
    private Boolean isClosed;

    /**
     * 后台下单支付记录的关闭时间
     */
    private ZonedDateTime closeTime;

    /**
     * 备注信息
     */
    private String remark;

    public PmsOrderPay() { }

    public PmsOrderPay(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public ZonedDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(ZonedDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderPseudoNo() {
        return orderPseudoNo;
    }

    public void setOrderPseudoNo(String orderPseudoNo) {
        this.orderPseudoNo = orderPseudoNo;
    }

    public String getOrderDeviceId() {
        return orderDeviceId;
    }

    public void setOrderDeviceId(String orderDeviceId) {
        this.orderDeviceId = orderDeviceId;
    }

    public String getOrderDeviceGroup() {
        return orderDeviceGroup;
    }

    public void setOrderDeviceGroup(String orderDeviceGroup) {
        this.orderDeviceGroup = orderDeviceGroup;
    }

    public Integer getOrderTotalFee() {
        return orderTotalFee;
    }

    public void setOrderTotalFee(Integer orderTotalFee) {
        this.orderTotalFee = orderTotalFee;
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus.name(); // todo
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

    public void setPayService(PayServiceProvider payService) {
        this.payService = payService.name();
    }

    public PayStatus getPayStatus() {
        return PayStatus.valueOf(payStatus);
    }

    public void setPayStatus(PayStatus payStatus) {
        this.payStatus = payStatus.name();
    }

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

    public ZonedDateTime getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(ZonedDateTime syncTime) {
        this.syncTime = syncTime;
    }

    public Boolean isClosed(){
        return isClosed;
    }

    public void setClosed(Boolean closed) {
        isClosed = closed;
    }

    public ZonedDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(ZonedDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}