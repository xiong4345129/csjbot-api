package com.csjbot.api.pay.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

// todo
public class PmsRefundDetailWx implements Serializable {
    /**
     * 数据库记录产生时间
     */
    private ZonedDateTime createTime;

    /**
     * 数据库记录更新时间
     */
    private ZonedDateTime updateTime;

    /**
     * 后台生成的订单退款流水号
     */
    private String refundNo;

    /**
     * 退款笔数
     */
    private Integer refundCount;

    /**
     * 微信退款编号，即 _$n 中n值
     */
    private Integer refundIdSn;

    /**
     * 微信退款单号
     */
    private String refundId;

    /**
     * 退款状态
     */
    private String refundStatus;

    /**
     * 退款成功时间
     */
    private ZonedDateTime refundSuccessTime;

    /**
     * 退款资金来源
     */
    private String refundAccount;

    /**
     * 退款入账账户
     */
    private String refundRecvAccout;

    private String refundChannel;

    /**
     * 退款金额
     */
    private Integer refundFee;

    /**
     * 退款货币种类
     */
    private String refundFeeType;

    // /**
    //  * 应结退款金额
    //  */
    // private Integer settlementTotalFee;

    /**
     * 现金退款金额
     */
    private Integer cashRefundFee;

    /**
     * 代金券退款总金额
     */
    private Integer couponRefundFee;

    private static final long serialVersionUID = 1L;

    public PmsRefundDetailWx() { }

    public PmsRefundDetailWx(String refundNo) {
        this.refundNo = refundNo;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public Integer getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(Integer refundCount) {
        this.refundCount = refundCount;
    }

    public Integer getRefundIdSn() {
        return refundIdSn;
    }

    public void setRefundIdSn(Integer refundIdSn) {
        this.refundIdSn = refundIdSn;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus.name();
    }

    public ZonedDateTime getRefundSuccessTime() {
        return refundSuccessTime;
    }

    public void setRefundSuccessTime(ZonedDateTime refundSuccessTime) {
        this.refundSuccessTime = refundSuccessTime;
    }

    public String getRefundAccount() {
        return refundAccount;
    }

    public void setRefundAccount(String refundAccount) {
        this.refundAccount = refundAccount;
    }

    public String getRefundRecvAccout() {
        return refundRecvAccout;
    }

    public void setRefundRecvAccout(String refundRecvAccout) {
        this.refundRecvAccout = refundRecvAccout;
    }

    public String getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel;
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

    // public Integer getSettlementTotalFee() {
    //     return settlementTotalFee;
    // }
    //
    // public void setSettlementTotalFee(Integer settlementTotalFee) {
    //     this.settlementTotalFee = settlementTotalFee;
    // }

    public Integer getCashRefundFee() {
        return cashRefundFee;
    }

    public void setCashRefundFee(Integer cashRefundFee) {
        this.cashRefundFee = cashRefundFee;
    }

    public Integer getCouponRefundFee() {
        return couponRefundFee;
    }

    public void setCouponRefundFee(Integer couponRefundFee) {
        this.couponRefundFee = couponRefundFee;
    }
}