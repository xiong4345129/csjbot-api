package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.WxPayCallback;
import org.apache.ibatis.session.SqlSessionFactory;

public class MyBatisWxPayDBService extends MybatisOrderPayDBService implements WxPayDBService {

    private static final String NAMESPACE = WxPayDBService.class.getName();

    private static String getStatement(String id) {
        return NAMESPACE + "." + id;
    }

    public MyBatisWxPayDBService(SqlSessionFactory factory) {
        super(factory);
    }

    @Override
    public int storePayResult(WxPayCallback callback) {
        return getSqlSession().insert(getStatement("storePayResult"), callback);
    }

    @Override
    public boolean resultExists(String orderId) {
        Integer cnt = getSqlSession().selectOne(getStatement("resultExists"), orderId);
        return cnt == 1;
    }

}
