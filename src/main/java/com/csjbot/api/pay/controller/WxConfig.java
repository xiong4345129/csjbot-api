package com.csjbot.api.pay.controller;

import com.csjbot.api.pay.model.Env;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class WxConfig {

    private static final String K_SERVER_IP = "server.ip.public";
    private static final String K_ENV = "pay.wx.env";
    private static final String K_SANDBOX = "pay.wx.sandbox.key";
    private static final String K_ORDER_URL = "pay.wx.order.url";
    private static final String K_CALLBACK_URL = "pay.wx.callback.url";

    private final Properties prop;

    public WxConfig(Properties prop) {
        this.prop = prop;
    }

    public String getServerIP() {
        String ip = prop.getProperty(K_SERVER_IP);
        if (isEmpty(ip)) throw new NullPointerException(K_SERVER_IP);
        return ip;
    }

    public Env getkEnv() {
        String envName = prop.getProperty(K_ENV);
        if (isEmpty(envName)) {
            return Env.DEV;
        }else {
            return Env.get(envName);
        }
    }

    public boolean isSandboxKey(){
        String s = prop.getProperty(K_SANDBOX);
        return Boolean.parseBoolean(s);
    }

    public String getOrderUrlStr() {
        String url = prop.getProperty(K_ORDER_URL);
        if (isEmpty(url)) throw new NullPointerException(K_ORDER_URL);
        return url;
    }

    public URI getOrderUrl() {
        return getUri(getOrderUrlStr());
    }

    public String getCallbackUrlStr() {
        String url = prop.getProperty(K_CALLBACK_URL);
        if (isEmpty(url)) throw new NullPointerException(K_CALLBACK_URL);
        return url;
    }

    public URI getCallbackUrl() {
        return getUri(getCallbackUrlStr());
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    private String replaceEmpty(String orig, String alter) {
        return isEmpty(orig) ? alter : orig;
    }

    public static URI getUri(String urlStr) {
        URI uri;
        try {
            uri = new URI(urlStr);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            uri = null;
        }
        return uri;
    }
}
