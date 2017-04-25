package com.csjbot.api.pay.controller;

import com.csjbot.api.pay.model.*;
import com.csjbot.api.pay.service.MediaTypeParser;
import com.csjbot.api.pay.util.WxPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.csjbot.api.pay.service.WxPayParamName.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.http.MediaType.TEXT_XML;

// move some get/set, http and static methods from controller...
// todo bad mvc structure
public class WxPayControllerHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxPayControllerHelper.class);

    @Autowired
    @Qualifier("jsonParser")
    private MediaTypeParser jsonParser;
    @Autowired
    @Qualifier("xmlParser")
    private MediaTypeParser xmlParser;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RestTemplate restTemplateWithCert;

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
            res.setWxOpenId(wxDetail.getOpenid());
            res.setWxTransactionId(wxDetail.getTransactionId());
        }
        return res;
    }

    // todo
    static void fillPayResultData(PmsPayDetailWx wxDetail, Map<String, String> resMap) {
        wxDetail.setOutTradeNo(resMap.get(K_OUT_TRADE_NO));
        wxDetail.setTransactionId(resMap.get(K_TRANSACTION_ID));
        wxDetail.setTimeEnd(WxPayUtil.parseDateTime(resMap.get(K_TIME_END)));

        wxDetail.setOpenid(resMap.get(K_OPENID));
        wxDetail.setIsSubscribe(resMap.get(K_IS_SUBSCRIBE));

        wxDetail.setBankType(resMap.get(K_BANK_TYPE));
        wxDetail.setSettlementTotalFee(WxPayUtil.parseFee(resMap.get(K_SETTLEMENT_TOTAL_FEE)));
        wxDetail.setCashFee(WxPayUtil.parseFee(resMap.get(K_CASH_FEE)));
        wxDetail.setCashFeeType(resMap.get(K_CASH_FEE_TYPE));
        wxDetail.setCouponFee(WxPayUtil.parseFee(resMap.get(K_COUPON_FEE)));
    }

    // todo imcomplete do not use!
    static List<PmsRefundDetailWx> parseRefundResultData(PmsRefund refund, Map<String, String> resMap) {
        final List<PmsRefundDetailWx> list = new ArrayList<>();
        final String refundNo = refund.getRefundNo();
        final Integer cnt = Integer.parseInt(resMap.get(K_REFUND_COUNT));
        for (int i = 0; i < cnt; i++) {
            PmsRefundDetailWx wxRefund = new PmsRefundDetailWx();
            wxRefund.setRefundNo(refundNo);
            wxRefund.setRefundCount(cnt);
            wxRefund.setRefundIdSn(i);
            wxRefund.setRefundId(resMap.get(getNthKey(K_REFUND_ID, i)));
            wxRefund.setRefundChannel(resMap.get(getNthKey(K_REFUND_CHANNEL, i)));
            wxRefund.setRefundAccount(resMap.get(getNthKey(K_REFUND_ACCOUNT, i)));
            wxRefund.setRefundRecvAccout(resMap.get(getNthKey(K_REFUND_RECV_ACCOUT,i)));
            wxRefund.setRefundStatus(resMap.get(getNthKey(K_REFUND_STATUS, i)));
            wxRefund.setRefundSuccessTime(WxPayUtil.parseDateTime(resMap.get(getNthKey(K_REFUND_SUCCESS_TIME, i))));
            wxRefund.setRefundFee(WxPayUtil.parseFee(resMap.get(getNthKey(K_REFUND_FEE, i))));
            list.add(wxRefund);
        }
        return list;
    }

    private String doPost(RestTemplate template, URI url, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(TEXT_XML);
        RequestEntity<String> req = new RequestEntity<>(body, headers, HttpMethod.POST, url);
        return template.postForObject(url, req, String.class);
    }

    String sendWxPost(URI url, String reqXml) {
        return doPost(restTemplate, url, reqXml);
    }

    String sendWxPostWithCert(URI url, String reqXml) {
        return doPost(restTemplateWithCert, url, reqXml);
    }

    Map<String, String> deserializeWxXml(String body) {
        return (body == null) ? null : xmlParser.deserializeToMap(body);
    }

    String serializeWxXml(Map<String, String> params) {
        return xmlParser.serialize(params, "xml");
    }

    WxClientRequest deserializeClientJson(String json) {
        return (json == null) ? null : jsonParser.deserialize(json, WxClientRequest.class);
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
        return clientErrResponse(status, res);
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
