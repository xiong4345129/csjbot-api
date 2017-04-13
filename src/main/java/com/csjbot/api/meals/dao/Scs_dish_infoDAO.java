package com.csjbot.api.meals.dao;

import com.csjbot.api.common.dao.BaseDAO;
import com.csjbot.api.meals.model.Scs_dish_info;

import java.util.List;

/**
 * Created by Zhangyangyang on 2017/4/13.
 */
public interface Scs_dish_infoDAO extends BaseDAO<Scs_dish_info> {
    //根据菜品分类查询菜品【
    public abstract List<Scs_dish_info> findDishByType(String type);

    //根据菜品名称查询
    public  abstract  Scs_dish_info findDishByName(String name);
}
