package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.WxClientRequest;
import com.csjbot.api.pay.model.WxPayDataWrapper;

import java.util.AbstractMap;
import java.util.Map;

public interface WxPayDataService {
    boolean checkSign(Map<String, String> params);

    boolean checkSign(Map<String, String> params, String sign);

    String computeSign(Map<String, String> params);

    WxPayDataWrapper buildOrderData(WxClientRequest request);

    WxPayDataWrapper buildCloseData(String orderId);

    WxPayDataWrapper buildQueryData(String orderId);

    WxPayDataWrapper buildRefundData(String orderId, Integer refundFee);

    WxPayDataWrapper buildRefundQueryData(String refundNo); // todo
}
