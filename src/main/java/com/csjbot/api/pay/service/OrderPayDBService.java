package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.*;

import java.util.List;
import java.util.Map;

public interface OrderPayDBService {
    Map<String, String> getAccount();

    // int isOrderPaySuccess(String orderId);

    int newOrderPayRecord(PmsOrderPay record);

    int updateOrderPayRecord(PmsOrderPay record);

    PmsOrderPay getOrderPayRecord(String orderId);

    boolean orderPayRecordExists(String orderId);

    boolean orderPayRecordExists(String orderPseudoNo, String orderDeviceId);

    int insertOrderItems(List<PmsOrderItem> items);

    int newRefundRecord(PmsRefund record);

    int updateRefundRecord(PmsRefund record);

    PmsRefund getRefundRecord(String refundNo);

    boolean refundRecordExists(String orderId);

    int getRefundedTotalFee(String orderId);

   Integer getUnitPrice(String itemId);

    int log(PmsOrderPayHttpLog record);
}
