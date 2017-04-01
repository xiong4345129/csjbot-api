package com.csjbot.pay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JacksonMapper implements MediaTypeParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonMapper.class);

    private final XmlMapper xmlMapper = new XmlMapper();
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public JacksonMapper() {
        jsonMapper
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());
    }

    @Override
    public String serialize(Object object, String type) {
        String outStr = null;
        try {
            switch (type.toUpperCase()) {
                case "JSON":
                    outStr = jsonMapper.writeValueAsString(object);
                    break;
                case "XML":
                    outStr = xmlMapper.writeValueAsString(object);
                    break;
                default:
                    throw new IllegalArgumentException("unknown type " + type);
            }
            return jsonMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error(type + " serialization", e);
        }
        return outStr;
    }

    @Override
    public <T> T deserialize(String src, Class<T> tClass, String type) {
        T outObj = null;
        try {
            switch (type.toUpperCase()) {
                case "JSON":
                    outObj = jsonMapper.readValue(src, tClass);
                    break;
                case "XML":
                    outObj = xmlMapper.readValue(src, tClass);
                    break;
                default:
                    throw new IllegalArgumentException("unknown type " + type);
            }
        } catch (IOException e) {
            LOGGER.error(type + " deserialization " + src, e);
        }
        return outObj;
    }
}
