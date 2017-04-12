package com.csjbot.api.pay.dao;

import com.csjbot.api.pay.model.PmsPayDetailWx;
import org.springframework.stereotype.Component;

@Component("wxPayMapper")
public interface PmsPayDetailWxMapper {

    int insert(PmsPayDetailWx record);

    int update(PmsPayDetailWx record);

    PmsPayDetailWx get(String id);

    boolean exists(String orderId);
}