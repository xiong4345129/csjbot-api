package com.csjbot.pay.dao;

import com.csjbot.pay.model.PmsOrderPay;
import com.csjbot.pay.model.PmsOrderPayExample;
import com.csjbot.pay.model.PmsOrderPayWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsOrderPayMapper {
    long countByExample(PmsOrderPayExample example);

    int deleteByExample(PmsOrderPayExample example);

    int deleteByPrimaryKey(String orderId);

    int insert(PmsOrderPayWithBLOBs record);

    int insertSelective(PmsOrderPayWithBLOBs record);

    List<PmsOrderPayWithBLOBs> selectByExampleWithBLOBs(PmsOrderPayExample example);

    List<PmsOrderPay> selectByExample(PmsOrderPayExample example);

    PmsOrderPayWithBLOBs selectByPrimaryKey(String orderId);

    int updateByExampleSelective(@Param("record") PmsOrderPayWithBLOBs record, @Param("example") PmsOrderPayExample example);

    int updateByExampleWithBLOBs(@Param("record") PmsOrderPayWithBLOBs record, @Param("example") PmsOrderPayExample example);

    int updateByExample(@Param("record") PmsOrderPay record, @Param("example") PmsOrderPayExample example);

    int updateByPrimaryKeySelective(PmsOrderPayWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PmsOrderPayWithBLOBs record);

    int updateByPrimaryKey(PmsOrderPay record);
}