package com.csjbot.pay.controller;

import java.util.Properties;

public class WxConfig {

    private final Properties prop;

    public WxConfig(Properties prop) {
        this.prop = prop;
    }

    public String getServerIP() {
        String ip = prop.getProperty("server.ip.public");
        if (isEmpty(ip)) throw new NullPointerException("server ip");
        return ip;
    }

    public String getCallbackUrl() {
        String url = prop.getProperty("pay.wx.callback.url");
        if (isEmpty(url)) throw new NullPointerException("callback url");
        return url;
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    private String replaceEmpty(String orig, String alter) {
        return isEmpty(orig) ? alter : orig;
    }

}
