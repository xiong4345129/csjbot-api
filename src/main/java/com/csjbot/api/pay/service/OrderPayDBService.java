package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.PmsOrderItem;
import com.csjbot.api.pay.model.PmsOrderPay;
import com.csjbot.api.pay.model.PmsOrderPayHttpLog;

import java.util.List;
import java.util.Map;

public interface OrderPayDBService {
    Map<String, String> getAccount();

    int newOrderPayRecord(PmsOrderPay record);

    int updateOrderPayRecord(PmsOrderPay record);

    PmsOrderPay getOrderPayRecord(String orderId);

    boolean orderPayRecordExists(String orderId);

    int insertOrderItems(List<PmsOrderItem> items);

    Integer getUnitPrice(String itemId);

    int log(PmsOrderPayHttpLog record);
}
