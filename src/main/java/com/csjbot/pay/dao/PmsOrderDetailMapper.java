package com.csjbot.pay.dao;

import com.csjbot.pay.model.PmsOrderDetail;
import java.util.List;

import com.csjbot.pay.model.PmsOrderDetailExample;
import org.apache.ibatis.annotations.Param;

public interface PmsOrderDetailMapper {
    long countByExample(PmsOrderDetailExample example);

    int deleteByExample(PmsOrderDetailExample example);

    int insert(PmsOrderDetail record);

    int insertSelective(PmsOrderDetail record);

    List<PmsOrderDetail> selectByExample(PmsOrderDetailExample example);

    int updateByExampleSelective(@Param("record") PmsOrderDetail record, @Param("example") PmsOrderDetailExample example);

    int updateByExample(@Param("record") PmsOrderDetail record, @Param("example") PmsOrderDetailExample example);
}