package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.WxPayDataWrapper;
import com.csjbot.api.pay.model.WxClientOrderRequest;

import java.util.Map;

public interface WxPayDataService {
    boolean checkSign(Map<String, String> params);

    boolean checkSign(Map<String, String> params, String sign);

    String computeSign(Map<String, String> params);

    // todo
    WxPayDataWrapper buildPayData(WxClientOrderRequest clientReq);

}
