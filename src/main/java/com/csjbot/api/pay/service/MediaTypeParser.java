package com.csjbot.api.pay.service;

public interface MediaTypeParser {

    String serialize(Object object, String type);

    <T> T deserialize(String src, Class<T> tClass, String type);
}
