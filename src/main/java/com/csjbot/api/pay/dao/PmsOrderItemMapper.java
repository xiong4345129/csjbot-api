package com.csjbot.api.pay.dao;

import com.csjbot.api.pay.model.PmsOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("itemMapper")
@Mapper
public interface PmsOrderItemMapper {
    int insertList(List<PmsOrderItem> items);
}
