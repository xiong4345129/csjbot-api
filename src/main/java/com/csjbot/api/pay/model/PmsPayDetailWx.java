package com.csjbot.api.pay.model;

import java.time.ZonedDateTime;

public class PmsPayDetailWx {
    /**
     * 后台生成的订单流水号
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
     * 终端IP
     */
    private String spbillCreateIp;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 预支付交易会话标识
     */
    private String prepayId;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 指定支付方式
     */
    private String limitPay;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 二维码链接
     */
    private String codeUrl;

    /**
     * 交易状态
     */
    private String tradeState;

    /**
     * 交易状态描述
     */
    private String tradeStateDesc;

    /**
     * 交易起始时间
     */
    private ZonedDateTime timeStart;

    /**
     * 支付完成时间
     */
    private ZonedDateTime timeEnd;

    /**
     * 交易结束时间
     */
    private ZonedDateTime timeExpire;

    /**
     * 付款银行
     */
    private String bankType;

    /**
     * 标价金额
     */
    private Integer totalFee;

    /**
     * 标价货种
     */
    private String feeType;

    /**
     * 退款金额
     */
    private Integer refundFee;

    /**
     * 退款货币种类
     */
    private String refundFeeType;

    /**
     * 现金支付金额
     */
    private Integer cashFee;

    /**
     * 现金支付币种
     */
    private String cashFeeType;

    /**
     * 现金退款金额
     */
    private Integer cashRefundFee;

    /**
     * 应结订单金额
     */
    private Integer settlementTotalFee;

    /**
     * 代金券金额
     */
    private Integer couponFee;

    /**
     * 商品标记
     */
    private String goodsTag;

    /**
     * 用户标识
     */
    private String openid;

    /**
     * 是否关注公众账号
     */
    private String isSubscribe;

    /**
     * 上次向微信查询订单时间
     */
    private ZonedDateTime syncTime;

    public PmsPayDetailWx() { }

    public PmsPayDetailWx(String orderId) {
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

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(WxTradeType tradeType) {
        this.tradeType = tradeType.name();
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getTradeStateDesc() {
        return tradeStateDesc;
    }

    public void setTradeStateDesc(String tradeStateDesc) {
        this.tradeStateDesc = tradeStateDesc;
    }

    public ZonedDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(ZonedDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public ZonedDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(ZonedDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public ZonedDateTime getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(ZonedDateTime timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public Integer getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Integer refundFee) {
        this.refundFee = refundFee;
    }

    public String getRefundFeeType() {
        return refundFeeType;
    }

    public void setRefundFeeType(String refundFeeType) {
        this.refundFeeType = refundFeeType;
    }

    public Integer getCashFee() {
        return cashFee;
    }

    public void setCashFee(Integer cashFee) {
        this.cashFee = cashFee;
    }

    public String getCashFeeType() {
        return cashFeeType;
    }

    public void setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType;
    }

    public Integer getCashRefundFee() {
        return cashRefundFee;
    }

    public void setCashRefundFee(Integer cashRefundFee) {
        this.cashRefundFee = cashRefundFee;
    }

    public Integer getSettlementTotalFee() {
        return settlementTotalFee;
    }

    public void setSettlementTotalFee(Integer settlementTotalFee) {
        this.settlementTotalFee = settlementTotalFee;
    }

    public Integer getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(Integer couponFee) {
        this.couponFee = couponFee;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public ZonedDateTime getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(ZonedDateTime syncTime) {
        this.syncTime = syncTime;
    }
}