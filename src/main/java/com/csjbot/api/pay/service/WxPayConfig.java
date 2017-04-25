package com.csjbot.api.pay.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;

public class WxPayConfig {

    public static final int MIN_MINUTES = 5;

    public static final String K_SERVER_IP = "server.ip.public";

    public static final String K_CERT_FILE = "pay.wx.cert";
    public static final String K_REFUND_ENABLE = "pay.wx.refund.enable";
    public static final String K_EXPIRE_MIN = "pay.wx.time.expire.min";
    public static final String K_SYNC_MIN = "pay.wx.time.sync.min";
    public static final String K_SYNC_SCH_MIN = "pay.wx.time.sync.schedule.min";
    public static final String K_QUEUE_SIZE = "pay.wx.query.queue.size";
    public static final String K_ORDER_URL = "pay.wx.order.url";
    public static final String K_QUERY_URL = "pay.wx.query.url";
    public static final String K_CLOSE_URL = "pay.wx.close.url";
    public static final String K_NOTIFY_URL = "pay.wx.notify.url";
    public static final String K_REFUND_URL = "pay.wx.refund.url";
    public static final String K_REFUND_QUERY_URL = "pay.wx.refund.query.url";


    private final Properties prop;
    private final String serverIp;
    private final int expireMinutes, syncMinutes, scheduleMinutes;
    private final URI orderUrl, queryUrl, closeUrl, notifyUrl, refundUrl, refundQueryUrl;
    private final int queryQueueCapacity;
    private final boolean isRefundEnabled;

    public WxPayConfig(Properties prop) {
        System.out.println("init WxConfig");
        this.prop = prop;
        serverIp = getValueStrict(K_SERVER_IP);
        orderUrl = getUri(getValueStrict(K_ORDER_URL));
        queryUrl = getUri(getValueStrict(K_QUERY_URL));
        closeUrl = getUri(getValueStrict(K_CLOSE_URL));
        notifyUrl = getUri(getValueStrict(K_NOTIFY_URL));
        isRefundEnabled = Boolean.parseBoolean(getValue(K_REFUND_ENABLE));
        if (isRefundEnabled) {
            refundUrl = getUri(getValueStrict(K_REFUND_URL));
            refundQueryUrl = getUri(getValueStrict(K_REFUND_QUERY_URL));
        } else {
            refundUrl = null;
            refundQueryUrl = null;
        }
        Integer expireMinSet = getValueAsInteger(K_EXPIRE_MIN);
        this.expireMinutes = (expireMinSet == null || expireMinSet < MIN_MINUTES) ? 120 : expireMinSet;
        this.syncMinutes = getValueAsInteger(K_SYNC_MIN, MIN_MINUTES);
        this.scheduleMinutes = getValueAsInteger(K_SYNC_SCH_MIN, MIN_MINUTES);
        this.queryQueueCapacity = getValueAsInteger(K_QUEUE_SIZE, 1000);
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
        return getValueAsInteger(key, null);
    }

    public Integer getValueAsInteger(String key, Integer defaultIfNull) {
        String val = prop.getProperty(key);
        return val == null ? defaultIfNull : Integer.parseInt(val);
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

    public boolean isRefundEnabled() {
        return isRefundEnabled;
    }

    public URI getRefundUrl() {
        return refundUrl;
    }

    public URI getRefundQueryUrl() {
        return refundQueryUrl;
    }

    public int getExpireMinutes() {
        return expireMinutes;
    }

    public int getSyncMinutes() {
        return syncMinutes;
    }

    public int getScheduleMinutes() {
        return scheduleMinutes;
    }

    public int getQueryQueueCapacity() {
        return queryQueueCapacity;
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
