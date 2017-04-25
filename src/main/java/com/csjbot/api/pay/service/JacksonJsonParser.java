package com.csjbot.api.pay.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public class JacksonJsonParser extends JacksonParser {

    private final ObjectMapper jsonMapper;

    public JacksonJsonParser() {
        jsonMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
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

}
