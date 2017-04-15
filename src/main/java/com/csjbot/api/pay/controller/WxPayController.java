package com.csjbot.api.pay.controller;

import com.csjbot.api.pay.model.*;
import com.csjbot.api.pay.service.*;
import com.csjbot.api.pay.util.WxPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;

import static com.csjbot.api.pay.model.ReStatus.isSuccess;
import static com.csjbot.api.pay.service.WxPayParamName.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/pay/wx")
public class WxPayController {
    // @RequestMapping(produces = "text/plain;charset=UTF-8")
    @RequestMapping
    @ResponseStatus(OK)
    public String hello() {
        LOGGER.debug(System.getProperty("file.encoding"));
        LOGGER.debug(Charset.defaultCharset().name());
        String hello =  "hello! file.encoding is " + System.getProperty("file.encoding") +
            "\n你好，中文是否正常显示？";
        LOGGER.debug(hello);
        return hello;
    }

    @RequestMapping("/echo")
    @ResponseStatus(OK)
    public String echo(@RequestParam("word") String echo) {
        return echo;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(WxPayController.class);

    // todo
    private final WxPayConfig config;

    private final WxPayDBService dbService;
    private final WxPayDataService dataBuilder;
    private final MediaTypeParser jsonParser;
    private final MediaTypeParser xmlParser;
    private final RestClient restClient;

    @Autowired
    public WxPayController(WxPayConfig wxPayConfig,
                           @Qualifier("wxPayDBService") WxPayDBService wxPayDBService,
                           @Qualifier("wxDataService") WxPayDataService dataBuilder,
                           @Qualifier("jsonParser") MediaTypeParser jsonParser,
                           @Qualifier("xmlParser") MediaTypeParser xmlParser,
                           RestClient restClient) {
        System.out.println("init WxPayController");
        this.config = wxPayConfig;
        this.jsonParser = jsonParser;
        this.xmlParser = xmlParser;

        this.dbService = wxPayDBService;
        this.dataBuilder = dataBuilder;
        this.restClient = restClient;
    }

    private String serializeWxXml(Map<String, String> params) {
        return xmlParser.serialize(params, "xml");
    }

    private ResponseEntity<String> sendWxPost(URI url, String reqXml) {
        return restClient.doPost(url, reqXml, APPLICATION_XML);
    }

    /**
     * 二维码支付下单请求：设备->后台
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> orderQR(@RequestBody String orderReqBody) {
        LOGGER.debug("order request " + orderReqBody);
        WxClientRequest orderReq =
            jsonParser.deserialize(orderReqBody, WxClientRequest.class);
        if (orderReq == null)
            return clientErrResponse(BAD_REQUEST, "JSON解析失败");
        final String reqId = orderReq.getId();
        final String pseudoNo = orderReq.getOrderPseudoNo();
        final String deviceId = orderReq.getRobotUid();
        if (dbService.orderPayRecordExists(pseudoNo, deviceId))
            return clientErrResponse(BAD_REQUEST, pseudoNo, null, "pseudoNo已使用", reqId);
        WxPayDataWrapper wxDataWrapper = dataBuilder.buildOrderData(orderReq);
        if (wxDataWrapper.isEmpty())
            return clientErrResponse(BAD_REQUEST, pseudoNo, OrderStatus.FAIL,
                "获取下单数据失败", reqId);
        // get data ok
        final PmsOrderPay orderPay = wxDataWrapper.getOrderPayData();
        final PmsPayDetailWx wxDetail = wxDataWrapper.getWxDetailData();
        final Map<String, String> params = wxDataWrapper.getWxParams();
        final String reqXml = serializeWxXml(params);
        final String orderId = orderPay.getOrderId();

        LOGGER.debug("orderId=" + orderId + " req xml\n" + reqXml);
        dbService.newOrderPayRecord(orderPay);
        dbService.newWxPayRecord(wxDetail);
        dbService.insertOrderItems(wxDataWrapper.getItems());
        dbService.log(new PmsOrderPayHttpLog(orderId, OrderPayOp.NEW_ORDER,
            true, "/pay/wx/order", orderReqBody));
        dbService.log(new PmsOrderPayHttpLog(orderId, OrderPayOp.NEW_PAY,
            true, config.getOrderUrl().toString(), reqXml));
        ResponseEntity<String> wxHttpRes = sendWxPost(config.getOrderUrl(), reqXml);
        return handleOrderResult(wxHttpRes, orderPay, wxDetail, reqId);
    }

    private ResponseEntity<String> handleOrderResult(ResponseEntity<String> wxHttpRes,
                                                     PmsOrderPay orderPay, PmsPayDetailWx wxDetail,
                                                     String reqId) {
        final String pseudoNo = orderPay.getOrderPseudoNo();
        final String orderId = orderPay.getOrderId();
        String wxOrderResBody = wxHttpRes.getBody();
        LOGGER.debug("new order " + orderId + " response " + wxOrderResBody);
        // get response data & check sign
        Map<String, String> wxOrderResMap = xmlParser.deserializeToMap(wxOrderResBody);
        if (wxOrderResMap == null || !dataBuilder.checkSign(wxOrderResMap))
            return clientErrResponse(BAD_GATEWAY, pseudoNo, OrderStatus.FAIL,
                "微信返回数据异常或解析失败", reqId);
        // check return status
        final String returnCode = wxOrderResMap.get(K_RETURN_CODE); // todo
        final String returnMsg = wxOrderResMap.get(K_RETURN_MSG);
        if (!isSuccess(returnCode)) {
            orderPay.setOrderStatus(OrderStatus.FAIL);
            orderPay.setPayStatus(PayStatus.CANCEL);
            orderPay.setOrderErrCode(returnCode);
            orderPay.setOrderErrDesc(returnMsg);
            orderPay.setCloseTime(ZonedDateTime.now());
            orderPay.setClosed(true);
            dbService.updateOrderPayRecord(orderPay);
            return clientErrResponse(BAD_GATEWAY, null, pseudoNo, OrderStatus.FAIL, null,
                returnCode, returnMsg, "请求微信出错", reqId);
        }
        // check result
        final String resultCode = wxOrderResMap.get(K_RESULT_CODE);
        if (isSuccess(resultCode)) {
            final String codeUrl = wxOrderResMap.get(K_CODE_URL);
            LOGGER.info("\n pseudoNo: " + pseudoNo +
                "  orderId: " + orderId + "\n" + codeUrl);
            orderPay.setOrderStatus(OrderStatus.SUCCESS);
            orderPay.setPayStatus(PayStatus.WAIT);
            wxDetail.setCodeUrl(codeUrl);
            wxDetail.setPrepayId(wxOrderResMap.get(K_PREPAY_ID));
            dbService.updateOrderPayRecord(orderPay);
            dbService.updateWxPayRecord(wxDetail);
            return clientOkResponse(new WxClientResponse(orderId, orderPay.getOrderStatus(),
                pseudoNo, codeUrl, reqId));
        } else {
            final String errCode = wxOrderResMap.get(K_ERR_CODE);
            final String errDesc = wxOrderResMap.get(K_ERR_CODE_DES);
            orderPay.setOrderStatus(OrderStatus.FAIL);
            orderPay.setPayStatus(PayStatus.CANCEL);
            orderPay.setOrderErrCode(errCode);
            orderPay.setOrderErrDesc(errDesc);
            orderPay.setCloseTime(ZonedDateTime.now());
            orderPay.setClosed(true);
            dbService.updateOrderPayRecord(orderPay);
            return clientErrResponse(FORBIDDEN, orderId, pseudoNo,
                orderPay.getOrderStatus(), orderPay.getPayStatus(),
                errCode, errDesc, "微信支付请求失败", reqId);
        }
    }

    /**
     * 微信平台异步通知结果：微信->后台
     */
    @RequestMapping(value = "/callback", method = RequestMethod.POST, consumes = TEXT_XML_VALUE)
    public ResponseEntity<String> callback(@RequestBody String callbackBody) {
        LOGGER.debug("pay callback\n" + callbackBody);
        // todo check sign!
        final Map<String, String> res = handleNotifyCallback(callbackBody);
        String resXml = serializeWxXml(res);
        return textResponse(OK, TEXT_XML, resXml);
    }

    private Map<String, String> handleNotifyCallback(String callbackBody) {
        // only easier to use
        final Map<String, String> failRes = Collections.singletonMap(K_RETURN_CODE, ReStatus.FAIL.name());
        final Map<String, String> okRes = Collections.singletonMap(K_RETURN_CODE, ReStatus.SUCCESS.name());
        // check sign & return status
        Map<String, String> callbackMap = xmlParser.deserializeToMap(callbackBody);
        if (!(callbackMap != null && dataBuilder.checkSign(callbackMap) &&
            isSuccess(callbackMap.get(K_RETURN_CODE))))
            return failRes;
        // check order id
        final String recOrderId = callbackMap.get(K_OUT_TRADE_NO);
        if (!(WxPayUtil.isValidOrderId(recOrderId) && dbService.wxPayRecordExists(recOrderId)))
            return failRes;
        // get wx detail data
        PmsOrderPay orderPay = dbService.getOrderPayRecord(recOrderId); // should exists
        if (orderPay.isClosed() && PayStatus.SUCCESS == orderPay.getPayStatus())
            return okRes;  // if order is success & closed, do not update again
        // log message
        dbService.log(new PmsOrderPayHttpLog(recOrderId,
            OrderPayOp.NOTIFY_PAY_RESULT, true, "/wx/pay/callback", callbackBody));
        // update
        PmsPayDetailWx wxDetail = dbService.getWxPayRecord(recOrderId); // should exists
        final String receivedTranId = callbackMap.get(K_TRANSACTION_ID);
        final String storedTranId = wxDetail.getTransactionId();
        final PayStatus storedPayStatus = orderPay.getPayStatus();
        // todo check other important data
        if (storedTranId != null) {
            // shouldn't happen!
            LOGGER.error("TRANSACTION ID not null! orderID=" + recOrderId +
                " tranId: stored=" + storedTranId + " received=" + receivedTranId);
        }
        // receive first time
        final String result = callbackMap.get(K_RESULT_CODE);
        if (PayStatus.isSuccess(result)) {
            orderPay.setPayStatus(PayStatus.SUCCESS);
            wxDetail.setTransactionId(receivedTranId);
            wxDetail.setOutTradeNo(recOrderId);
            fillPayResultData(wxDetail, callbackMap);
        } else {
            // seems not happen, wx does not send FAIL notifications
            LOGGER.info("receive result=FAIL callback for order " + recOrderId);
            orderPay.setPayStatus(PayStatus.FAIL);
            orderPay.setPayErrCode(callbackMap.get(K_ERR_CODE));
            orderPay.setPayErrDesc(callbackMap.get(K_ERR_CODE_DES));
        }
        orderPay.setCloseTime(ZonedDateTime.now());
        orderPay.setClosed(true);
        dbService.updateOrderPayRecord(orderPay);
        dbService.updateWxPayRecord(wxDetail);
        return okRes;
    }

    private void fillPayResultData(PmsPayDetailWx wxDetail, Map<String, String> resMap) {
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

    private Integer parseFee(String val) {
        if (val == null || !val.matches("[0-9]+")) return null;
        return Integer.parseInt(val);
    }

    /**
     * 订单状况查询：设备->后台
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ResponseEntity<String> query(@RequestParam("orderid") String orderId) {
        LOGGER.debug("new query for " + orderId);
        WxClientResponse res = new WxClientResponse(orderId);
        if (WxPayUtil.isValidOrderId(orderId)) {
            PmsOrderPay orderPay = dbService.getOrderPayRecord(orderId);
            if (orderPay != null) {
                final PmsPayDetailWx wxDetail = dbService.getWxPayRecord(orderId);
                final OrderStatus orderStatus = orderPay.getOrderStatus();
                if (!orderPay.isClosed() && isSyncTimeSuitable(wxDetail))
                    syncRemote(orderPay, wxDetail); // todo, temp use here, change to threadpool later
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
                return clientOkResponse(res);
            } else {
                return clientErrResponse(NOT_FOUND, "未找到记录");
            }
        } else {
            return clientErrResponse(BAD_REQUEST, "订单号错误");
        }
    }

    private boolean isSyncTimeSuitable(PmsPayDetailWx wxDetail) {
        final ZonedDateTime lastSync = wxDetail.getSyncTime();
        final ZonedDateTime now = ZonedDateTime.now();
        LOGGER.debug("minutes since create order: " + WxPayUtil.minutesBetween(wxDetail.getTimeStart(), now));
        if (lastSync == null) {
            return WxPayUtil.minutesBetween(wxDetail.getTimeStart(), now) > WxPayConfig.MIN_MINUTES;
        } else {
            return WxPayUtil.minutesBetween(lastSync, now) > config.getSyncMinutes();
        }
    }

    // todo schedule this
    private boolean syncRemote(PmsOrderPay orderPay, PmsPayDetailWx wxDetail) {
        if (orderPay.isClosed()) return false; // todo if closed, don't check again
        final String orderId = orderPay.getOrderId();
        final String wxTranId = wxDetail.getTransactionId();
        AbstractMap.SimpleEntry<String, String> reqIdPair;
        if (wxTranId != null) {
            reqIdPair = new AbstractMap.SimpleEntry<>(K_TRANSACTION_ID, wxTranId);
        } else {
            reqIdPair = new AbstractMap.SimpleEntry<>(K_OUT_TRADE_NO, orderId);
        }
        WxPayDataWrapper wrapper = dataBuilder.buildQueryData(reqIdPair);
        if (!wrapper.isEmpty()) {
            String reqXml = serializeWxXml(wrapper.getWxParams());
            dbService.log(new PmsOrderPayHttpLog(orderId, OrderPayOp.QUERY_PAY,
                true, config.getQueryUrl().toString(), reqXml));
            final ZonedDateTime syncTime = ZonedDateTime.now();
            ResponseEntity<String> wxHttpRes = sendWxPost(config.getQueryUrl(), reqXml);
            final String resBody = wxHttpRes.getBody();
            if (resBody == null) {
                LOGGER.error("empty wx query result for order " + orderId);
                return false;
            }
            LOGGER.debug("new wx query result for order " + orderId + " " + resBody);
            dbService.log(new PmsOrderPayHttpLog(orderId,
                OrderPayOp.QUERY_PAY, false, config.getQueryUrl().toString(), resBody));
            Map<String, String> wxQueryResMap = xmlParser.deserializeToMap(wxHttpRes.getBody());
            if (wxQueryResMap == null || !dataBuilder.checkSign(wxQueryResMap)
                || !isSuccess(wxQueryResMap.get(K_RETURN_CODE))) {
                LOGGER.error("query wx for order " + orderId + " return error or fail");
                return false;
            }
            final String resultCode = wxQueryResMap.get(K_RESULT_CODE);
            final String tradeState = wxQueryResMap.get(K_TRADE_STATE);
            final String tradeStateDesc = wxQueryResMap.get(K_TRADE_STATE_DESC);
            LOGGER.info("query wx for order " + orderId + " resultCode=" + resultCode +
                " tradeState=" + tradeState + " tradeStateDesc=" + tradeStateDesc);
            if (!isSuccess(resultCode) || tradeState == null) return false;
            wxDetail.setSyncTime(syncTime);
            wxDetail.setTradeState(tradeState);
            wxDetail.setTradeStateDesc(tradeStateDesc);
            PayStatus newPayStatus = null;
            TradeState state = TradeState.valueOf(tradeState);
            switch (state) {
                case SUCCESS:
                    fillPayResultData(wxDetail, wxQueryResMap); // store data
                    newPayStatus = PayStatus.SUCCESS;
                    break;
                case NOTPAY:
                    if (WxPayUtil.isLater(syncTime, wxDetail.getTimeExpire()))
                        newPayStatus = PayStatus.EXPIRE;
                    break;
                case CLOSED:
                    newPayStatus = PayStatus.CLOSE;
                    break;
                case PAYERROR:
                    newPayStatus = PayStatus.FAIL;
                    break;
                default: // todo not handle
            }
            // update wx detail
            dbService.updateWxPayRecord(wxDetail);
            // update orderpay if ok
            if (newPayStatus != null) {
                orderPay.setPayStatus(newPayStatus);
                orderPay.setCloseTime(ZonedDateTime.now());
                orderPay.setClosed(true);
                orderPay.setRemark(orderPay.getRemark() +
                    "\n" + orderPay.getCloseTime().toString() +
                    "] update pay_status to " + newPayStatus + " by query");
                dbService.updateOrderPayRecord(orderPay);
            }
        }
        return true;
    }

    /**
     * 关闭订单：设备->后台
     */
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public ResponseEntity<String> close(@RequestBody String closeReqBody) {
        LOGGER.debug("new close request\n" + closeReqBody);
        final WxClientRequest clientReq = jsonParser.deserialize(closeReqBody, WxClientRequest.class);
        if (clientReq == null)
            return clientErrResponse(BAD_REQUEST, "JSON解析错误");
        final String orderId = clientReq.getOrderId();
        final String robotUid = clientReq.getRobotUid();
        final String robotModel = clientReq.getRobotModel();
        if (orderId == null || robotUid == null || robotModel == null || !WxPayUtil.isValidOrderId(orderId))
            return clientErrResponse(BAD_REQUEST, "参数校验错误");
        PmsOrderPay orderPay = dbService.getOrderPayRecord(orderId);
        if (orderPay == null)
            return clientErrResponse(NOT_FOUND, "未找到订单" + orderId);
        if (!(robotUid.equals(orderPay.getOrderDeviceId()) && robotModel.equals(orderPay.getOrderDeviceGroup())))
            return clientErrResponse(FORBIDDEN, "关闭订单只能由同一设备发起"); // todo need?
        if (orderPay.isClosed())
            return clientErrResponse(FORBIDDEN, orderId, null,
                orderPay.getOrderStatus(), orderPay.getPayStatus(),
                "订单" + orderId + "已关闭", clientReq.getId());
        if (WxPayUtil.minutesBetween(orderPay.getCreateTime(), ZonedDateTime.now()) <= WxPayConfig.MIN_MINUTES + 1) // allow 1 more min
            return clientErrResponse(FORBIDDEN, orderId, "申请关闭订单时间过短", clientReq.getId());
        final WxPayDataWrapper dataWrapper = dataBuilder.buildCloseData(clientReq);
        final Map<String, String> params = dataWrapper.getWxParams();
        final String reqXml = serializeWxXml(params);
        dbService.log(new PmsOrderPayHttpLog(orderId, OrderPayOp.CLOSE_ORDER,
            true, "/pay/wx/close", closeReqBody));
        dbService.log(new PmsOrderPayHttpLog(orderId, OrderPayOp.CLOSE_PAY,
            true, config.getCloseUrl().toString(), reqXml));
        ResponseEntity<String> wxHttpRes = sendWxPost(config.getCloseUrl(), reqXml);
        return handleCloseResult(wxHttpRes, orderPay, clientReq.getId());
    }

    private ResponseEntity<String> handleCloseResult(ResponseEntity<String> wxHttpRes,
                                                     PmsOrderPay orderPay, String reqId) {
        final String orderId = orderPay.getOrderId();
        final OrderStatus orderStatus = orderPay.getOrderStatus();
        final PayStatus storedPayStatus = orderPay.getPayStatus();
        final String wxCloseResBody = wxHttpRes.getBody();
        LOGGER.debug("close order " + orderId + " response " + wxCloseResBody);
        if (wxCloseResBody == null)
            return clientErrResponse(BAD_GATEWAY, orderId, null,
                orderStatus, storedPayStatus, "请求微信关闭订单失败", reqId);
        Map<String, String> wxCloseResMap = xmlParser.deserializeToMap(wxCloseResBody);
        if (wxCloseResMap == null || !dataBuilder.checkSign(wxCloseResMap))
            return clientErrResponse(BAD_GATEWAY, orderId, null,
                orderStatus, storedPayStatus, "微信返回结果异常或解析失败", reqId);
        // check return status
        final String returnCode = wxCloseResMap.get(K_RETURN_CODE); // todo
        final String returnMsg = wxCloseResMap.get(K_RETURN_MSG);
        if (!isSuccess(returnCode))
            return clientErrResponse(BAD_GATEWAY, orderId, null,
                orderStatus, storedPayStatus,
                returnCode, returnMsg, "请求微信出错", reqId);
        final String resultCode = wxCloseResMap.get(K_RESULT_CODE);
        final String resultMsg = wxCloseResMap.get(K_RETURN_MSG);
        if (!isSuccess(resultCode))
            return clientErrResponse(BAD_REQUEST, orderId, null, orderStatus, storedPayStatus,
                wxCloseResMap.get(K_ERR_CODE), wxCloseResMap.get(K_ERR_CODE_DES),
                "关闭请求失败: " + resultCode + " " + resultMsg, reqId);
        // all ok, update
        orderPay.setPayStatus(PayStatus.CLOSE);
        orderPay.setCloseTime(ZonedDateTime.now());
        orderPay.setClosed(true);
        orderPay.setRemark(resultMsg);
        dbService.updateOrderPayRecord(orderPay);
        return clientOkResponse(new WxClientResponse(orderId, orderStatus, orderPay.getPayStatus(), reqId));
    }

    // @RequestMapping(value = "/downloadBill", method = RequestMethod.GET)
    // public ResponseEntity<String> downloadBill(){
    // }

    private ResponseEntity<String> clientOkResponse(WxClientResponse res) {
        return jsonResponse(OK, res);
    }

    private ResponseEntity<String> clientErrResponse(HttpStatus status, String errMsg) {
        return clientErrResponse(status, null, null, errMsg, null);
    }

    private ResponseEntity<String> clientErrResponse(HttpStatus status, String orderId, String errMsg, String reqId) {
        return clientErrResponse(status, orderId, null, null, null, errMsg, reqId);
    }

    private ResponseEntity<String> clientErrResponse(HttpStatus status, String pseudoNo,
                                                     OrderStatus orderStatus, String errMsg, String id) {
        return clientErrResponse(status, null, pseudoNo, orderStatus, null, errMsg, id);
    }

    private ResponseEntity<String> clientErrResponse(HttpStatus status, String orderId, String pseudoNo,
                                                     OrderStatus orderStatus, PayStatus payStatus,
                                                     String errMsg, String id) {
        return clientErrResponse(status, orderId, pseudoNo, orderStatus, payStatus,
            String.valueOf(status.value()), status.getReasonPhrase(), errMsg, id);
    }

    private ResponseEntity<String> clientErrResponse(HttpStatus status, String orderId, String pseudoNo,
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

    private ResponseEntity<String> clientErrResponse(HttpStatus status, WxClientResponse res) {
        return jsonResponse(status, res);
    }

    private ResponseEntity<String> jsonResponse(HttpStatus status, Object obj) {
        return textResponse(status, APPLICATION_JSON, jsonParser.serialize(obj));
    }

    private ResponseEntity<String> xmlResponse(HttpStatus status, Object obj) {
        return textResponse(status, APPLICATION_XML, xmlParser.serialize(obj));
    }

    private ResponseEntity<String> textResponse(HttpStatus status, MediaType mediaType, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new ResponseEntity<>(body, headers, status);
    }
}
