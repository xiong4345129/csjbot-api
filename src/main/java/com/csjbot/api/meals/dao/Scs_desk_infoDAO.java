package com.csjbot.api.meals.dao;

import com.csjbot.api.common.dao.BaseDAO;
import com.csjbot.api.meals.model.Scs_desk_info;

import java.util.List;

/**
 * Created by Zhangyangyang on 2017/4/18.
 */
public interface Scs_desk_infoDAO extends BaseDAO<Scs_desk_info>{

    List<Scs_desk_info> selectDeskByNubmer(String number);
}
