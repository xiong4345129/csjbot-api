package com.csjbot.api.pay.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.csjbot.api.pay.service.WxPayParamName.K_API_KEY;
import static com.csjbot.api.pay.service.WxPayParamName.K_SIGN;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class WxPayUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxPayUtil.class);
    private static final String NONCE_STR_SRC =
        RandomGen.ALPHABET.toUpperCase() + RandomGen.NUMBER;

    public static boolean isValidOrderId(String str) {
        return OrderIdGen.check(str);
    }

    public static String newOrderId() {
        return OrderIdGen.next();
    }

    public static String newNonceStr() {
        return RandomGen.randStr(32, NONCE_STR_SRC);
    }

    // todo what is productId and how to create??
    public static String newProductId(String orderId, String clientOrderNo) {
        String src = (orderId + clientOrderNo).replace("-", "");
        int srcLen = src.length();
        if (srcLen > 32) src = src.substring(srcLen - 32, srcLen);
        return src;
    }

    public static boolean checkSign(Map<String, String> params, String key) {
        return checkSign(params, key, params.get(K_SIGN));
    }

    public static boolean checkSign(Map<String, String> params, String key, String sign) {
        String signComputed = computeSign(params, key);
        return sign != null && sign.equals(signComputed);
    }

    public static String computeSign(Map<String, String> params, String key) {
        return computeSign(params, key, false);
    }

    public static String computeSign(Map<String, String> params, String key, boolean verbose) {
        if (params == null || key == null)
            throw new NullPointerException("param map or key is null");
        TreeMap<String, String> treeMap = new TreeMap<>(params);
        String signSrc = treeMap.entrySet().stream()
            // filter out null values and sign entry
            .filter(e -> !(e.getValue() == null || K_SIGN.equals(e.getKey())))
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining("&"));
        signSrc = signSrc + "&" + K_API_KEY + "=" + key;
        String sign = null;
        try {
            sign = ChecksumGen.compute(ChecksumGen.MD5, signSrc, UTF_8).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("computeSign", e);
        }
        // debug
        if (verbose && sign != null) {
            dumpMap(params, "key => " + key);
            LOGGER.debug("\nsrc:\n" + signSrc + " " + signSrc.length());
            LOGGER.debug("\nsign:\n" + sign + " " + sign.length());
        }
        return sign;
    }

    public static void dumpMap(Map<String, String> map) {
        dumpMap(map, null);
    }

    public static void dumpMap(Map<String, String> map, String extraMsg) {
        String mapStr = map.entrySet().stream()
            .map(e -> "<" + e.getKey() + ">#" + e.getValue() + "#")
            .collect(Collectors.joining("\n"));
        LOGGER.debug("\n[DUMP MAP]\n" + extraMsg + "\n" + mapStr);
    }

    /**
     * <pre>
     * 微信时间规则：
     * 1. 格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
     * 2. 标准北京时间，时区为东八区
     * </pre>
     *
     * @param str datetime string in weixin format
     * @return parsed ZonedDateTime
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1">微信统一下单API</a>
     * @see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=4_2">微信参数规定</a>
     */
    public static ZonedDateTime parseDateTime(String str) {
        if (str == null || !str.matches("[0-9]{14}"))
            return null;
        String zdtStr = str.substring(0, 4) +
            "-" + str.substring(4, 6) +
            "-" + str.substring(6, 8) +
            "T" + str.substring(8, 10) +
            ":" + str.substring(10, 12) +
            ":" + str.substring(12, 14) +
            "+08:00"; // zone Beijin
        return ZonedDateTime.parse(zdtStr);
    }

    /**
     * @param zdt ZonedDateTime
     * @return string representation in weixin datetime format
     * @see WxPayUtil#parseDateTime(String)
     */
    public static String formatDateTime(ZonedDateTime zdt) {
        // 2017-04-11T15:53:03.720+08:00[Asia/Shanghai]
        return (zdt == null) ? null :
            zdt.toString().substring(0, 19)
                .replaceAll("[^0-9]", "");
    }

    public static void main(String[] args) throws IOException {
    }

    private static final String xml = "" +
        "<xml><appid><![CDATA[wxfec1f26a067221ae]]></appid>\n" +
        "<attach><![CDATA[201703291745-12]]></attach>\n" +
        "<bank_type><![CDATA[CFT]]></bank_type>\n" +
        "<cash_fee><![CDATA[1]]></cash_fee>\n" +
        "<fee_type><![CDATA[CNY]]></fee_type>\n" +
        "<is_subscribe><![CDATA[N]]></is_subscribe>\n" +
        "<mch_id><![CDATA[1236388002]]></mch_id>\n" +
        "<nonce_str><![CDATA[G9H38D738D8HBRL8CEAQBELS3IE0AZ4U]]></nonce_str>\n" +
        "<openid><![CDATA[o-WsVt5A1Mfr-wcIa9Hv-I_bTD1A]]></openid>\n" +
        "<out_trade_no><![CDATA[1491476-0850-000-001-549]]></out_trade_no>\n" +
        "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
        "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
        "<sign><![CDATA[6F6F5888DD816B73CDA8DF74F362431B]]></sign>\n" +
        "<time_end><![CDATA[20170406185656]]></time_end>\n" +
        "<total_fee>1</total_fee>\n" +
        "<trade_type><![CDATA[NATIVE]]></trade_type>\n" +
        "<transaction_id><![CDATA[4000702001201704066087716755]]></transaction_id>\n" +
        "</xml>";

    // public static String makeXml(Map<String, String> map) {
    //     String body = map.entrySet().stream()
    //         .filter(e -> e.getValue() != null)
    //         .map(e -> "<" + e.getKey() + ">" + e.getValue() + "</" + e.getKey() + ">")
    //         .collect(Collectors.joining(""));
    //     String xml = "<xml>" + body + "</xml>";
    //     return xml;
    // }

    // public static Date convert(ZonedDateTime ldt) {
    //     Instant instant = ldt.toInstant();
    //     Date date = Date.from(instant);
    //     return date;
    // }
}
