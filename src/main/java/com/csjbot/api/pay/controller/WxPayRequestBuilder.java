package com.csjbot.api.pay.controller;

import com.csjbot.api.pay.model.*;
import com.csjbot.api.pay.model.PmsOrderItem;
import com.csjbot.api.pay.service.OrderPayDBService;
import com.csjbot.api.pay.service.WxConfig;
import com.csjbot.api.pay.util.OrderIdGen;
import com.csjbot.api.pay.util.RandomGen;
import com.csjbot.api.pay.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class WxPayRequestBuilder {

    public static final String K_API_KEY = "key";
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

    public static final String SANDBOX_API_KEY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456";

    private static final String NONCE_STR_SRC =
        RandomGen.ALPHA.toUpperCase() + RandomGen.NUMBER;
    private final String hostIp;
    private final String callbackUrl;
    private final String apiKey, appId, mchId;
    private final boolean isSandboxKey;

    private final OrderPayDBService dbService;

    private static final Logger LOGGER = LoggerFactory.getLogger(WxPayRequestBuilder.class);

    public WxPayRequestBuilder(WxConfig config,
                               OrderPayDBService dbService) {
        this.dbService = dbService;
        Map<String, String> accMap = dbService.getAccount();
        this.apiKey = accMap.get(K_API_KEY);
        this.appId = accMap.get(K_APPID);
        this.mchId = accMap.get(K_MCH_ID);
        this.isSandboxKey = config.isSandboxKey();
        this.hostIp = config.getServerIP();
        this.callbackUrl = config.getCallbackUrlStr();
    }

    public PmsOrderPay buildData(WxClientOrderRequest clientReq,
                                 WxTradeType tradeTypeEnum) {
        PmsOrderPay res = null;
        if (WxTradeType.NATIVE == tradeTypeEnum) {
            try {
                res = buildQrPayRequestData(clientReq);
            } catch (Exception e) {
                LOGGER.error("build data", e);
            }
        }
        return res;
    }

    // todo validation
    private PmsOrderPay buildQrPayRequestData(WxClientOrderRequest clientReq) throws Exception {
        final String orderId = OrderIdGen.next();
        final WxClientOrderRequest.Data clientData = clientReq.getData();
        final String orderPseudoNo = clientData.getOrderPseudoNo();
        final Date orderTime = WxUtil.convert(clientData.getOrderTime());
        final String nonceStr = RandomGen.randStr(32, NONCE_STR_SRC);
        final List<PmsOrderItem> details =
            transformOrderDetails(orderId, clientData.getOrderList());
        final Integer totalFee = sumFee(details);
        final String productId =
            WxUtil.composeProductId(orderId, clientData.getOrderPseudoNo());
        final String tradeType = WxTradeType.NATIVE.name();
        final String body = clientData.getOrderDesc();

        Map<String, String> map = prepareParams(orderId, orderPseudoNo,
            body, productId, totalFee, tradeType, nonceStr);
        final String wxReqXml = WxUtil.makeXml(map);

        PmsOrderPay orderPay = new PmsOrderPay(orderId);
        orderPay.setOrderTime(orderTime);
        orderPay.setOrderPseudoNo(orderPseudoNo);
        orderPay.setOrderDetails(details);
        orderPay.setOrderTotalFee(totalFee);
        orderPay.setOrderStatus(OrderStatus.ACCEPT);

        orderPay.setPayService(PayServiceProvider.weixin);
        orderPay.setPayMethod(tradeType);
        orderPay.setPayProductId(productId);
        orderPay.setPayStatus(PayStatus.PRE);

        orderPay.setRobotUid(clientData.getRobotUid());
        orderPay.setRobotModel(clientData.getRobotModel());

        orderPay.setPayRequestText(wxReqXml);
        // client request json will be set in controller

        return orderPay;
    }

    // todo bad POJO design!
    public List<PmsOrderItem> transformOrderDetails(final String orderId, List<OrderItem> itemList) {
        int cnt = itemList.size();
        List<PmsOrderItem> detailList = new ArrayList<>(cnt);
        for (OrderItem item : itemList) {
            PmsOrderItem detail = new PmsOrderItem(orderId, item.getObjectId(), item.getQty());
            Integer unitPrice = dbService.getUnitPrice(item.getObjectId());
            // todo item not exit???
            detail.setUnitPrice(unitPrice == null ? 0 : unitPrice);
            detailList.add(detail);
        }
        return detailList;
    }

    public Integer sumFee(List<PmsOrderItem> details) {
        Integer fee = 0;
        for (PmsOrderItem d : details) {
            fee = fee + d.getItemQty() * d.getUnitPrice();
        }
        return fee;
    }

    private Map<String, String> prepareParams(String orderId, String orderPseudoNo,
                                              String body, String productId, Integer totalFee,
                                              String tradeType, String nonceStr) throws NoSuchAlgorithmException {
        final Map<String, String> map = new TreeMap<>();
        map.put(K_APPID, appId);
        map.put(K_MCH_ID, mchId);
        map.put(K_NONCE_STR, nonceStr);
        map.put(K_BODY, body);
        map.put(K_OUT_TRADE_NO, orderId);
        map.put(K_TOTAL_FEE, String.valueOf(totalFee));
        map.put(K_SPBILL_CREATE_IP, hostIp);
        map.put(K_NOTIFY_URL, callbackUrl);
        map.put(K_TRADE_TYPE, tradeType);
        map.put(K_PRODUCT_ID, productId);
        // if cound be used in callback
        map.put(K_ATTACH, orderPseudoNo);

        // add sandbox test support
        final String key = (isSandboxKey) ? SANDBOX_API_KEY : apiKey;
        final String sign = WxUtil.computeSign(map, key, true);
        map.put(K_SIGN, sign);
        return map;
    }

}
