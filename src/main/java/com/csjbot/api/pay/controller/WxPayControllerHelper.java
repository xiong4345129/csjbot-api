package com.csjbot.api.pay.controller;

import com.csjbot.api.pay.model.*;
import com.csjbot.api.pay.service.MediaTypeParser;
import com.csjbot.api.pay.util.WxPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;

import static com.csjbot.api.pay.service.WxPayParamName.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.http.MediaType.TEXT_XML;

// move some get/set, http and static methods from controller...
// todo is model design inefficient?
public class WxPayControllerHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxPayControllerHelper.class);

    @Autowired
    @Qualifier("jsonParser")
    private MediaTypeParser jsonParser;
    @Autowired
    @Qualifier("xmlParser")
    private MediaTypeParser xmlParser;
    @Autowired
    private RestClient restClient;

    static AbstractMap.SimpleEntry<String, String> getQueryId(String orderId, String tranId) {
        AbstractMap.SimpleEntry<String, String> reqIdPair;
        if (tranId != null) {
            reqIdPair = new AbstractMap.SimpleEntry<>(K_TRANSACTION_ID, tranId);
        } else {
            reqIdPair = new AbstractMap.SimpleEntry<>(K_OUT_TRADE_NO, orderId);
        }
        return reqIdPair;
    }

    static PayStatus mapFromTradeState(TradeState state, boolean isExpired) {
        PayStatus payStatus = null;
        switch (state) {
            case SUCCESS:
                payStatus = PayStatus.SUCCESS;
                break;
            case NOTPAY:
                if (isExpired) payStatus = PayStatus.EXPIRE;
                break;
            case CLOSED:
                payStatus = PayStatus.CLOSE;
                break;
            case PAYERROR:
                payStatus = PayStatus.FAIL;
                break;
            default: // todo not handle
        }
        return payStatus;
    }

    static WxClientResponse getQueryClientData(PmsOrderPay orderPay, PmsPayDetailWx wxDetail) {
        WxClientResponse res = new WxClientResponse(orderPay.getOrderId());
        final OrderStatus orderStatus = orderPay.getOrderStatus();
        res.setOrderTime(orderPay.getOrderTime());
        res.setCloseTime(orderPay.getCloseTime());
        res.setOrderTotalFee(orderPay.getOrderTotalFee());
        res.setOrderStatus(orderStatus);
        res.setPayStatus(orderPay.getPayStatus());
        if (OrderStatus.SUCCESS == orderStatus) {
            res.setPayTimeStart(wxDetail.getTimeStart());
            res.setPayTimeExpire(wxDetail.getTimeExpire());
            res.setPayTimeEnd(wxDetail.getTimeEnd());
            res.setPayCashFee(wxDetail.getCashFee());
            res.setPayCouponFee(wxDetail.getCouponFee());
            res.setPayRefundFee(wxDetail.getRefundFee());
            res.setWxOpenId(wxDetail.getOpenid());
            res.setWxTransactionId(wxDetail.getTransactionId());
        }
        return res;
    }

    // todo
    static void fillPayResultData(PmsPayDetailWx wxDetail, Map<String, String> resMap) {
        wxDetail.setOutTradeNo(resMap.get(K_OUT_TRADE_NO));
        wxDetail.setTimeEnd(WxPayUtil.parseDateTime(resMap.get(K_TIME_END)));

        wxDetail.setOpenid(resMap.get(K_OPENID));
        wxDetail.setIsSubscribe(resMap.get(K_IS_SUBSCRIBE));

        wxDetail.setBankType(resMap.get(K_BANK_TYPE));
        wxDetail.setSettlementTotalFee(parseFee(resMap.get(K_SETTLEMENT_TOTAL_FEE)));
        wxDetail.setCashFee(parseFee(resMap.get(K_CASH_FEE)));
        wxDetail.setCashFeeType(resMap.get(K_CASH_FEE_TYPE));
        wxDetail.setCouponFee(parseFee(resMap.get(K_COUPON_FEE)));
        wxDetail.setRefundFee(parseFee(resMap.get(K_REFUND_FEE)));
        wxDetail.setRefundFeeType(resMap.get(K_REFUND_FEE_TYPE));
    }

    public static Integer parseFee(String val) {
        if (val == null || !val.matches("[0-9]+")) return null;
        return Integer.parseInt(val);
    }


    String sendWxPost(URI url, String reqXml) {
        ResponseEntity<String> res = restClient.doPost(url, reqXml, APPLICATION_XML);
        return res.getBody();// wx response status code is always 200
    }

    Map<String, String> deserializeWxXml(String body) {
        return (body == null) ? null : xmlParser.deserializeToMap(body);
    }

    String serializeWxXml(Map<String, String> params) {
        return xmlParser.serialize(params, "xml");
    }

    <T> T deserializeClientJson(String json, Class<T> tClass) {
        return (json == null) ? null : jsonParser.deserialize(json, tClass);
    }


    private static final Map<String, String> CALLBACK_OK = Collections.singletonMap(K_RETURN_CODE, ReStatus.SUCCESS.name());
    private static final Map<String, String> CALLBACK_FAIL = Collections.singletonMap(K_RETURN_CODE, ReStatus.FAIL.name());

    ResponseEntity<String> wxCallbackOkResponse() {
        return wxCallbackResponse(CALLBACK_OK);
    }

    ResponseEntity<String> wxCallbackFailResponse() {
        return wxCallbackResponse(CALLBACK_FAIL);
    }

    ResponseEntity<String> wxCallbackResponse(Map<String, String> map) {
        return textResponse(OK, TEXT_XML, serializeWxXml(map));
    }

    ResponseEntity<String> clientOkResponse(WxClientResponse res) {
        return jsonResponse(OK, res);
    }

    ResponseEntity<String> clientErrResponse(HttpStatus status, String errMsg) {
        return clientErrResponse(status, null, null, errMsg, null);
    }

    ResponseEntity<String> clientErrResponse(HttpStatus status, String orderId, String errMsg, String reqId) {
        return clientErrResponse(status, orderId, null, null, null, errMsg, reqId);
    }

    ResponseEntity<String> clientErrResponse(HttpStatus status, String pseudoNo,
                                             OrderStatus orderStatus, String errMsg, String id) {
        return clientErrResponse(status, null, pseudoNo, orderStatus, null, errMsg, id);
    }

    ResponseEntity<String> clientErrResponse(HttpStatus status, String orderId, String pseudoNo,
                                             OrderStatus orderStatus, PayStatus payStatus,
                                             String errMsg, String id) {
        return clientErrResponse(status, orderId, pseudoNo, orderStatus, payStatus,
            String.valueOf(status.value()), status.getReasonPhrase(), errMsg, id);
    }

    ResponseEntity<String> clientErrResponse(HttpStatus status, String orderId, String pseudoNo,
                                             OrderStatus orderStatus, PayStatus payStatus,
                                             String errCode, String errCodeDesc,
                                             String errMsg, String id) {
        WxClientResponse res = new WxClientResponse();
        res.setOrderId(orderId);
        res.setOrderPseudoNo(pseudoNo);
        res.setOrderStatus(orderStatus);
        res.setPayStatus(payStatus);
        res.setErrCode(errCode);
        res.setErrDesc(errCodeDesc);
        res.setRemark(errMsg);
        res.setId(id);
        return jsonResponse(status, res);
    }

    ResponseEntity<String> clientErrResponse(HttpStatus status, WxClientResponse res) {
        return jsonResponse(status, res);
    }

    ResponseEntity<String> jsonResponse(HttpStatus status, Object obj) {
        return textResponse(status, APPLICATION_JSON, jsonParser.serialize(obj));
    }

    ResponseEntity<String> xmlResponse(HttpStatus status, Object obj) {
        return textResponse(status, APPLICATION_XML, xmlParser.serialize(obj));
    }

    ResponseEntity<String> textResponse(HttpStatus status, MediaType mediaType, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new ResponseEntity<>(body, headers, status);
    }

}
