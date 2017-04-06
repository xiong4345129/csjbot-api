package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.PmsOrderItem;
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

    protected SqlSessionTemplate getSqlSession() {
        return sqlSession;
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
    public int insertOrderList(List<PmsOrderItem> items) {
        return sqlSession.insert(getStatement("insertOrderList"), items);
    }

    @Override
    public Integer getUnitPrice(String itemId) {
        return sqlSession.selectOne(getStatement("getUnitPrice"), itemId);
    }

    @Override
    public PmsOrderPay getOrderPayRecord(String orderId) {
        return sqlSession.selectOne(getStatement("getOrderPayRecord"), orderId);
    }

    @Override
    public boolean orderExists(String orderId) {
        Integer cnt = sqlSession.selectOne(getStatement("orderExists"), orderId);
        return cnt == 1;
    }

}
