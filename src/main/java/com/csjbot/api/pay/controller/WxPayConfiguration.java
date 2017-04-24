package com.csjbot.api.pay.controller;

import com.csjbot.api.pay.service.WxPayConfig;
import com.csjbot.api.pay.service.WxPayDBService;
import com.csjbot.api.pay.service.WxPayParamName;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.KeyManagerFactory;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;

@Configuration
public class WxPayConfiguration {

    @Autowired
    @Qualifier("wxPayDBService")
    private WxPayDBService dbService;
    @Autowired
    private WxPayConfig config;
    @Autowired
    Netty4ClientHttpRequestFactory factory;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
            .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplateWithCert() {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters()
            .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    @Bean
    Netty4ClientHttpRequestFactory createHttpRequestFactory() throws Exception {
        Netty4ClientHttpRequestFactory factory = new Netty4ClientHttpRequestFactory();
        SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();
        if (config.isRefundEnabled()) {  // todo use environment settings
            final String fileName = config.getValueStrict(WxPayConfig.K_CERT_FILE);
            final String passwd = dbService.getAccount().get(WxPayParamName.K_MCH_ID);
            if (passwd == null) throw new NullPointerException("mch_id");
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream fileIn = new FileInputStream(fileName)) {
                keyStore.load(fileIn, passwd.toCharArray());
            }
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, passwd.toCharArray());
            SslContext sslContext = sslContextBuilder.keyManager(keyManagerFactory).build();
            factory.setSslContext(sslContext);
        }else {
            factory.setSslContext(sslContextBuilder.build());
        }
        return factory;
    }

}
