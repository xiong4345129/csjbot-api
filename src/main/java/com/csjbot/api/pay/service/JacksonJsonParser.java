package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.WxClientOrderRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public class JacksonJsonParser extends JacksonParser {

    private final ObjectMapper jsonMapper;

    public JacksonJsonParser() {
        jsonMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());
    }

    public JacksonJsonParser(ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    protected ObjectMapper getMapper() {
        return jsonMapper;
    }

    @Override
    protected String getType() {
        return "JSON";
    }

    public static void main(String[] args) {
        MediaTypeParser parser = new JacksonJsonParser();
        WxClientOrderRequest request = parser.deserialize(json, WxClientOrderRequest.class);
        System.out.println(request.getData() == null);
    }

    private static final String json = "" +
        "{\n" +
        "    \"data\": {\n" +
        "        \"orderDesc\": \"ZHY-001\",\n" +
        "        \"orderList\": [\n" +
        "            {\n" +
        "                \"objectId\": \"EJGA0KHUYW9CZRXSKI4XJ634YPSWW9A3\",\n" +
        "                \"qty\": 1,\n" +
        "                \"state\": \"REGULAR\"\n" +
        "            }\n" +
        "        ],\n" +
        "        \"orderPseudoNo\": \"20170407104937-1\",\n" +
        "        \"orderTime\": \"2017-04-07T10:49:42+08:00\",\n" +
        "        \"robotModel\": \"yingbin\",\n" +
        "        \"robotUid\": \"722E720FB6EF4e3eBCB3F86FEEEDC0FE\",\n" +
        "        \"venderCode\": \"ZHY\",\n" +
        "        \"venderUser\": \"gxy\"\n" +
        "    },\n" +
        "    \"id\": \"201704071049420002C18024CB4B94dfc8AB0A64817A59075\"\n" +
        "}";
}
