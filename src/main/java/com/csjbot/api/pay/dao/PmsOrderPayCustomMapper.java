package com.csjbot.api.pay.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

// todo
@Component("customMapper")
@Mapper
public interface PmsOrderPayCustomMapper {
    List<Map<String, String>> getAccount();

    Integer getUnitPrice(String itemId);


}
