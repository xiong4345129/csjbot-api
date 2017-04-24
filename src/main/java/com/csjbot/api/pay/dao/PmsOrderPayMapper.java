package com.csjbot.api.pay.dao;

import com.csjbot.api.pay.model.PmsOrderPay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("orderPayMapper")
@Mapper
public interface PmsOrderPayMapper {

    int insert(PmsOrderPay record);

    int update(PmsOrderPay record);

    PmsOrderPay get(String orderId);

    boolean exists(String orderId);

    boolean existsByCandidate(@Param("pseudoNo") String pseudoNo,
                              @Param("deviceId") String deviceId);
}