package com.csjbot.api.pay.service;

import com.csjbot.api.pay.dao.PmsOrderItemMapper;
import com.csjbot.api.pay.dao.PmsOrderPayCustomMapper;
import com.csjbot.api.pay.dao.PmsOrderPayHttpLogMapper;
import com.csjbot.api.pay.dao.PmsOrderPayMapper;
import com.csjbot.api.pay.model.PmsOrderItem;
import com.csjbot.api.pay.model.PmsOrderPay;
import com.csjbot.api.pay.model.PmsOrderPayHttpLog;
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
    public Integer getUnitPrice(String itemId) {
        return customMapper.getUnitPrice(itemId);
    }

    @Override
    public int log(PmsOrderPayHttpLog record) {
        return httpLogMapper.insert(record);
    }

}
