package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.WxPayCallback;

public interface WxPayDBService extends OrderPayDBService {
    int storePayResult(WxPayCallback callback);

    boolean resultExists(String orderId);
}
