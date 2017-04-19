package com.csjbot.api.pay.service;

import com.csjbot.api.pay.dao.PmsPayDetailWxMapper;
import com.csjbot.api.pay.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("wxPayDBService")
public class WxPayDBServiceImpl extends OrderPayDBServiceImpl implements WxPayDBService {

    @Autowired
    private PmsPayDetailWxMapper wxPayMapper;

    public WxPayDBServiceImpl() {
        System.out.println("init WxPayDBServiceImpl");
    }

    @Override
    public int newWxPayRecord(PmsPayDetailWx record) {
        return wxPayMapper.insert(record);
    }

    @Override
    public int updateWxPayRecord(PmsPayDetailWx record) {
        return wxPayMapper.update(record);
    }

    @Override
    public PmsPayDetailWx getWxPayRecord(String orderId) {
        return wxPayMapper.get(orderId);
    }

    @Override
    public boolean wxPayRecordExists(String orderId) {
        return wxPayMapper.exists(orderId);
    }

    // @Override
    // public boolean newWxPayOrder(WxPayDataWrapper dataWrapper) {
    //     newOrderPayRecord(dataWrapper.getOrderPay());
    //     newWxPayRecord(dataWrapper.getWxDetail());
    //     insertOrderItems(dataWrapper.getItems());
    //     return true;
    // }
    //
    //
    // public boolean newWxPayNewOrder(WxPayDataWrapper dataWrapper) {
    //     newOrderPayRecord(dataWrapper.getOrderPay());
    //     newWxPayRecord(dataWrapper.getWxDetail());
    //     insertOrderItems(dataWrapper.getItems());
    //     return true;
    // }
}
