package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.PmsPayDetailWx;

public interface WxPayDBService extends OrderPayDBService {
    int newWxPayRecord(PmsPayDetailWx record);

    int updateWxPayRecord(PmsPayDetailWx record);

    PmsPayDetailWx getWxPayRecord(String orderId);

    boolean wxPayRecordExists(String orderId);
}
