package com.csjbot.api.meals.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csjbot.api.common.util.FileZipUtil;
import com.csjbot.api.common.util.JsonUtil;
import com.csjbot.api.common.util.RandomUtil;
import com.csjbot.api.meals.dao.Scs_desk_infoDAO;
import com.csjbot.api.meals.dao.Scs_dish_infoDAO;
import com.csjbot.api.meals.dao.Scs_dish_typeDAO;
import com.csjbot.api.meals.model.Scs_desk_info;
import com.csjbot.api.meals.model.Scs_dish_info;
import com.csjbot.api.meals.model.Scs_dish_type;
import com.csjbot.api.robot.dao.Sys_attachmentDAO;
import com.csjbot.api.robot.dao.Ums_userDAO;
import com.csjbot.api.robot.model.Sys_attachment;
import com.csjbot.api.robot.model.Ums_user;
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

    @Autowired
    private Scs_desk_infoDAO desk_infoDAO;

    @Autowired
    private Ums_userDAO ums_userDAO;

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
            List<Sys_attachment> saList = sys_attachmentDAO.getSystByProId(sdi.getId().toString());
            if (sdt != null){
                dish.put("dishTypeId",sdt.getId());
                dish.put("dishTypeName",sdt.getType_name().toString());
            }else {
                dish.put("dishTypeId","");
                dish.put("dishTypeName","未知类型");
            }
            if (saList.size() > 0 ){
                dish.put("dishImageName",saList.get(0).getAlias_name());
                dish.put("dishImageUrl",FileZipUtil.PATH+":8080/api/scs/downFile?filePath="+saList.get(0).getLocation().toString()+"&fileName="+saList.get(0).getAlias_name().toString());
            }else {
                dish.put("dishImageName","");
                dish.put("dishImageUrl","");
            }
            dish.put("dishId",sdi.getId().toString());
            dish.put("dishName",sdi.getName().toString());
            dish.put("dishPrice",sdi.getPrice());
            dish.put("dishMemo",sdi.getMemo().toString());
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
            dish_type.put("dishTypeId",2000+sdt.getId());
            dish_type.put("dishTypeName",sdt.getType_name().toString());
            dishes.add(dish_type);
        }
        jsonUtil.setResult(dishes);
        return JsonUtil.toJson(jsonUtil);
    }

    //添加菜品类型
    @Override
    public JSONObject addDishType(JSONObject json) {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        String[] key = { "dishTypeName" };
        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断菜品是否存在
            Scs_dish_type sdt = scs_dish_typeDAO.findScsDishTypeByName(json.getString("dishTypeName"));
            if (sdt == null){
                sdt = new Scs_dish_type();
                sdt.setCreator_fk(RandomUtil.generateString(32));
                sdt.setUpdater_fk(RandomUtil.generateString(32));
                sdt.setType_name(json.getString("dishTypeName"));
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
        String[] key = { "dishTypeName","newTypeName" };
        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断菜品类型是否存在
            Scs_dish_type sdt = scs_dish_typeDAO.findScsDishTypeByName(json.getString("dishTypeName"));
            if (sdt != null){
                sdt.setType_name(json.getString("newTypeName"));
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
        String[] key = { "dishTypeName" };
        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断菜品类型是否存在
            Scs_dish_type sdt = scs_dish_typeDAO.findScsDishTypeByName(json.getString("dishTypeName"));
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
        String[] key = { "dishName","newTypeName","newDishName","newDishPrice","newDishMemo" };
        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断这个菜品是否存在
            Scs_dish_info sdi = scs_dish_infoDAO.findDishByName(json.getString("dishName"));
            if (sdi != null){
                Scs_dish_type sdt = scs_dish_typeDAO.findScsDishTypeByName(json.getString("newTypeName"));
                if (sdt != null){
                    sdi.setDish_type(sdt.getId());
                }else {
                    sdi.setDish_type(0);
                }
                sdi.setName(json.getString("newDishName"));
                sdi.setPrice(json.getDouble("newDishPrice"));
                sdi.setMemo(json.getString("newDishMemo"));
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
        String[] key = { "dishName"};
        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断这个菜品是否存在
            Scs_dish_info sdi = scs_dish_infoDAO.findDishByName(json.getString("dishName"));

            if (sdi != null){
                List<Sys_attachment> list = sys_attachmentDAO.getSystByProId(sdi.getId());
                if (list.size() > 0){
                     sys_attachmentDAO.delete(list.get(0));
                }
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
        String[] key = { "dishTypeName" };
        List<Object> dishes = new ArrayList<>();
        Map<String,Object> result = new HashMap<>();

        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断菜品类型是否存在
            Scs_dish_type sdt = scs_dish_typeDAO.findScsDishTypeByName(json.getString("dishTypeName"));
            if (sdt != null){
                List<Scs_dish_info> sdi_list = scs_dish_infoDAO.findDishByType(sdt.getId().toString());
                for (Scs_dish_info sdi: sdi_list) {
                    Map<String,Object> dish  = new HashMap<>();
                    List<Sys_attachment> saList = sys_attachmentDAO.getSystByProId(sdi.getId());
                    if (saList.size() > 0){
                        dish.put("dishImageName",saList.get(0).getAlias_name());
                        dish.put("dishImageUrl",saList.get(0).getLocation());
                    } else {
                        dish.put("dishImageUrl","");
                        dish.put("dishImageName","");
                    }
                    dish.put("dishTypeId",sdt.getId());
                    dish.put("dishTypeName",sdt.getType_name().toString());
                    dish.put("dishId",sdi.getId());
                    dish.put("dishName",sdi.getName().toString());
                    dish.put("dishPrice",sdi.getPrice());
                    dish.put("dishMemo",sdi.getMemo().toString());
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
        String[] key = { "dishName" };
        if (CharacterUtil.judgeJsonFormat(key, json)) {
            //判断这个菜品是否存在
            Scs_dish_info sdi = scs_dish_infoDAO.findDishByName(json.getString("dishName"));
            if (sdi != null){
                Scs_dish_type sdt = scs_dish_typeDAO.selectByPrimaryKey(sdi.getDish_type());
                if (sdt == null){
                    dish.put("dishTypeName","未知类型");
                }else {
                    dish.put("dishTypeName",sdt.getType_name().toString());
                }
                dish.put("dishName",sdi.getName().toString());
                dish.put("dishPrice",sdi.getPrice());
                dish.put("dishMemo",sdi.getMemo().toString());
                jsonUtil.setResult(dish);
            }else {
                jsonUtil = getJsonUtilEntity(false);
                jsonUtil.setMessage("The desk does not exist!");
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
            demo.put("fileName",sa.getAlias_name().toString());
            demo.put("fileType",sa.getFile_type().toString());
            demo.put("fileUrl", FileZipUtil.PATH+":8080/api/scs/downFile?filePath="+sa.getLocation().toString()+"&fileName="+sa.getAlias_name().toString());
            ace.add(demo);
        }
        jsonUtil.setResult(ace);
        return JsonUtil.toJson(jsonUtil);
    }

    //添加桌位
    @Override
    public JSONObject addDeskInfo(JSONObject json) {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        String[] key = { "userName","deskNumber","deskAlias","deskMemo","desk_x","desk_y","desk_z","desk_w","desk_v","desk_q","deskValid","deskSerialNumber" };
        if (CharacterUtil.judgeJsonFormat(key, json)) {
           // Scs_desk_info sdi = desk_infoDAO.selectDeskByNubmer(json.getString("deskNumber"));
            Ums_user ums_user = ums_userDAO.findUserByName(json.getString("userName"));
            if (ums_user != null){
                Scs_desk_info sdi = new Scs_desk_info();
                sdi.setId(RandomUtil.generateString(32));
                sdi.setNumber(json.getString("deskNumber"));
                sdi.setAlias(json.getString("deskAlias"));
                sdi.setCreator_fk(ums_user.getId().toString());
                sdi.setDate_create(RandomUtil.getTimeStampFor());
                sdi.setDate_update(RandomUtil.getTimeStampFor());
                sdi.setDeskq(json.getFloat("desk_q"));
                sdi.setDeskv(json.getFloat("desk_v"));
                sdi.setDeskw(json.getFloat("desk_w"));
                sdi.setDeskx(json.getFloat("desk_x"));
                sdi.setDesky(json.getFloat("desk_y"));
                sdi.setDeskz(json.getFloat("desk_z"));
                sdi.setMemo(json.getString("deskMemo"));
                sdi.setValid(json.getShort("deskValid"));
                sdi.setDesk_type(json.getInteger("deskSerialNumber"));
                sdi.setUpdater_fk(ums_user.getId().toString());
                int n = desk_infoDAO.insert(sdi);
                if (n != 1){
                    jsonUtil = getJsonUtilEntity(false);
                    jsonUtil.setMessage("Error from Database operations!");
                }
            }else {
                jsonUtil = getJsonUtilEntity(false);
                jsonUtil.setMessage("The desk already exists!");
            }
        }else {
            jsonUtil = getJsonUtilEntity(false);
            jsonUtil.setMessage("Error from json format!");
        }
        return JsonUtil.toJson(jsonUtil);
    }

    //删除桌位信息
    @Override
    public JSONObject deleteDeskInfo(JSONObject json) {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        String[] key = { "deskNumbers","type"};

        if (CharacterUtil.judgeJsonFormat(key, json)) {
            String[] str = json.getString("deskNumbers").split("&");
           // JSONArray idArray = json.getJSONArray("deskNumbers");
            for (String number: str) {
                List<Scs_desk_info> sdiList = desk_infoDAO.selectDeskByNubmer(number.toString());
                for (Scs_desk_info sdi:sdiList) {
                    if (json.getInteger("type") == -1){
                        if (sdi.getDesk_type() == -1 ){
                            desk_infoDAO.delete(sdi);
                        }
                    }else {
                        if (sdi.getDesk_type() != -1 ){
                            desk_infoDAO.delete(sdi);
                        }
                    }
                }
            }
        }else {
            jsonUtil = getJsonUtilEntity(false);
            jsonUtil.setMessage("Error from json format!");
        }
        return JsonUtil.toJson(jsonUtil);
    }

    //查看所有桌位信息
    @Override
    public JSONObject showAllDeskInfo(Integer desk_type) {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        List<Scs_desk_info> list = desk_infoDAO.selectAll();
        List<Object> result = new ArrayList<>();
        for (Scs_desk_info sdi: list) {
            if (desk_type == -1){
                if (sdi.getDesk_type() == -1){
                    Map<String,Object> map = new HashMap<>();
                    map.put("deskId",sdi.getId().toString());
                    map.put("deskNumber",sdi.getNumber().toString());
                    map.put("deskAlias",sdi.getAlias().toString());
                    map.put("deskMemo",sdi.getMemo().toString());
                    map.put("desk_x",sdi.getDeskx());
                    map.put("desk_y",sdi.getDesky());
                    map.put("desk_z",sdi.getDeskz());
                    map.put("desk_w",sdi.getDeskw());
                    map.put("desk_v",sdi.getDeskv());
                    map.put("desk_q",sdi.getDeskq());
                    map.put("deskValid",sdi.getValid());
                    map.put("deskSerialNumber",sdi.getDesk_type());
                    result.add(map);
                }
            }else {
                if (sdi.getDesk_type() != -1){
                    Map<String,Object> map = new HashMap<>();
                    map.put("deskId",sdi.getId().toString());
                    map.put("deskNumber",sdi.getNumber().toString());
                    map.put("deskAlias",sdi.getAlias().toString());
                    map.put("deskMemo",sdi.getMemo().toString());
                    map.put("desk_x",sdi.getDeskx());
                    map.put("desk_y",sdi.getDesky());
                    map.put("desk_z",sdi.getDeskz());
                    map.put("desk_w",sdi.getDeskw());
                    map.put("desk_v",sdi.getDeskv());
                    map.put("deskValid",sdi.getValid());
                    map.put("desk_q",sdi.getDeskq());
                    map.put("deskSerialNumber",sdi.getDesk_type());
                    result.add(map);
                }
            }
        }
        jsonUtil.setResult(result);
        return JsonUtil.toJson(jsonUtil);
    }

    //查看桌位信息
    @Override
    public JSONObject showDeskInfo(JSONObject json) {
        JsonUtil jsonUtil = getJsonUtilEntity(true);
        String[] key = { "deskNumber"};
        List<Object> list = new ArrayList<>();
        if (CharacterUtil.judgeJsonFormat(key, json)) {
            List<Scs_desk_info> sdiList = desk_infoDAO.selectDeskByNubmer(json.getString("deskNumber"));
            if (sdiList.size() > 0){
                for (Scs_desk_info sdi: sdiList) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("deskId",sdi.getId().toString());
                    map.put("deskNumber",sdi.getNumber().toString());
                    map.put("deskAlias",sdi.getAlias().toString());
                    map.put("deskMemo",sdi.getMemo().toString());
                    map.put("desk_x",sdi.getDeskx());
                    map.put("desk_y",sdi.getDesky());
                    map.put("desk_z",sdi.getDeskz());
                    map.put("desk_w",sdi.getDeskw());
                    map.put("desk_v",sdi.getDeskv());
                    map.put("desk_q",sdi.getDeskq());
                    map.put("deskSerialNumber",sdi.getDesk_type());
                    map.put("deskValid",sdi.getValid());
                    list.add(map);
                }

                jsonUtil.setResult(list);
            }else {
                jsonUtil = getJsonUtilEntity(false);
                jsonUtil.setMessage("Error from json format!");
            }
        }
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
