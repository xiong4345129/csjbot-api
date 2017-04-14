package com.csjbot.api.pay.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class WxPayConfig {

    public static final Integer MIN_MINUTES = 5;
    public static final String K_SERVER_IP = "server.ip.public";
    public static final String K_EXPIRE_MIN = "pay.wx.time.expire.min";
    public static final String K_SYNC_MIN = "pay.wx.time.sync.min";
    public static final String K_ORDER_URL = "pay.wx.order.url";
    public static final String K_QUERY_URL = "pay.wx.query.url";
    public static final String K_CLOSE_URL = "pay.wx.close.url";
    public static final String K_NOTIFY_URL = "pay.wx.notify.url";

    public static final Integer EXPIRE_MIN_DEFAULT = 120;

    private final Properties prop;
    private final String serverIp;
    private final Integer expireMinutes, syncMinutes;
    private final URI orderUrl, queryUrl, closeUrl, notifyUrl;

    public WxPayConfig(Properties prop) {
        System.out.println("init WxConfig");
        this.prop = prop;
        serverIp = getValueStrict(K_SERVER_IP);
        orderUrl = getUri(getValueStrict(K_ORDER_URL));
        queryUrl = getUri(getValueStrict(K_QUERY_URL));
        closeUrl = getUri(getValueStrict(K_CLOSE_URL));
        notifyUrl = getUri(getValueStrict(K_NOTIFY_URL));
        Integer expireMinSet = getValueAsInteger(K_EXPIRE_MIN);
        this.expireMinutes = (expireMinSet == null || expireMinSet < MIN_MINUTES) ?
            EXPIRE_MIN_DEFAULT : expireMinSet;
        Integer syncMinSet = getValueAsInteger(K_SYNC_MIN);
        this.syncMinutes = (syncMinSet == null) ? expireMinutes / 2 : syncMinSet;
    }

    public String getValueStrict(String key) {
        String val = prop.getProperty(key);
        if (isEmpty(val)) throw new NullPointerException(key);
        return val;
    }

    public String getValue(String key) {
        return prop.getProperty(key);
    }

    public Integer getValueAsInteger(String key) {
        String val = prop.getProperty(key);
        return val == null ? null : Integer.parseInt(val);
    }

    public String getServerIP() {
        return serverIp;
    }

    public URI getOrderUrl() {
        return orderUrl;
    }

    public URI getQueryUrl() {
        return queryUrl;
    }

    public URI getCloseUrl() {
        return closeUrl;
    }

    public URI getNotifyUrl() {
        return notifyUrl;
    }

    public Integer getExpireMinutes() {
        return expireMinutes;
    }

    public Integer getSyncMinutes() {
        return syncMinutes;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    private static String replaceEmpty(String orig, String alter) {
        return isEmpty(orig) ? alter : orig;
    }

    public static URI getUri(String urlStr) {
        try {
            return new URI(urlStr);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("url parse fail", e);
        }
    }
}
