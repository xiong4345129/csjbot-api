package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.PmsOrderItem;
import com.csjbot.api.pay.model.PmsOrderPay;

import java.util.List;
import java.util.Map;

public interface OrderPayDBService {
    Map<String, String> getAccount();

    int newOrder(PmsOrderPay orderPay);

    int updateOrder(PmsOrderPay orderPay);

    int insertOrderList(List<PmsOrderItem> items);

    Integer getUnitPrice(String itemId);

    PmsOrderPay getOrderPayRecord(String orderId);

    boolean orderExists(String orderId);
}
