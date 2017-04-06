package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.PmsOrderDetail;
import com.csjbot.api.pay.model.PmsOrderPay;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MybatisOrderPayDBService implements OrderPayDBService {

    private static final String NAMESPACE = OrderPayDBService.class.getName();

    private static String getStatement(String id) {
        return NAMESPACE + "." + id;
    }

    private final SqlSessionTemplate sqlSession;

    public MybatisOrderPayDBService(SqlSessionFactory factory) {
        this.sqlSession = new SqlSessionTemplate(factory);
    }

    @Override
    public Map<String, String> getAccount() {
        List<HashMap<String, String>> res = sqlSession.selectList(getStatement("getAccount"));
        Map<String, String> map = new HashMap<>();
        for (Map m : res) map.put((String) m.get("code"), (String) m.get("value"));
        return map;
    }

    @Override
    public int newOrder(PmsOrderPay orderPay) {
        return sqlSession.insert(getStatement("newOrder"), orderPay);
    }

    @Override
    public int updateOrder(PmsOrderPay orderPay) {
        return sqlSession.update(getStatement("updateOrder"), orderPay);
    }

    @Override
    public int insertOrderList(List<PmsOrderDetail> items) {
        return sqlSession.insert(getStatement("insertOrderList"), items);
    }

    @Override
    public Integer getUnitPrice(String itemId) {
        return sqlSession.selectOne(getStatement("getUnitPrice"), itemId);
    }

    @Override
    public String getOrderStatus(String orderId) {
        return sqlSession.selectOne(getStatement("getOrderStatus"),orderId);
    }

    // @Override
    // public int updateUnitPrice(String orderId) {
    //     return sqlSession.update(getStatement("updateUnitPrice"), orderId);
    // }
    //
    // @Override
    // public Integer calculcateTotalFee(String orderId) {
    //     return sqlSession.selectOne(getStatement("calculateTotalFee"), orderId);
    // }
}
