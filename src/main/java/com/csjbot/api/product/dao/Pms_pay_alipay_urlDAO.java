package com.csjbot.api.product.dao;

import com.csjbot.api.common.dao.BaseDAO;
import com.csjbot.api.product.model.Pms_pay_alipay_url;

/**
 * Created by Zhangyangyang on 2017/4/13.
 */
public interface Pms_pay_alipay_urlDAO extends BaseDAO<Pms_pay_alipay_url>{
    //根据key
    public abstract  Pms_pay_alipay_url findPAUByKey(String key);
}
