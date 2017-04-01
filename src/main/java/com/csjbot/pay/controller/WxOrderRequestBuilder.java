package com.csjbot.pay.controller;

import com.csjbot.pay.model.OrderItem;
import com.csjbot.pay.model.PmsOrderDetail;
import com.csjbot.pay.model.PmsOrderPay;
import com.csjbot.pay.model.WxClientOrderRequest;
import com.csjbot.pay.service.MediaTypeParser;
import com.csjbot.pay.service.OrderPayDBService;
import com.csjbot.pay.util.OrderIdGen;
import com.csjbot.pay.util.RandomGen;
import com.csjbot.util.MD5;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class WxOrderRequestBuilder {
    public static final String API_KEY = "key";
    public static final String K_APPID = "appid";
    public static final String K_MCH_ID = "mch_id";
    public static final String K_DEVICE_INFO = "device_info";
    public static final String K_NONCE_STR = "nonce_str";
    public static final String K_SIGN = "sign";
    public static final String K_SIGN_TYPE = "sign_type";
    public static final String K_BODY = "body";
    public static final String K_DETAIL = "detail";
    public static final String K_ATTACH = "attach";
    public static final String K_OUT_TRADE_NO = "out_trade_no";
    public static final String K_FEE_TYPE = "fee_type";
    public static final String K_TOTAL_FEE = "total_fee";
    public static final String K_SPBILL_CREATE_IP = "spbill_create_ip";
    public static final String K_TIME_START = "time_start";
    public static final String K_TIME_EXPIRE = "time_expire";
    public static final String K_GOODS_TAG = "goods_tag";
    public static final String K_NOTIFY_URL = "notify_url";
    public static final String K_TRADE_TYPE = "trade_type";
    public static final String K_PRODUCT_ID = "product_id";
    public static final String K_LIMIT_PAY = "limit_pay";
    public static final String K_OPENID = "openid";

    private static final String NONCE_STR_SRC =
        RandomGen.ALPHA.toUpperCase() + RandomGen.NUMBER;
    private final String hostIp;
    private final String callbackUrl;
    private final String apiKey, appId, mchId;

    private final OrderPayDBService dbService;

    public WxOrderRequestBuilder(WxConfig config,
                                 OrderPayDBService dbService) {
        this.dbService = dbService;
        Map<String, String> accMap = dbService.getAccount();
        this.apiKey = accMap.get(API_KEY);
        this.appId = accMap.get(K_APPID);
        this.mchId = accMap.get(K_MCH_ID);
        this.hostIp = config.getServerIP();
        this.callbackUrl = config.getCallbackUrl();
    }

    public PmsOrderPay buildData(WxClientOrderRequest clientReq,
                                 WxTradeType tradeTypeEnum) {
        if (WxTradeType.NATIVE == tradeTypeEnum) {
            return buildQrPayRequestData(clientReq);
        } else {
            return null;
        }
    }

    private PmsOrderPay buildQrPayRequestData(WxClientOrderRequest clientReq) {
        final WxClientOrderRequest.Data clientData = clientReq.getData();
        final Date orderTime = convert(clientData.getOrderTime());
        final String nonceStr = RandomGen.randStr(32, NONCE_STR_SRC);
        final String orderId = OrderIdGen.next();
        final List<PmsOrderDetail> details =
            transformOrderDetails(orderId, clientData.getOrderList());
        final Integer totalFee = sumFee(details);
        final String productId =
            composeProductId(orderId, clientData.getOrderPseudoNo());
        final String tradeType = WxTradeType.NATIVE.name();

        final Map<String, String> map = new HashMap<>();
        map.put(K_APPID, appId);
        map.put(K_MCH_ID, mchId);
        map.put(K_NONCE_STR, nonceStr);
        map.put(K_BODY, clientData.getOrderDesc());
        map.put(K_OUT_TRADE_NO, orderId);
        map.put(K_TOTAL_FEE, String.valueOf(totalFee));
        map.put(K_SPBILL_CREATE_IP, hostIp);
        map.put(K_NOTIFY_URL, callbackUrl);
        map.put(K_TRADE_TYPE, tradeType);
        map.put(K_PRODUCT_ID, productId);
        // used in callback
        map.put(K_ATTACH, clientData.getOrderPseudoNo());

        final String sign = computeSign(map, apiKey);
        map.put(K_SIGN, sign);

        PmsOrderPay orderPay = new PmsOrderPay(orderId);
        orderPay.setOrderTime(orderTime);
        orderPay.setOrderDetails(details);
        orderPay.setOrderTotalFee(totalFee);
        orderPay.setOrderStatus(OrderStatus.RECEIVED);
        orderPay.setPayMethod(tradeType);
        orderPay.setPayProductId(productId);
        orderPay.setPayStatus(PayStatus.WAIT);
        orderPay.setRobotUid(clientData.getRobotUid());
        orderPay.setRobotModel(clientData.getRobotModel());

        final String wxReqXml = makeXml(map);
        orderPay.setPayRequestText(wxReqXml);
        orderPay.setMap(map);
        return null;
    }

    // todo bad POJO design!
    public List<PmsOrderDetail> transformOrderDetails(final String orderId, List<OrderItem> itemList) {
        int cnt = itemList.size();
        List<PmsOrderDetail> detailList = new ArrayList<>(cnt);
        for (OrderItem item : itemList) {
            PmsOrderDetail detail = new PmsOrderDetail(orderId, item.getObjectId(), item.getQty());
            Integer unitPrice = dbService.getUnitPrice(item.getObjectId());
            detail.setUnitPrice(unitPrice);
            detailList.add(detail);
        }
        return detailList;
    }

    public Integer sumFee(List<PmsOrderDetail> details) {
        Integer fee = 0;
        for (PmsOrderDetail d : details) {
            fee = fee + d.getItemQty() * d.getUnitPrice();
        }
        return fee;
    }

    private static String computeSign(Map<String, String> map, String key) {
        TreeMap<String, String> treeMap = new TreeMap<>(map);
        String signSrc = treeMap.entrySet().stream()
            .filter(e -> e.getValue() != null)
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining("&"));
        signSrc = signSrc + "&" + API_KEY + "=" + key;
        String sign = MD5.stringToMD5(signSrc).toUpperCase();
        return sign;
    }

    private static String composeProductId(String orderId, String clientOrderNo) {
        String src = (orderId + clientOrderNo).replace("-", "");
        int srcLen = src.length();
        if (srcLen > 32) src = src.substring(srcLen - 32, srcLen);
        return src;
    }

    private String makeXml(Map<String, String> map) {
        String body = map.entrySet().stream()
            .filter(e -> e.getValue() != null)
            .map(e -> "<" + e.getKey() + ">" + e.getValue() + "</" + e.getKey() + ">")
            .collect(Collectors.joining(""));
        String xml = "<xml>" + body + "</xml>";
        return xml;
    }

    private static Date convert(LocalDateTime ldt) {
        ZoneId id = ZoneOffset.systemDefault();
        ZonedDateTime zdt = ldt.atZone(id);
        Instant instant = zdt.toInstant();
        Date date = Date.from(instant);
        return date;
    }

    public static void main(String[] args) {
        Date date = convert(LocalDateTime.now());
    }

    private static void signTest() {
        Map<String, String> map = new HashMap<>();
        map.put("appid", "wxd930ea5d5a258f4f");
        map.put("mch_id", "10000100");
        map.put("device_info", "1000");
        map.put("body", "test");
        map.put("nonce_str", "ibuaiVcKdpRxkhJA");
        String sign = computeSign(map, "192006250b4c09247ec02edce69f6a2d");
        System.out.println(sign);
        System.out.println("9A0A8659F005D6984697E2CA0A9CF3B7".equals(sign));
    }

    private static void prodIdTest() {
        String orderId = OrderIdGen.next();
        String clienNo1 = "20170329174500-12";
        String clientNo2 = clienNo1 + "34567";
        System.out.println(composeProductId(orderId, clienNo1));
        System.out.println(composeProductId(orderId, clientNo2));
    }
}
