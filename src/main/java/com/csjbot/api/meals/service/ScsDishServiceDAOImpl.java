package com.csjbot.api.meals.service;

import com.alibaba.fastjson.JSONObject;
import com.csjbot.api.common.util.JsonUtil;
import com.csjbot.api.common.util.RandomUtil;
import com.csjbot.api.meals.dao.Scs_dish_infoDAO;
import com.csjbot.api.meals.dao.Scs_dish_typeDAO;
import com.csjbot.api.meals.model.Scs_dish_info;
import com.csjbot.api.meals.model.Scs_dish_type;
import com.csjbot.api.robot.dao.Sys_attachmentDAO;
import com.csjbot.api.robot.model.Sys_attachment;
import com.csjbot.api.robot.util.CharacterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhangyangyang on 2017/4/13.
 */
@Service
public class ScsDishServiceDAOImpl implements ScsDishServiceDAO{
    @Autowired
    private Scs_dish_infoDAO scs_dish_infoDAO;

    @Autowired
    private Scs_dish_typeDAO scs_dish_typeDAO;

    @Autowired
    private Sys_attachmentDAO sys_attachmentDAO;

    //查询所有菜品信息
    @Override
    public JSONObject findAllDishInfo() {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        List<Object> dishes = new ArrayList<>();
        Map<String,Object> result = new HashMap<>();
        List<Scs_dish_info> list = scs_dish_infoDAO.selectAll();

        for (Scs_dish_info sdi:list) {
            Map<String,Object> dish  = new HashMap<>();
            Scs_dish_type sdt = scs_dish_typeDAO.selectByPrimaryKey(sdi.getDish_type());
            if (sdt != null){
                dish.put("type_name",sdt.getType_name().toString());
            }else {
                dish.put("type_name","未知类型");
            }

            dish.put("name",sdi.getName().toString());
            dish.put("price",sdi.getPrice());
            dish.put("memo",sdi.getMemo().toString());
            dishes.add(dish);
        }
        result.put("dishes",dishes);
        jsonUtil.setResult(result);
        return JsonUtil.toJson(jsonUtil);
    }

    //查询所有菜品类型信息
    @Override
    public JSONObject findAllDishType() {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        List<Object> dishes = new ArrayList<>();

        List<Scs_dish_type> list = scs_dish_typeDAO.selectAll();
        for (Scs_dish_type sdt:list) {
            Map<String,Object> dish_type = new HashMap<>();
            dish_type.put("id",2000+sdt.getId());
            dish_type.put("name",sdt.getType_name().toString());
            dishes.add(dish_type);
        }
        jsonUtil.setResult(dishes);
        return JsonUtil.toJson(jsonUtil);
    }

