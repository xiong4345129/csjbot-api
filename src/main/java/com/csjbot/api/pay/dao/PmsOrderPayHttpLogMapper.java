package com.csjbot.api.pay.dao;

import com.csjbot.api.pay.model.PmsOrderPayHttpLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component("httpLogMapper")
@Mapper
public interface PmsOrderPayHttpLogMapper {
    int insert(PmsOrderPayHttpLog record);
}