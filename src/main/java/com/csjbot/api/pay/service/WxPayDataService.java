package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.OrderPayOp;
import com.csjbot.api.pay.model.WxPayDataWrapper;

import java.util.Map;

public interface WxPayDataService {
    boolean checkSign(Map<String, String> params);

    boolean checkSign(Map<String, String> params, String sign);

    String computeSign(Map<String, String> params);

    // todo
    <T> WxPayDataWrapper buildData(T request, OrderPayOp operation);

}
