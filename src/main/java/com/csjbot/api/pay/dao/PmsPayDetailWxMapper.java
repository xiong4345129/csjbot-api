package com.csjbot.api.pay.dao;

import com.csjbot.api.pay.model.PmsPayDetailWx;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component("wxPayMapper")
@Mapper
public interface PmsPayDetailWxMapper {

    int insert(PmsPayDetailWx record);

    int update(PmsPayDetailWx record);

    PmsPayDetailWx get(String id);

    boolean exists(String orderId);
}