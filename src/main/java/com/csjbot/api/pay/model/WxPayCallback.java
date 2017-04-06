package com.csjbot.api.pay.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class WxPayCallback {

    public String getOrderId() { return outTradeNo; }

    @JacksonXmlProperty(localName = "return_code") private String returnCode;
    @JacksonXmlProperty(localName = "return_msg") private String returnMsg;
    @JacksonXmlProperty(localName = "appid") private String appid;
    @JacksonXmlProperty(localName = "mch_id") private String mchId;
    @JacksonXmlProperty(localName = "device_info") private String deviceInfo;
    @JacksonXmlProperty(localName = "nonce_str") private String nonceStr;
    @JacksonXmlProperty(localName = "sign") private String sign;
    @JacksonXmlProperty(localName = "sign_type") private String signType;
    @JacksonXmlProperty(localName = "result_code") private String resultCode;
    @JacksonXmlProperty(localName = "err_code") private String errCode;
    @JacksonXmlProperty(localName = "err_code_des") private String errCodeDes;
    @JacksonXmlProperty(localName = "openid") private String openid;
    @JacksonXmlProperty(localName = "is_subscribe") private String isSubscribe;
    @JacksonXmlProperty(localName = "trade_type") private String tradeType;
    @JacksonXmlProperty(localName = "bank_type") private String bankType;
    @JacksonXmlProperty(localName = "total_fee") private Integer totalFee;
    @JacksonXmlProperty(localName = "settlement_total_fee") private Integer settlementTotalFee;
    @JacksonXmlProperty(localName = "fee_type") private String feeType;
    @JacksonXmlProperty(localName = "cash_fee") private Integer cashFee;
    @JacksonXmlProperty(localName = "cash_fee_type") private String cashFeeType;
    @JacksonXmlProperty(localName = "coupon_fee") private Integer couponFee;
    @JacksonXmlProperty(localName = "coupon_count") private Integer couponCount;
    @JacksonXmlProperty(localName = "coupon_type_$n") private Integer couponType$N;
    @JacksonXmlProperty(localName = "coupon_id_$n") private String couponId$N;
    @JacksonXmlProperty(localName = "coupon_fee_$n") private Integer couponFee$N;
    @JacksonXmlProperty(localName = "transaction_id") private String transactionId;
    @JacksonXmlProperty(localName = "out_trade_no") private String outTradeNo;
    @JacksonXmlProperty(localName = "attach") private String attach;
    @JacksonXmlProperty(localName = "time_end") private String timeEnd;

    public String getReturnCode() {
        return returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public String getAppid() {
        return appid;
    }

    public String getMchId() {
        return mchId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public String getSignType() {
        return signType;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public String getOpenid() {
        return openid;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public String getTradeType() {
        return tradeType;
    }

    public String getBankType() {
        return bankType;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public Integer getSettlementTotalFee() {
        return settlementTotalFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public Integer getCashFee() {
        return cashFee;
    }

    public String getCashFeeType() {
        return cashFeeType;
    }

    public Integer getCouponFee() {
        return couponFee;
    }

    public Integer getCouponCount() {
        return couponCount;
    }

    public Integer getCouponType$N() {
        return couponType$N;
    }

    public String getCouponId$N() {
        return couponId$N;
    }

    public Integer getCouponFee$N() {
        return couponFee$N;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public String getAttach() {
        return attach;
    }

    public String getTimeEnd() {
        return timeEnd;
    }
}
