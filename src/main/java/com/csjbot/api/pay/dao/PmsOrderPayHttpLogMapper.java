package com.csjbot.api.pay.dao;

import com.csjbot.api.pay.model.PmsOrderPayHttpLog;
import org.springframework.stereotype.Component;

@Component("httpLogMapper")
public interface PmsOrderPayHttpLogMapper {
    int insert(PmsOrderPayHttpLog record);
}