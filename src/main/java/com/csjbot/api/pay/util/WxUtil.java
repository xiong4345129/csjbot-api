package com.csjbot.api.pay.util;

import com.csjbot.api.pay.controller.WxPayRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class WxUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxUtil.class);

    public static String computeSign(Map<String, String> map, String key)
        throws NoSuchAlgorithmException {
        return computeSign(map, key, false);
    }

    public static String computeSign(Map<String, String> map, String key, boolean verbose)
        throws NoSuchAlgorithmException {
        TreeMap<String, String> treeMap = new TreeMap<>(map);
        String signSrc = treeMap.entrySet().stream()
            .filter(e -> e.getValue() != null)
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining("&"));
        signSrc = signSrc + "&" + WxPayRequestBuilder.K_API_KEY + "=" + key;
        String sign = ChecksumGen.compute(ChecksumGen.MD5, signSrc, UTF_8).toUpperCase();
        if (verbose) {
            dumpMap("key => " + key, map);
            LOGGER.debug("\nsrc:\n" + signSrc + " " + signSrc.length());
            LOGGER.debug("\nsign:\n" + sign + " " + sign.length());
        }
        return sign;
    }

    public static void dumpMap(String msg, Map<String, String> map) {
        String mapStr = map.entrySet().stream()
            .map(e -> "<" + e.getKey() + ">#" + e.getValue() + "#")
            .collect(Collectors.joining("\n"));
        LOGGER.debug("\n[DUMP MAP]\n" + msg + "\n" + mapStr);
    }

    public static String composeProductId(String orderId, String clientOrderNo) {
        String src = (orderId + clientOrderNo).replace("-", "");
        int srcLen = src.length();
        if (srcLen > 32) src = src.substring(srcLen - 32, srcLen);
        return src;
    }

    public static String makeXml(Map<String, String> map) {
        String body = map.entrySet().stream()
            .filter(e -> e.getValue() != null)
            .map(e -> "<" + e.getKey() + ">" + e.getValue() + "</" + e.getKey() + ">")
            .collect(Collectors.joining(""));
        String xml = "<xml>" + body + "</xml>";
        return xml;
    }

    public static Date convert(LocalDateTime ldt) {
        ZoneId id = ZoneOffset.systemDefault();
        ZonedDateTime zdt = ldt.atZone(id);
        Instant instant = zdt.toInstant();
        Date date = Date.from(instant);
        return date;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Map<String, String> map = new HashMap<>();
        map.put("appid", "wx52bcd8d769364a0c");
        map.put("attach", "201703291745-12");
        map.put("body", "周黑鸭张浦店-食品");
        map.put("mch_id", "1236388002");
        map.put("nonce_str", "KQCA7GLOI9DCXL6TNX2CPIMAC7U8P0TT");
        map.put("notify_url", "http://120.27.233.57:9090/api/com.csjbot.api.pay/wx/callback");
        map.put("out_trade_no", "1491032-7198-000-001-965");
        map.put("product_id", "91032719800000196520170329174512");
        map.put("spbill_create_ip", "120.27.233.57");
        map.put("total_fee", "12300");
        map.put("trade_type", "NATIVE");
        String sign = computeSign(map, WxPayRequestBuilder.SANDBOX_API_KEY, true);
        System.out.println(sign);
    }

    private static void prodIdTest() {
        String orderId = OrderIdGen.next();
        String clienNo1 = "20170329174500-12";
        String clientNo2 = clienNo1 + "34567";
        System.out.println(composeProductId(orderId, clienNo1));
        System.out.println(composeProductId(orderId, clientNo2));
    }
}
