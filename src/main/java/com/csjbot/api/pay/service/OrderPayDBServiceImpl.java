package com.csjbot.api.pay.service;

import com.csjbot.api.pay.dao.*;
import com.csjbot.api.pay.model.PmsOrderItem;
import com.csjbot.api.pay.model.PmsOrderPay;
import com.csjbot.api.pay.model.PmsOrderPayHttpLog;
import com.csjbot.api.pay.model.PmsRefund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("orderPayDBService")
public class OrderPayDBServiceImpl implements OrderPayDBService {

    @Autowired
    private PmsOrderPayMapper orderPayMapper;
    @Autowired
    private PmsOrderItemMapper itemMapper;
    @Autowired
    private PmsOrderPayHttpLogMapper httpLogMapper;
    @Autowired
    private PmsOrderPayCustomMapper customMapper;
    @Autowired
    private PmsRefundMapper refundMapper;

    public OrderPayDBServiceImpl() {
        System.out.println("init OrderPayDBServiceImpl"); // todo initialized how many times ???
    }

    @Override
    public Map<String, String> getAccount() {
        List<Map<String, String>> res = customMapper.getAccount();
        Map<String, String> map = new HashMap<>();
        for (Map m : res) map.put((String) m.get("code"), (String) m.get("value"));
        return map;
    }

    @Override
    public int newOrderPayRecord(PmsOrderPay record) {
        return orderPayMapper.insert(record);
    }

    @Override
    public int updateOrderPayRecord(PmsOrderPay record) {
        return orderPayMapper.update(record);
    }

    @Override
    public PmsOrderPay getOrderPayRecord(String orderId) {
        return orderPayMapper.get(orderId);
    }

    @Override
    public boolean orderPayRecordExists(String orderId) {
        return orderPayMapper.exists(orderId);
    }

    @Override
    public boolean orderPayRecordExists(String orderPseudoNo, String orderDeviceId) {
        return orderPayMapper.existsByCandidate(orderPseudoNo, orderDeviceId);
    }

    @Override
    public int insertOrderItems(List<PmsOrderItem> items) {
        return itemMapper.insertList(items);
    }

    @Override
    public int newRefundRecord(PmsRefund record) {
        return refundMapper.insert(record);
    }

    @Override
    public int updateRefundRecord(PmsRefund record) {
        return refundMapper.update(record);
    }

    @Override
    public PmsRefund getRefundRecord(String refundNo) {
        return refundMapper.get(refundNo);
    }

    @Override
    public boolean refundRecordExists(String orderId) {
        return refundMapper.count(orderId) > 0;
    }

    @Override
    public int getRefundedTotalFee(String orderId) {
        return refundMapper.sum(orderId);
    }

    @Override
    public Integer getUnitPrice(String itemId) {
        return customMapper.getUnitPrice(itemId);
    }

    @Override
    public int log(PmsOrderPayHttpLog record) {
        return httpLogMapper.insert(record);
    }

}
