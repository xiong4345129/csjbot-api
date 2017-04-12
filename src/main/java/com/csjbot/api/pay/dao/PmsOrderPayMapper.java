package com.csjbot.api.pay.dao;

import com.csjbot.api.pay.model.PmsOrderPay;
import org.springframework.stereotype.Component;

@Component("orderPayMapper")
public interface PmsOrderPayMapper {

    int insert(PmsOrderPay record);

    int update(PmsOrderPay record);

    PmsOrderPay get(String orderId);

    boolean exists(String orderId);
}