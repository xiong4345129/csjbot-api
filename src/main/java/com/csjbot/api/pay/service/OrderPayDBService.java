package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.PmsOrderDetail;
import com.csjbot.api.pay.model.PmsOrderPay;

import java.util.List;
import java.util.Map;

public interface OrderPayDBService {
    Map<String, String> getAccount();

    int newOrder(PmsOrderPay orderPay);

    int updateOrder(PmsOrderPay orderPay);

    int insertOrderList(List<PmsOrderDetail> items);

    Integer getUnitPrice(String itemId);

    String getOrderStatus(String orderId);
    // int updateUnitPrice(String orderId);
    //
    // Integer calculcateTotalFee(String orderId);
}