    //添加菜品类型
    @Override
    public JSONObject addDishType(JSONObject json) {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        String[] key = { "type_name" };
        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断菜品是否存在
            Scs_dish_type sdt = scs_dish_typeDAO.findScsDishTypeByName(json.getString("type_name"));
            if (sdt == null){
                sdt = new Scs_dish_type();
                sdt.setCreator_fk(RandomUtil.generateString(32));
                sdt.setUpdater_fk(RandomUtil.generateString(32));
                sdt.setType_name(json.getString("type_name"));
                sdt.setDate_update(RandomUtil.getTimeStampFor());
                sdt.setDate_create(RandomUtil.getTimeStampFor());
                int n = scs_dish_typeDAO.insert(sdt);
                if (n != 1){
                    jsonUtil = getJsonUtilEntity(false);
                    jsonUtil.setMessage("Error from Database operations!");
                }
            }else {
                jsonUtil = getJsonUtilEntity(false);
                jsonUtil.setMessage("The dish's type already exists!");
            }
        }else {
            jsonUtil = getJsonUtilEntity(false);
            jsonUtil.setMessage("Error from json format!");
        }
        return JsonUtil.toJson(jsonUtil);
    }

    //更新菜品类型信息
    @Override
    public JSONObject updateDishType(JSONObject json) {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        String[] key = { "type_name","new_name" };
        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断菜品类型是否存在
            Scs_dish_type sdt = scs_dish_typeDAO.findScsDishTypeByName(json.getString("type_name"));
            if (sdt != null){
                sdt.setType_name(json.getString("new_name"));
                int n = scs_dish_typeDAO.updateByPrimaryKey(sdt);
                if (n !=1 ){
                    jsonUtil = getJsonUtilEntity(false);
                    jsonUtil.setMessage("Error from Database operations!");
                }
            }else {
                jsonUtil = getJsonUtilEntity(false);
                jsonUtil.setMessage("The dish's type does not exist!");
            }
        }else {
            jsonUtil = getJsonUtilEntity(false);
            jsonUtil.setMessage("Error from json format!");
        }
        return JsonUtil.toJson(jsonUtil);
    }

    //删除菜品类型信息
    @Override
    public JSONObject deleteDishType(JSONObject json) {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        String[] key = { "type_name" };
        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断菜品类型是否存在
            Scs_dish_type sdt = scs_dish_typeDAO.findScsDishTypeByName(json.getString("type_name"));
            if (sdt != null){
                int n = scs_dish_typeDAO.delete(sdt);
                if (n !=1 ){
                    jsonUtil = getJsonUtilEntity(false);
                    jsonUtil.setMessage("Error from Database operations!");
                }
            }else {
                jsonUtil = getJsonUtilEntity(false);
                jsonUtil.setMessage("The dish's type does not exist!");
            }
        }else {
            jsonUtil = getJsonUtilEntity(false);
            jsonUtil.setMessage("Error from json format!");
        }
        return JsonUtil.toJson(jsonUtil);
    }

    //更新菜品信息
    @Override
    public JSONObject updateFishInfo(JSONObject json) {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        String[] key = { "name","new_type_name","new_name","new_price","new_memo" };
        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断这个菜品是否存在
            Scs_dish_info sdi = scs_dish_infoDAO.findDishByName(json.getString("name"));
            if (sdi != null){
                Scs_dish_type sdt = scs_dish_typeDAO.findScsDishTypeByName(json.getString("new_type_name"));
                if (sdt != null){
                    sdi.setDish_type(sdt.getId());
                }else {
                    sdi.setDish_type(0);
                }
                sdi.setName(json.getString("new_name"));
                sdi.setPrice(json.getDouble("new_price"));
                sdi.setMemo(json.getString("new_memo"));
                int n = scs_dish_infoDAO.updateByPrimaryKey(sdi);
                if (n != 1){
                    jsonUtil = getJsonUtilEntity(false);
                    jsonUtil.setMessage("Error from Database operations!");
                }
            }else {
                jsonUtil = getJsonUtilEntity(false);
                jsonUtil.setMessage("The dish does not exist!");
            }
        }else {
            jsonUtil = getJsonUtilEntity(false);
            jsonUtil.setMessage("Error from json format!");
        }
        return JsonUtil.toJson(jsonUtil);
    }

    //根据菜品名称删除菜品信息
    @Override
    public JSONObject deleteFishInfo(JSONObject json) {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        String[] key = { "name"};
        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断这个菜品是否存在
            Scs_dish_info sdi = scs_dish_infoDAO.findDishByName(json.getString("name"));
            if (sdi != null){
                int n = scs_dish_infoDAO.delete(sdi);
                if (n != 1){
                    jsonUtil = getJsonUtilEntity(false);
                    jsonUtil.setMessage("Error from Database operations!");
                }
            }else {
                jsonUtil = getJsonUtilEntity(false);
                jsonUtil.setMessage("The dish does not exist!");
            }
        }else {
            jsonUtil = getJsonUtilEntity(false);
            jsonUtil.setMessage("Error from json format!");
        }
        return JsonUtil.toJson(jsonUtil);
    }

    //根据菜品类型查询菜品信息
    @Override
    public JSONObject findDishByType(JSONObject json) {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        String[] key = { "type_name" };
        List<Object> dishes = new ArrayList<>();
        Map<String,Object> result = new HashMap<>();

        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断菜品类型是否存在
            Scs_dish_type sdt = scs_dish_typeDAO.findScsDishTypeByName(json.getString("type_name"));
            if (sdt != null){
                List<Scs_dish_info> sdi_list = scs_dish_infoDAO.findDishByType(sdt.getId().toString());
                for (Scs_dish_info sdi: sdi_list) {
                    Map<String,Object> dish  = new HashMap<>();
                    dish.put("type_name",sdt.getType_name().toString());
                    dish.put("name",sdi.getName().toString());
                    dish.put("price",sdi.getPrice());
                    dish.put("memo",sdi.getMemo().toString());
                    dishes.add(dish);
                }
                result.put("dishes",dishes);
                jsonUtil.setResult(result);
            }else {
                jsonUtil = getJsonUtilEntity(false);
                jsonUtil.setMessage("The dish's type does not exist!");
            }
        }else {
            jsonUtil = getJsonUtilEntity(false);
            jsonUtil.setMessage("Error from json format!");
        }
        return JsonUtil.toJson(jsonUtil);
    }

    //根据菜品名称查询菜品信息
    @Override
    public JSONObject findDishByName(JSONObject json) {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        Map<String,Object> dish = new HashMap<>();
        String[] key = { "name" };
        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断这个菜品是否存在
            Scs_dish_info sdi = scs_dish_infoDAO.findDishByName(json.getString("name"));
            if (sdi != null){
                Scs_dish_type sdt = scs_dish_typeDAO.selectByPrimaryKey(sdi.getDish_type());
                if (sdt == null){
                    dish.put("type_name","未知类型");
                }else {
                    dish.put("type_name",sdt.getType_name().toString());
                }
                dish.put("name",sdi.getName().toString());
                dish.put("price",sdi.getPrice());
                dish.put("memo",sdi.getMemo().toString());
                jsonUtil.setResult(dish);
            }else {
                jsonUtil = getJsonUtilEntity(false);
                jsonUtil.setMessage("The dish does not exist!");
            }
        }else {
            jsonUtil = getJsonUtilEntity(false);
            jsonUtil.setMessage("Error from json format!");
        }
        return JsonUtil.toJson(jsonUtil);
    }

    //查询所有附件信息
    @Override
    public JSONObject showAccessoryS() {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        List<Object> ace = new ArrayList<>();
        List<Sys_attachment> list = sys_attachmentDAO.getSystByType("SC_ACCESSORY");
        for (Sys_attachment sa:list) {
            Map<String,Object> demo = new HashMap<>();
            demo.put("file_name",sa.getAlias_name().toString());
            demo.put("file_type",sa.getFile_type().toString());
            demo.put("file_url",sa.getLocation().toString());
            ace.add(demo);
        }
        jsonUtil.setResult(ace);
        return JsonUtil.toJson(jsonUtil);
    }

    //获得返回json
    public JsonUtil getJsonUtilEntity(boolean flag) {
        JsonUtil jsonUtil;
        if (flag) {
            jsonUtil = new JsonUtil("200", "ok", null);
        } else {
            jsonUtil = new JsonUtil("500", "", null);
        }
        return jsonUtil;
    }
}
