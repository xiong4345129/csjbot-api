package com.csjbot.api.pay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class JacksonParser implements MediaTypeParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonParser.class);

    private static final TypeReference<HashMap<String, String>> typeRef =
        new TypeReference<HashMap<String, String>>() {};

    protected abstract ObjectMapper getMapper();

    protected abstract String getType();

    @Override
    public String serialize(Object object) {
        String outStr = null;
        try {
            outStr = getMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error(getType() + " serialize", e);
        }
        return outStr;
    }

    @Override
    public String serialize(Object object, String rootName) {
        String outStr = null;
        try {
            outStr = getMapper()
                .writer().withRootName(rootName)
                .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error(getType() + " serialize", e);
        }
        return outStr;
    }

    @Override
    public <T> T deserialize(String src, Class<T> tClass) {
        T outObj = null;
        try {
            outObj = getMapper().readValue(src, tClass);
        } catch (IOException e) {
            logDeErr(src, tClass.getName(), e);
        }
        return outObj;
    }

    @Override
    public Map<String, String> deserializeToMap(String src) {
        Map<String, String> outMap = null;
        try {
            outMap = getMapper().readValue(src, typeRef);
        } catch (IOException e) {
            logDeErr(src, "map", e);
        }
        return outMap;
    }

    private void logDeErr(String src, String cls, Throwable e) {
        LOGGER.error("Fail to deserialize " + getType() + " to " + cls, e);
    }
}
