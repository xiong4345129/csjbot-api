package com.csjbot.pay.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "xml")
public class WxPayResponse {

    @JacksonXmlProperty(localName = "return_code")
    private String returnCode;
    @JacksonXmlProperty(localName = "return_msg")
    private String returnMsg;
    @JacksonXmlProperty(localName = "appid")
    private String appId;
    @JacksonXmlProperty(localName = "mch_id")
    private String mchId;
    @JacksonXmlProperty(localName = "device_info")
    private String deviceInfo;
    @JacksonXmlProperty(localName = "nonce_str")
    private String nonceStr;
    @JacksonXmlProperty(localName = "sign")
    private String sign;
    @JacksonXmlProperty(localName = "result_code")
    private String resultCode;
    @JacksonXmlProperty(localName = "err_code")
    private String errCode;
    @JacksonXmlProperty(localName = "err_code_des")
    private String errCodeDes;
    @JacksonXmlProperty(localName = "trade_type")
    private String tradeType;
    @JacksonXmlProperty(localName = "prepay_id")
    private String prepayId;
    @JacksonXmlProperty(localName = "code_url")
    private String codeUrl;

    // https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1
    // todo undocumented in parameter list but appear in example xml??
    @JacksonXmlProperty(localName = "openid")
    private String openId;
    // todo in request doc not in callback?
    private String attach;

    public String getAttach() {
        return attach;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
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

    public String getNonceStr() {
        return nonceStr;
    }

    public String getSign() {
        return sign;
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

    public String getTradeType() {
        return tradeType;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    // public static void main(String[] args) throws IOException {
    //     String testXml =
    //         "<xml>\n" +
    //             "   <return_code><![CDATA[SUCCESS]]></return_code>\n" +
    //             "   <return_msg><![CDATA[OK]]></return_msg>\n" +
    //             "   <appid><![CDATA[wx2421b1c4370ec43b]]></appid>\n" +
    //             "   <mch_id><![CDATA[10000100]]></mch_id>\n" +
    //             "   <nonce_str><![CDATA[IITRi8Iabbblz1Jc]]></nonce_str>\n" +
    //             "   <openid><![CDATA[oUpF8uMuAJO_M2pxb1Q9zNjWeS6o]]></openid>\n" +
    //             "   <sign><![CDATA[7921E432F65EB8ED0CE9755F0E86D72F]]></sign>\n" +
    //             "   <result_code><![CDATA[SUCCESS]]></result_code>\n" +
    //             "   <prepay_id><![CDATA[wx201411101639507cbf6ffd8b0779950874]]></prepay_id>\n" +
    //             "   <trade_type><![CDATA[JSAPI]]></trade_type>\n" +
    //             "</xml>";
    //     XmlMapper mapper = new XmlMapper();
    //     WxOrderResponse res = mapper.readValue(testXml, WxOrderResponse.class);
    //     System.out.println(res.getReturnCode());
    // }
}
