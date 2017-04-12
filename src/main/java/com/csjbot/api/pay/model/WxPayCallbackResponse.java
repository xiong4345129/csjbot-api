package com.csjbot.api.pay.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class WxPayCallbackResponse {
    @JacksonXmlProperty(localName = "return_code")
    private final String returnCode;
    @JacksonXmlProperty(localName = "return_msg")
    private String returnMsg;

    public WxPayCallbackResponse(ReturnStatus status) {
        this.returnCode = status.name();
    }

    public WxPayCallbackResponse(ReturnStatus status, String returnMsg) {
        this.returnCode = status.name();
        this.returnMsg = returnMsg;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
}
