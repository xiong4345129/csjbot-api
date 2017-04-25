package com.csjbot.api.meals.dao;

import com.csjbot.api.common.dao.BaseDAO;
import com.csjbot.api.meals.model.Scs_dish_type;

/**
 * Created by Zhangyangyang on 2017/4/13.
 */
public interface Scs_dish_typeDAO extends BaseDAO<Scs_dish_type>{
    //根据菜品类型名称查询菜品类型
    public abstract  Scs_dish_type findScsDishTypeByName(String type_name);
}
