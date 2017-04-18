package com.csjbot.api.meals.service;

import com.alibaba.fastjson.JSONObject;

import javax.json.Json;
import java.util.List;

/**
 * Created by Zhangyangyang on 2017/4/13.
 */
public interface ScsDishServiceDAO {
    //查询全部菜品
    JSONObject findAllDishInfo();

    //查询所有菜品分类
    JSONObject findAllDishType();

    //添加菜品分类
     JSONObject addDishType(JSONObject json);

    //修改菜品分类
     JSONObject updateDishType(JSONObject json);

    //删除菜品分类
     JSONObject deleteDishType(JSONObject json);

    //修改菜品信息
     JSONObject updateFishInfo(JSONObject json);

    //删除菜品信息
     JSONObject deleteFishInfo(JSONObject json);

    //根据菜品类型查询菜品
     JSONObject findDishByType(JSONObject json);

    //根据菜品名称查询
     JSONObject findDishByName(JSONObject json);

    //查询所有附件信息
     JSONObject showAccessoryS();

    //添加桌位
    JSONObject addDeskInfo(JSONObject json);

    //删除多个桌位
    JSONObject deleteDeskInfo(JSONObject json);

    //查看桌位信息
    JSONObject showAllDeskInfo();

    JSONObject showDeskInfo(JSONObject json);

}
