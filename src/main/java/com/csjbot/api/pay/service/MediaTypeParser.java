package com.csjbot.api.pay.service;

import java.util.Map;

public interface MediaTypeParser {

    String serialize(Object object);

    String serialize(Object object, String rootName);

    <T> T deserialize(String src, Class<T> tClass);

    Map<String, String> deserializeToMap(String src);
}
