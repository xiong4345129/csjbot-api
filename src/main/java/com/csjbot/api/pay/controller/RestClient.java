package com.csjbot.api.pay.controller;

import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;

public class RestClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public RestClient() {
        this.restTemplate.getMessageConverters()
            .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

    public ResponseEntity<String> doPost(URI uri,
                                         String body,
                                         MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        RequestEntity<String> req = new RequestEntity<>(body, HttpMethod.POST, uri);
        ResponseEntity<String> res = restTemplate.postForEntity(uri, req, String.class);
        return res;
    }

}
