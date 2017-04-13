package com.csjbot.api.meals.service;

import com.alibaba.fastjson.JSONObject;

import javax.json.Json;

/**
 * Created by Zhangyangyang on 2017/4/13.
 */
public interface ScsDishServiceDAO {
    //查询全部菜品
    public JSONObject findAllDishInfo();

    //查询所有菜品分类
    public JSONObject findAllDishType();

    //添加菜品分类
    public JSONObject addDishType(JSONObject json);

    //修改菜品分类
    public JSONObject updateDishType(JSONObject json);

    //删除菜品分类
    public JSONObject deleteDishType(JSONObject json);

    //修改菜品信息
    public JSONObject updateFishInfo(JSONObject json);

    //删除菜品信息
    public JSONObject deleteFishInfo(JSONObject json);

    //根据菜品类型查询菜品
    public JSONObject findDishByType(JSONObject json);

    //根据菜品名称查询
    public JSONObject findDishByName(JSONObject json);
}
