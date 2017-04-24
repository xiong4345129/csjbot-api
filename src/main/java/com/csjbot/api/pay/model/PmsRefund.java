package com.csjbot.api.pay.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class PmsRefund implements Serializable {
    /**
     * 后台生成的订单退款流水号，全局唯一，一个order_id可能有多个refund_no
     */
    private String refundNo;

    /**
     * 数据库记录产生时间
     */
    private ZonedDateTime createTime;

    /**
     * 数据库记录更新时间
     */
    private ZonedDateTime updateTime;

    /**
     * 后台生成的正式订单流水号，全局唯一
     */
    private String orderId;

    /**
     * 一笔订单可能分多次退款，该栏位仅为某一笔退款申请的总金额，单位人民币/分取整
     */
    private Integer refundFee;

    /**
     * 退款状态
     */
    private String refundStatus;

    /**
     * 退款错误代码
     */
    private String refundErrCode;

    /**
     * 退款错误描述
     */
    private String refundErrDesc;

    /**
     * 上次向微信查询订单时间
     */
    private ZonedDateTime syncTime;

    /**
     * 该笔退款是否处理完成（结果可能成功或失败）
     */
    private Boolean isClosed;

    /**
     * 退款处理完成时间
     */
    private ZonedDateTime closeTime;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;

    public PmsRefund() {
    }

    public PmsRefund(String orderId, String refundNo, Integer refundFee, RefundStatus refundStatus) {
        this.refundNo = refundNo;
        this.orderId = orderId;
        this.refundFee = refundFee;
        this.refundStatus = refundStatus.name();
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Integer refundFee) {
        this.refundFee = refundFee;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus.name();
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundErrCode() {
        return refundErrCode;
    }

    public void setRefundErrCode(String refundErrCode) {
        this.refundErrCode = refundErrCode;
    }

    public String getRefundErrDesc() {
        return refundErrDesc;
    }

    public void setRefundErrDesc(String refundErrDesc) {
        this.refundErrDesc = refundErrDesc;
    }

    public ZonedDateTime getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(ZonedDateTime syncTime) {
        this.syncTime = syncTime;
    }

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
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