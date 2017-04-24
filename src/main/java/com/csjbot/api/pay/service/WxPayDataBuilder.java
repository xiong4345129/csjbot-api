package com.csjbot.api.pay.service;

import com.csjbot.api.pay.model.WxPayDataWrapper;
import com.csjbot.api.pay.model.*;
import com.csjbot.api.pay.util.WxPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

import static com.csjbot.api.pay.service.WxPayParamName.*;

@Service("wxDataService")
public class WxPayDataBuilder implements WxPayDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxPayDataBuilder.class);

    private final String hostIp;
    private final String callbackUrl;
    private final String apiKey, appId, mchId;
    private final Integer expireMin;
    private final WxPayDBService dbService;

    @Autowired
    public WxPayDataBuilder(WxPayConfig config,
                            @Qualifier("wxPayDBService") WxPayDBService dbService) {
        System.out.println("init WxPayDataBuilder");
        this.callbackUrl = config.getValueStrict(WxPayConfig.K_NOTIFY_URL);
        this.hostIp = config.getServerIP();
        this.expireMin = config.getExpireMinutes();
        if (dbService == null) throw new NullPointerException("dbService");
        this.dbService = dbService;
        Map<String, String> accMap = dbService.getAccount();
        this.apiKey = accMap.get(K_API_KEY);
        this.appId = accMap.get(K_APPID);
        this.mchId = accMap.get(K_MCH_ID);
        if (apiKey == null || appId == null || mchId == null)
            throw new NullPointerException("wx secrets");
    }

    @Override
    public boolean checkSign(Map<String, String> params) {
        return WxPayUtil.checkSign(params, apiKey);
    }

    @Override
    public boolean checkSign(Map<String, String> params, String sign) {
        return WxPayUtil.checkSign(params, apiKey, sign);
    }

    @Override
    public String computeSign(Map<String, String> params) {
        return WxPayUtil.computeSign(params, apiKey, false);
    }

    private Map<String, String> createInitialParams() {
        Map<String, String> params = new TreeMap<>();
        params.put(K_APPID, appId);
        params.put(K_MCH_ID, mchId);
        params.put(K_NONCE_STR, WxPayUtil.newNonceStr());
        return params;
    }


    private WxPayDataWrapper computeSignAndWrapData(Map<String, String> params) {
        // remove empty entries
        params.values().removeIf(s -> s == null || s.trim().length() == 0);
        // compute sign
        final String sign = computeSign(params);
        params.put(K_SIGN, sign);
        return new WxPayDataWrapper(params);
    }

    private void ensureNotNull(Object obj) {
        if (obj == null) throw new NullPointerException();
    }

    @Override
    public WxPayDataWrapper buildQueryData(String orderId) {
        ensureNotNull(orderId);
        Map<String, String> params = createInitialParams();
        PmsPayDetailWx wxDetail = dbService.getWxPayRecord(orderId);
        ensureNotNull(wxDetail);
        String tranId = wxDetail.getTransactionId();
        if (tranId != null) {
            params.put(K_TRANSACTION_ID, tranId);
        } else {
            params.put(K_OUT_TRADE_NO, orderId);
        }
        return computeSignAndWrapData(params);
    }

    @Override
    public WxPayDataWrapper buildRefundData(String orderId, Integer refundFee) {
        ensureNotNull(orderId);
        PmsPayDetailWx wxDetail = dbService.getWxPayRecord(orderId);
        ensureNotNull(wxDetail);

        final String refundNo = WxPayUtil.newRefundNo();

        Map<String, String> params = createInitialParams();
        params.put(K_TRANSACTION_ID, wxDetail.getTransactionId());
        // whether refund fee is valid should be checked before calling this builder!
        params.put(K_TOTAL_FEE, String.valueOf(wxDetail.getTotalFee()));
        params.put(K_REFUND_FEE, String.valueOf(refundFee));
        params.put(K_OP_USER_ID, mchId);
        params.put(K_OUT_REFUND_NO, refundNo);
        WxPayDataWrapper wrapper = computeSignAndWrapData(params);
        wrapper.setRefund(new PmsRefund(orderId, refundNo, refundFee, RefundStatus.PRE));
        // wrapper.setWxRefund(new PmsRefundDetailWx(refundNo));
        return wrapper;
    }

    @Override
    public WxPayDataWrapper buildRefundQueryData(String refundNo) {
        ensureNotNull(refundNo);
        Map<String, String> params = createInitialParams();
        params.put(K_OUT_REFUND_NO, refundNo);
        return computeSignAndWrapData(params);
    }

    @Override
    public WxPayDataWrapper buildCloseData(String orderId) {
        ensureNotNull(orderId);
        Map<String, String> map = createInitialParams();
        map.put(K_OUT_TRADE_NO, orderId);
        return computeSignAndWrapData(map);
    }

    @Override
    public WxPayDataWrapper buildOrderData(WxClientRequest orderReq) {
        ensureNotNull(orderReq);
        WxPayDataWrapper res = new WxPayDataWrapper(true);
        WxTradeType tradeType = orderReq.getPayMethod();
        if (WxTradeType.NATIVE == tradeType) {
            res = buildQrPayData(orderReq);
        }
        return res;
    }

    private WxPayDataWrapper buildQrPayData(WxClientRequest clientReq) {
        final String orderId = WxPayUtil.newOrderId();
        final String orderPseudoNo = clientReq.getOrderPseudoNo();
        final ZonedDateTime orderTime = clientReq.getOrderTime();
        final List<PmsOrderItem> items = sortItems(orderId, clientReq.getOrderList());
        final Integer totalFee = sumFee(items);
        final String productId = WxPayUtil.newProductId(orderId, clientReq.getOrderPseudoNo());
        final String tradeType = WxTradeType.NATIVE.name();
        final String body = clientReq.getOrderDesc();
        final ZonedDateTime timeStart = ZonedDateTime.now();
        final ZonedDateTime timeExpire = timeStart.plusMinutes(expireMin);

        final Map<String, String> params = createInitialParams();
        params.put(K_BODY, body);
        params.put(K_OUT_TRADE_NO, orderId);
        params.put(K_TOTAL_FEE, String.valueOf(totalFee));
        params.put(K_SPBILL_CREATE_IP, hostIp);
        params.put(K_NOTIFY_URL, callbackUrl);
        params.put(K_TRADE_TYPE, tradeType);
        params.put(K_PRODUCT_ID, productId);
        params.put(K_TIME_START, WxPayUtil.formatDateTime(timeStart));
        params.put(K_TIME_EXPIRE, WxPayUtil.formatDateTime(timeExpire));
        // if cound be used in callback
        params.put(K_ATTACH, orderPseudoNo);

        WxPayDataWrapper data = computeSignAndWrapData(params);
        data.setOrderPay(newOrderPay(orderId, orderTime, orderPseudoNo,
            clientReq.getRobotUid(), clientReq.getRobotModel(), totalFee));
        data.setWxDetail(newWxDetail(orderId, WxTradeType.NATIVE,
            productId, totalFee, timeStart, timeExpire));
        data.setItems(items);
        return data;
    }

    private PmsOrderPay newOrderPay(String orderId, ZonedDateTime orderTime, String orderPseudoNo,
                                    String deviceId, String deviceGroup, int totalFee) {
        PmsOrderPay orderPay = new PmsOrderPay(orderId);
        orderPay.setOrderTime(orderTime);
        orderPay.setOrderPseudoNo(orderPseudoNo);
        orderPay.setOrderDeviceId(deviceId);
        orderPay.setOrderDeviceGroup(deviceGroup);
        orderPay.setOrderTotalFee(totalFee);
        orderPay.setOrderStatus(OrderStatus.ACCEPT);
        orderPay.setPayService(PayServiceProvider.weixin);
        orderPay.setPayStatus(PayStatus.PRE);
        return orderPay;
    }

    private PmsPayDetailWx newWxDetail(String orderId, WxTradeType tradeType,
                                       String productId, int totalFee,
                                       ZonedDateTime timeStart, ZonedDateTime timeExpire) {
        PmsPayDetailWx wxDetail = new PmsPayDetailWx(orderId);
        wxDetail.setOutTradeNo(orderId);
        wxDetail.setProductId(productId);
        wxDetail.setTradeType(tradeType);
        wxDetail.setSpbillCreateIp(hostIp);
        wxDetail.setTotalFee(totalFee);
        wxDetail.setTimeStart(timeStart);
        wxDetail.setTimeExpire(timeExpire);
        return wxDetail;
    }

    // todo
    private List<PmsOrderItem> sortItems(String orderId, List<WxClientOrderItem> rawItems) {
        final List<PmsOrderItem> pmsItems = new ArrayList<>(rawItems.size());
        for (WxClientOrderItem rawItem : rawItems) {
            String itemId = rawItem.getItemId();
            Integer unitPrice = dbService.getUnitPrice(itemId);
            if (unitPrice != null) {
                pmsItems.add(new PmsOrderItem(orderId, itemId, rawItem.getItemQty(), unitPrice));
            } else {
                LOGGER.error("no price defined for " + itemId);
            }
        }
        return pmsItems;
    }

    private Integer sumFee(List<PmsOrderItem> details) {
        Integer fee = 0;
        for (PmsOrderItem d : details) {
            fee = fee + d.getItemQty() * d.getUnitPrice();
        }
        return fee;
    }

}
