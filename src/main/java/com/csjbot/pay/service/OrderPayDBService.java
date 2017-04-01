package com.csjbot.pay.service;

import com.csjbot.pay.model.PmsOrderDetail;
import com.csjbot.pay.model.PmsOrderPay;

import java.util.List;
import java.util.Map;

public interface OrderPayDBService {
    Map<String, String> getAccount();

    int newOrder(PmsOrderPay orderPay);

    int insertOrderList(List<PmsOrderDetail> items);

    Integer getUnitPrice(String itemId);

    // int updateUnitPrice(String orderId);
    //
    // Integer calculcateTotalFee(String orderId);
}
