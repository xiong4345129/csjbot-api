package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.OrderPayOp;
import com.csjbot.api.pay.model.PmsPayDetailWx;
import com.csjbot.api.pay.model.WxPayDataWrapper;

import java.net.URI;

public interface WxPayDBService extends OrderPayDBService {
    int newWxPayRecord(PmsPayDetailWx record);

    int updateWxPayRecord(PmsPayDetailWx record);

    PmsPayDetailWx getWxPayRecord(String orderId);

    boolean wxPayRecordExists(String orderId);

    // boolean newWxPayOrder(WxPayDataWrapper dataWrapper);
}
