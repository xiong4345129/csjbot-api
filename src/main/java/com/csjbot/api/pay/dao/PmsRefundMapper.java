package com.csjbot.api.pay.dao;

import com.csjbot.api.pay.model.PmsRefund;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component("refundMapper")
@Mapper
public interface PmsRefundMapper {
    int insert(PmsRefund refund);

    int update(PmsRefund refund);

    PmsRefund get(String refundNo);

    int count(String orderId);

    Integer sum(String orderId);

}