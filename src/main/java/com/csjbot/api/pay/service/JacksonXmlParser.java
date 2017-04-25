package com.csjbot.api.pay.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class JacksonXmlParser extends JacksonParser {

    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    protected ObjectMapper getMapper() {
        return xmlMapper;
    }

    @Override
    protected String getType() {
        return "XML";
    }
}
