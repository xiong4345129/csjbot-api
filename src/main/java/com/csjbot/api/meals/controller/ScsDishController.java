package com.csjbot.api.meals.controller;

import com.alibaba.fastjson.JSONObject;
import com.csjbot.api.common.util.ResponseUtil;
import com.csjbot.api.meals.service.ScsDishServiceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Zhangyangyang on 2017/4/13.
 */
@Controller
public class ScsDishController {
    @Autowired
    private ScsDishServiceDAO scsDishServiceDAO;

    // 查询所有菜品信息
    @RequestMapping(value = "api/scs/findAllDishInfo", method = RequestMethod.GET)
    @ResponseBody
    public void findAllDishInfo(HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.findAllDishInfo());
    }

    // 更新菜品信息
    @RequestMapping(value = "api/scs/updateDishInfo", method = RequestMethod.POST)
    @ResponseBody
    public void updateDishInfo(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.updateFishInfo(data));
    }

    //删除菜品信息
    @RequestMapping(value = "api/scs/deleteDishInfo", method = RequestMethod.POST)
    @ResponseBody
    public void deleteDishInfo(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.deleteFishInfo(data));
    }

    //根据菜品类型查询菜品信息
    @RequestMapping(value = "api/scs/findDishInfoByType", method = RequestMethod.POST)
    @ResponseBody
    public void findDishInfoByType(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.findDishByType(data));
    }

    //查询所有菜品类型信息
    @RequestMapping(value = "api/scs/findAllDishTypeInfo", method = RequestMethod.GET)
    @ResponseBody
    public void findAllDishTypeInfo( HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.findAllDishType());
    }

    //添加菜品类型信息
    @RequestMapping(value = "api/scs/addDishTypeInfo", method = RequestMethod.POST)
    @ResponseBody
    public void addDishTypeInfo(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.addDishType(data));
    }

    //修改菜品类型信息
    @RequestMapping(value = "api/scs/updateDishTypeInfo", method = RequestMethod.POST)
    @ResponseBody
    public void updateDishTypeInfo(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.updateDishType(data));
    }

    //删除菜品类型信息
    @RequestMapping(value = "api/scs/deleteDishTypeInfo", method = RequestMethod.POST)
    @ResponseBody
    public void deleteDishTypeInfo(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.deleteDishType(data));
    }

}