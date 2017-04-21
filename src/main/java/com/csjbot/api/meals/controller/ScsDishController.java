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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Zhangyangyang on 2017/4/13.
 */
@Controller
public class ScsDishController {
    @Autowired
    private ScsDishServiceDAO scsDishServiceDAO;

    // 查询所有菜品信息
    @RequestMapping(value = "/scs/findAllDishInfo", method = RequestMethod.GET)
    @ResponseBody
    public void findAllDishInfo(HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.findAllDishInfo());
    }

    // 更新菜品信息
    @RequestMapping(value = "/scs/updateDishInfo", method = RequestMethod.POST)
    @ResponseBody
    public void updateDishInfo(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.updateFishInfo(data));
    }

    //删除菜品信息
    @RequestMapping(value = "/scs/deleteDishInfo", method = RequestMethod.POST)
    @ResponseBody
    public void deleteDishInfo(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.deleteFishInfo(data));
    }

    //根据菜品类型查询菜品信息
    @RequestMapping(value = "/scs/findDishInfoByType", method = RequestMethod.POST)
    @ResponseBody
    public void findDishInfoByType(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.findDishByType(data));
    }

    //查询所有菜品类型信息
    @RequestMapping(value = "/scs/findAllDishTypeInfo", method = RequestMethod.GET)
    @ResponseBody
    public void findAllDishTypeInfo( HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.findAllDishType());
    }

    //添加菜品类型信息
    @RequestMapping(value = "/scs/addDishTypeInfo", method = RequestMethod.POST)
    @ResponseBody
    public void addDishTypeInfo(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.addDishType(data));
    }

    //修改菜品类型信息
    @RequestMapping(value = "/scs/updateDishTypeInfo", method = RequestMethod.POST)
    @ResponseBody
    public void updateDishTypeInfo(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.updateDishType(data));
    }

    //删除菜品类型信息
    @RequestMapping(value = "/scs/deleteDishTypeInfo", method = RequestMethod.POST)
    @ResponseBody
    public void deleteDishTypeInfo(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.deleteDishType(data));
    }
    //获得送餐附件信息
    @RequestMapping(value = "/scs/showDishAccessory", method = RequestMethod.GET)
    @ResponseBody
    public void showDishAccessory( HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.showAccessoryS());
    }
    //添加桌位
    @RequestMapping(value = "/scs/addDeskInfo", method = RequestMethod.POST)
    @ResponseBody
    public void addDeskInfo(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.addDeskInfo(data));
    }
    //删除桌位
    @RequestMapping(value = "/scs/deleteDeskInfo", method = RequestMethod.POST)
    @ResponseBody
    public void deleteDeskInfo(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.deleteDeskInfo(data));
    }
    //查询所有桌位
    @RequestMapping(value = "/scs/showAllDeskInfo", method = RequestMethod.GET)
    @ResponseBody
    public void showAllDeskInfo(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int type = Integer.valueOf(request.getParameter("type"));
        ResponseUtil.write(response,scsDishServiceDAO.showAllDeskInfo(type));
    }
    //查询单个桌位
    @RequestMapping(value = "/scs/showDeskInfo", method = RequestMethod.POST)
    @ResponseBody
    public void showDeskInfo(@RequestBody JSONObject data, HttpServletResponse response)
            throws IOException {
        ResponseUtil.write(response,scsDishServiceDAO.showDeskInfo(data));
    }
    //下载文件接口
    @RequestMapping(value = "/scs/downFile", method = RequestMethod.GET)
    @ResponseBody
    public void downFile(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        File file = new File(request.getParameter("filePath")); //要下载的文件绝对路径
        InputStream ins = new BufferedInputStream(new FileInputStream(request.getParameter("filePath")));
        byte [] buffer = new byte[ins.available()];
        ins.read(buffer);
        ins.close();

        response.reset();
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(request.getParameter("fileName").getBytes()));
        response.addHeader("Content-Length", "" + file.length());
        OutputStream ous = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        ous.write(buffer);
        ous.flush();
        ous.close();
    }


}
