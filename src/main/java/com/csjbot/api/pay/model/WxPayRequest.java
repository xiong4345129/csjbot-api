package com.csjbot.api.pay.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * todo not use
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "xml")
public class WxPayRequest {
    @JacksonXmlProperty(localName = "appid")
    private final String appId;
    @JacksonXmlProperty(localName = "mch_id")
    private final String mchId;
    @JacksonXmlProperty(localName = "device_info")
    private String deviceInfo;
    @JacksonXmlProperty(localName = "nonce_str")
    private final String nonceStr;
    private final String sign;
    @JacksonXmlProperty(localName = "sign_type")
    private String signType;
    private final String body;
    private String detail;
    private String attach;
    @JacksonXmlProperty(localName = "out_trade_no")
    private final String outTradeNo;
    @JacksonXmlProperty(localName = "fee_type")
    private String feeType;
    @JacksonXmlProperty(localName = "total_fee")
    private final Long totalFee;
    @JacksonXmlProperty(localName = "spbill_create_ip")
    private final String spbillCreateIp;
    @JacksonXmlProperty(localName = "time_start")
    private String timeStart;
    @JacksonXmlProperty(localName = "time_expire")
    private String timeExpire;
    @JacksonXmlProperty(localName = "goods_tag")
    private String goodsTag;
    @JacksonXmlProperty(localName = "notify_url")
    private final String notifyUrl;
    @JacksonXmlProperty(localName = "trade_type")
    private final String tradeType;
    @JacksonXmlProperty(localName = "product_id")
    private String productId;
    @JacksonXmlProperty(localName = "limit_pay")
    private String limitPay;
    @JacksonXmlProperty(localName = "open_id")
    private String openId;

    public WxPayRequest(String appId, String mchId, String nonceStr, String sign,
                        String body, String outTradeNo, Long totalFee,
                        String spbillCreateIp, String notifyUrl, String tradeType) {
        this.appId = appId;
        this.mchId = mchId;
        this.nonceStr = nonceStr;
        this.sign = sign;
        this.body = body;
        this.outTradeNo = outTradeNo;
        this.totalFee = totalFee;
        this.spbillCreateIp = spbillCreateIp;
        this.notifyUrl = notifyUrl;
        this.tradeType = tradeType;
    }

    public String getAppId() {
        return appId;
    }

    public String getMchId() {
        return mchId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
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

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getBody() {
        return body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getTradeType() {
        return tradeType;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public static void main(String[] args) throws JsonProcessingException {
        WxPayRequest req = new WxPayRequest(
            "wx2421b1c4370ec43b",
            "10000100",
            "1add1a30ac87aa2db72f57a2375d8fec",
            "0CB01533B8C1EF103065174F50BCA001",
            "APP支付测试",
            "1415659990",
            1l,
            "14.23.150.211",
            "http://wxpay.wxutil.com/pub_v2/com.csjbot.api.pay/notify.v2.php",
            "APP"
        );
        XmlMapper mapper = new XmlMapper();
        String reqStr = mapper.writeValueAsString(req);
        System.out.println(reqStr);
    }
}
