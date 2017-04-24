package com.csjbot.api.pay.controller;

import com.csjbot.api.pay.model.*;
import com.csjbot.api.pay.service.WxPayConfig;
import com.csjbot.api.pay.service.WxPayDBService;
import com.csjbot.api.pay.service.WxPayDataService;
import com.csjbot.api.pay.util.WxPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.csjbot.api.pay.controller.WxPayControllerHelper.*;
import static com.csjbot.api.pay.model.ReStatus.isSuccess;
import static com.csjbot.api.pay.service.WxPayParamName.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

// todo 1.use object injection in controller 2.create object wrap wx result map
@RestController
@RequestMapping("/pay/wx")
public class WxPayController {
    // @RequestMapping(produces = "text/plain;charset=UTF-8")
    @RequestMapping
    @ResponseStatus(OK)
    public String hello() {
        LOGGER.debug(System.getProperty("file.encoding"));
        LOGGER.debug(Charset.defaultCharset().name());
        String hello = "hello! file.encoding is " + System.getProperty("file.encoding") +
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

    private final WxPayConfig config;
    private final WxPayControllerHelper helper;
    private final WxPayDBService dbService;
    private final WxPayDataService dataBuilder;
    private final ThreadPoolTaskExecutor queryExecutor;
    private final ThreadPoolTaskScheduler queryScheduler;
    private final BlockingQueue<String> queryQueue;
    private final AtomicBoolean isQueryScheduleRunning = new AtomicBoolean(false);

    @Autowired
    public WxPayController(WxPayConfig wxPayConfig, WxPayControllerHelper helper,
                           @Qualifier("wxPayDBService") WxPayDBService wxPayDBService,
                           @Qualifier("wxDataService") WxPayDataService dataBuilder,
                           ThreadPoolTaskExecutor wxPayQueryExecutor,
                           ThreadPoolTaskScheduler wxPayQueryScheduler) {
        System.out.println("init WxPayController");
        this.config = wxPayConfig;
        this.helper = helper;

        this.dbService = wxPayDBService;
        this.dataBuilder = dataBuilder;

        this.queryExecutor = wxPayQueryExecutor;
        this.queryScheduler = wxPayQueryScheduler;
        this.queryQueue = new ArrayBlockingQueue<>(config.getQueryQueueCapacity());//todo
    }

    @PostConstruct
    public void start() {
        LOGGER.debug("PostConstruct");
        queryScheduler.scheduleAtFixedRate(() -> {
            if (isQueryScheduleRunning.compareAndSet(false, true)) {
                final long checkSize = queryQueue.size(); // size will change during for-loop, record it beforehand
                LOGGER.debug("query check size " + checkSize);
                for (int i = 0; i < checkSize; i++) {
                    final String orderId = queryQueue.poll(); // orders that are closed will only be removed here
                    if (orderId == null) break; // should not happen
                    final PmsOrderPay orderPay = dbService.getOrderPayRecord(orderId); // todo
                    if (orderPay == null) {
                        LOGGER.error(orderId + " db record not found"); // should not happen
                    } else {
                        if (!orderPay.isClosed()) {
                            queryQueue.add(orderId); // put back to queue end
                            if (isSyncTimeSuitable(orderPay)) {
                                queryExecutor.execute(() -> syncRemote(orderPay));
                            }
                        }
                    }
                }
                isQueryScheduleRunning.set(false);
            }
        }, 1000 * 60 * config.getScheduleMinutes());
    }

    @PreDestroy
    public void stop() {
        LOGGER.debug("preDestroy");
        queryExecutor.shutdown(); // spring has already implemented shutdown wait
        queryScheduler.shutdown();
    }

    private boolean isNewOrder(ZonedDateTime createTime) {
        long minSinceCreate = WxPayUtil.minutesBetween(createTime, ZonedDateTime.now());
        return minSinceCreate <= WxPayConfig.MIN_MINUTES + 1; // wx requires 5 min, add 1 more here for simple use
    }

    private boolean isSyncTimeSuitable(PmsOrderPay orderPay) {
        final ZonedDateTime lastSync = orderPay.getSyncTime();
        final ZonedDateTime now = ZonedDateTime.now();
        if (lastSync == null) {
            return true;
        } else {
            return WxPayUtil.minutesBetween(lastSync, now) > config.getSyncMinutes();
        }
    }

    private boolean syncRemote(PmsOrderPay orderPay) {
        return syncRemote(orderPay, dbService.getWxPayRecord(orderPay.getOrderId()));
    }

    // todo refactor code, schedule this
    private boolean syncRemote(PmsOrderPay orderPay, PmsPayDetailWx wxDetail) {
        if (orderPay.isClosed()) return false; // if closed, don't check again
        final String orderId = orderPay.getOrderId();
        WxPayDataWrapper wrapper = dataBuilder.buildQueryData(orderId);
        if (!wrapper.isEmpty()) {
            String reqXml = helper.serializeWxXml(wrapper.getWxParams());
            logHttp(orderId, OrderPayOp.QUERY_PAY, true, config.getQueryUrl().toString(), reqXml);
            final ZonedDateTime syncTime = ZonedDateTime.now();
            final String resBody = helper.sendWxPost(config.getQueryUrl(), reqXml);
            if (resBody == null) {
                LOGGER.error("empty wx query result for order " + orderId);
                return false;
            }
            LOGGER.debug("new wx query result for order " + orderId + " " + resBody);
            logHttp(orderId, OrderPayOp.QUERY_PAY, false, config.getQueryUrl().toString(), resBody);
            Map<String, String> resMap = helper.deserializeWxXml(resBody);
            if (resMap == null || !dataBuilder.checkSign(resMap)
                || !isSuccess(resMap.get(K_RETURN_CODE))) {
                LOGGER.error("query wx for order " + orderId + " return error or fail");
                return false;
            }
            final String resultCode = resMap.get(K_RESULT_CODE);
            final String tradeState = resMap.get(K_TRADE_STATE);
            final String tradeStateDesc = resMap.get(K_TRADE_STATE_DESC);
            LOGGER.info("query wx for order " + orderId + " resultCode=" + resultCode +
                " tradeState=" + tradeState + " tradeStateDesc=" + tradeStateDesc);
            // update wx detail
            wxDetail.setTradeState(tradeState);
            wxDetail.setTradeStateDesc(tradeStateDesc);
            if (isSuccess(resultCode) && tradeState != null) {
                TradeState state = TradeState.valueOf(tradeState);
                if (TradeState.SUCCESS == state) {
                    if (tradeStateDesc == null) wxDetail.setTradeState("已支付");
                    fillPayResultData(wxDetail, resMap);
                }
                PayStatus newPayStatus =
                    mapFromTradeState(state, WxPayUtil.isLater(syncTime, wxDetail.getTimeExpire()));
                if (newPayStatus != null) {
                    orderPay.setPayStatus(newPayStatus);
                    orderPay.setCloseTime(ZonedDateTime.now());
                    orderPay.setClosed(true);
                    orderPay.setRemark(newPayStatus + " by query");
                }
            } else {
                orderPay.setPayErrCode(resMap.get(K_ERR_CODE));
                orderPay.setPayErrDesc(resMap.get(K_ERR_CODE_DES));
            }
            dbService.updateWxPayRecord(wxDetail);
            // update orderpay if need
            orderPay.setSyncTime(syncTime);
            dbService.updateOrderPayRecord(orderPay);
        }
        return true;
    }

    /**
     * 二维码支付下单请求：设备->后台
     */
    @PostMapping(value = "/order", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> orderQR(@RequestBody String orderReqBody) {
        LOGGER.debug("order request " + orderReqBody);
        WxClientRequest orderReq = helper.deserializeClientJson(orderReqBody);
        if (orderReq == null)
            return helper.clientErrResponse(BAD_REQUEST, "JSON解析失败");
        if (orderReq.getOrderList() == null || orderReq.getOrderList().size() == 0)
            return helper.clientErrResponse(BAD_GATEWAY, "商品列表为空");
        final String reqId = orderReq.getId();
        final String pseudoNo = orderReq.getOrderPseudoNo();
        if (dbService.orderPayRecordExists(pseudoNo, orderReq.getRobotUid()))
            return helper.clientErrResponse(BAD_REQUEST, pseudoNo, null, "pseudoNo已使用", reqId);
        final WxPayDataWrapper dataWrapper = dataBuilder.buildOrderData(orderReq);
        if (dataWrapper.isEmpty())
            return helper.clientErrResponse(BAD_REQUEST, pseudoNo, OrderStatus.FAIL,
                "获取下单数据失败", reqId);
        // get data ok
        final String reqXml = helper.serializeWxXml(dataWrapper.getWxParams());

        dbService.newOrderPayRecord(dataWrapper.getOrderPay());
        dbService.newWxPayRecord(dataWrapper.getWxDetail());
        dbService.insertOrderItems(dataWrapper.getItems());

        final String orderId = dataWrapper.getOrderPay().getOrderId();
        logHttp(orderId, OrderPayOp.NEW_ORDER, true, "/pay/wx/order", orderReqBody);
        logHttp(orderId, OrderPayOp.NEW_PAY, true, config.getOrderUrl(), reqXml);

        return handleOrderResult(helper.sendWxPost(config.getOrderUrl(), reqXml),
            dataWrapper, reqId);
    }

    private ResponseEntity<String> handleOrderResult(String wxOrderResBody,
                                                     WxPayDataWrapper dataWrapper,
                                                     String reqId) {
        final PmsOrderPay orderPay = dataWrapper.getOrderPay();
        final PmsPayDetailWx wxDetail = dataWrapper.getWxDetail();
        final String pseudoNo = orderPay.getOrderPseudoNo();
        final String orderId = orderPay.getOrderId();
        LOGGER.debug("new order " + orderId + " response " + wxOrderResBody);
        // get response data & check sign
        Map<String, String> wxOrderResMap = helper.deserializeWxXml(wxOrderResBody);
        if (wxOrderResMap == null || !dataBuilder.checkSign(wxOrderResMap))
            return helper.clientErrResponse(BAD_GATEWAY, pseudoNo, OrderStatus.FAIL,
                "微信返回数据异常或解析失败", reqId);
        // check return status & result
        final String returnCode = wxOrderResMap.get(K_RETURN_CODE); // todo
        final String returnMsg = wxOrderResMap.get(K_RETURN_MSG);
        final String resultCode = wxOrderResMap.get(K_RESULT_CODE);
        String errCode = wxOrderResMap.get(K_ERR_CODE);
        String errDesc = wxOrderResMap.get(K_ERR_CODE_DES);
        if (isSuccess(resultCode)) {
            final String codeUrl = wxOrderResMap.get(K_CODE_URL);
            LOGGER.info("\n pseudoNo: " + pseudoNo + "  orderId: " + orderId + "\n" + codeUrl);
            orderPay.setOrderStatus(OrderStatus.SUCCESS);
            orderPay.setPayStatus(PayStatus.WAIT);
            wxDetail.setCodeUrl(codeUrl);
            wxDetail.setPrepayId(wxOrderResMap.get(K_PREPAY_ID));
            dbService.updateOrderPayRecord(orderPay);
            dbService.updateWxPayRecord(wxDetail);

            queryQueue.add(orderId);

            return helper.clientOkResponse(new WxClientResponse(orderId, orderPay.getOrderStatus(),
                pseudoNo, codeUrl, reqId));
        } else {
            HttpStatus status;
            if (isSuccess(returnCode)) {  // todo how to handle http error
                status = FORBIDDEN;
            } else {
                status = BAD_GATEWAY;
                errCode = returnCode;
                errDesc = returnMsg;
            }
            orderPay.setOrderStatus(OrderStatus.FAIL);
            orderPay.setPayStatus(PayStatus.CANCEL);
            orderPay.setOrderErrCode(errCode);
            orderPay.setOrderErrDesc(errDesc);
            orderPay.setCloseTime(ZonedDateTime.now());
            orderPay.setClosed(true);
            dbService.updateOrderPayRecord(orderPay);
            return helper.clientErrResponse(status, orderId, pseudoNo,
                orderPay.getOrderStatus(), orderPay.getPayStatus(),
                errCode, errDesc, "微信支付下单请求失败", reqId);
        }
    }


    /**
     * 微信平台异步通知结果：微信->后台
     */
    @PostMapping(value = "/callback", consumes = TEXT_XML_VALUE, produces = TEXT_XML_VALUE)
    public ResponseEntity<String> callback(@RequestBody String callbackBody) {
        LOGGER.debug("pay callback\n" + callbackBody);
        // check sign & return status
        Map<String, String> callbackMap = helper.deserializeWxXml(callbackBody);
        if (callbackMap == null || !dataBuilder.checkSign(callbackMap) || !isSuccess(callbackMap.get(K_RETURN_CODE)))
            return helper.wxCallbackFailResponse();
        // check order id
        final String recOrderId = callbackMap.get(K_OUT_TRADE_NO);
        if (!WxPayUtil.isValidOrderId(recOrderId)) {
            if (!dbService.wxPayRecordExists(recOrderId)) LOGGER.error("no wx detail found for " + recOrderId);
            return helper.wxCallbackFailResponse();
        }
        // log
        logHttp(recOrderId, OrderPayOp.NOTIFY_PAY_RESULT, true, config.getNotifyUrl(), callbackBody);
        // get wx detail data
        final PmsOrderPay orderPay = dbService.getOrderPayRecord(recOrderId); // should exists
        if (orderPay.isClosed() && PayStatus.SUCCESS == orderPay.getPayStatus())
            return helper.wxCallbackOkResponse();  // if order is success & closed, do not update again
        // log message
        dbService.log(new PmsOrderPayHttpLog(recOrderId,
            OrderPayOp.NOTIFY_PAY_RESULT, true, "/wx/pay/callback", callbackBody));
        // update
        final PmsPayDetailWx wxDetail = dbService.getWxPayRecord(recOrderId); // should exists
        final String receivedTranId = callbackMap.get(K_TRANSACTION_ID);
        final String storedTranId = wxDetail.getTransactionId();
        // todo check other important data
        if (storedTranId != null) { // shouldn't happen!
            LOGGER.error("TRANSACTION ID not null! orderID=" + recOrderId +
                " tranId: stored=" + storedTranId + " received=" + receivedTranId);
        }
        // receive first time
        final String result = callbackMap.get(K_RESULT_CODE);
        if (PayStatus.isSuccess(result)) {
            orderPay.setPayStatus(PayStatus.SUCCESS);
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
        return helper.wxCallbackOkResponse();
    }

    /**
     * 订单状况查询：设备->后台
     */
    @GetMapping(value = "/query", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> query(@RequestParam("orderid") String orderId) {
        LOGGER.debug("new query for " + orderId);
        if (WxPayUtil.isValidOrderId(orderId)) {
            PmsOrderPay orderPay = dbService.getOrderPayRecord(orderId);
            if (orderPay != null) {
                final PmsPayDetailWx wxDetail = dbService.getWxPayRecord(orderId);
                if (!orderPay.isClosed() && isSyncTimeSuitable(orderPay))
                    syncRemote(orderPay, wxDetail); // todo, temp use here, change to threadpool later
                WxClientResponse res = getQueryClientData(orderPay, wxDetail);
                Integer refundedTotal;
                if (dbService.refundRecordExists(orderId) && (refundedTotal = dbService.getRefundedTotalFee(orderId)) > 0) {
                    res.setHasRefund(true);
                    res.setRefundTotalFee(refundedTotal);
                } else {
                    res.setHasRefund(false);
                }
                return helper.clientOkResponse(res);
            } else {
                return helper.clientErrResponse(NOT_FOUND, "未找到记录");
            }
        } else {
            return helper.clientErrResponse(BAD_REQUEST, "订单号错误");
        }
    }

    /**
     * 关闭订单：设备->后台
     */
    @PostMapping(value = "/close", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> close(@RequestBody String closeReqBody) {
        LOGGER.debug("new close request\n" + closeReqBody);
        final WxClientRequest clientReq = helper.deserializeClientJson(closeReqBody);
        if (clientReq == null)
            return helper.clientErrResponse(BAD_REQUEST, "JSON解析错误");
        final String orderId = clientReq.getOrderId();
        final String robotUid = clientReq.getRobotUid();
        final String robotModel = clientReq.getRobotModel();
        if (orderId == null || robotUid == null || robotModel == null || !WxPayUtil.isValidOrderId(orderId))
            return helper.clientErrResponse(BAD_REQUEST, "参数校验错误");
        PmsOrderPay orderPay = dbService.getOrderPayRecord(orderId);
        if (orderPay == null)
            return helper.clientErrResponse(NOT_FOUND, "未找到订单" + orderId);
        if (!(robotUid.equals(orderPay.getOrderDeviceId()) && robotModel.equals(orderPay.getOrderDeviceGroup())))
            return helper.clientErrResponse(FORBIDDEN, "关闭订单只能由同一设备发起"); // todo need?
        if (orderPay.isClosed())
            return helper.clientErrResponse(FORBIDDEN, orderId, null,
                orderPay.getOrderStatus(), orderPay.getPayStatus(),
                "订单" + orderId + "已关闭", clientReq.getId());
        if (isNewOrder(orderPay.getCreateTime()))
            return helper.clientErrResponse(FORBIDDEN, orderId, "申请关闭订单时间过短", clientReq.getId());
        final WxPayDataWrapper dataWrapper = dataBuilder.buildCloseData(orderId);
        final String reqXml = helper.serializeWxXml(dataWrapper.getWxParams());
        logHttp(orderId, OrderPayOp.CLOSE_ORDER, true, "/pay/wx/close", closeReqBody);
        logHttp(orderId, OrderPayOp.CLOSE_PAY, true, config.getCloseUrl(), reqXml);
        return handleCloseResult(helper.sendWxPost(config.getCloseUrl(), reqXml),
            orderPay, clientReq.getId());
    }

    private ResponseEntity<String> handleCloseResult(String wxCloseResBody,
                                                     PmsOrderPay orderPay, String reqId) {
        final String orderId = orderPay.getOrderId();
        final OrderStatus orderStatus = orderPay.getOrderStatus();
        final PayStatus storedPayStatus = orderPay.getPayStatus();
        LOGGER.debug("close order " + orderId + " response " + wxCloseResBody);
        Map<String, String> wxCloseResMap = helper.deserializeWxXml(wxCloseResBody);
        if (wxCloseResMap == null || !dataBuilder.checkSign(wxCloseResMap))
            return helper.clientErrResponse(BAD_GATEWAY, orderId, null,
                orderStatus, storedPayStatus, "微信返回结果异常或解析失败", reqId);
        // check return status
        final String returnCode = wxCloseResMap.get(K_RETURN_CODE); // todo
        if (!isSuccess(returnCode))
            return helper.clientErrResponse(BAD_GATEWAY, orderId, null, orderStatus, storedPayStatus,
                returnCode, wxCloseResMap.get(K_RETURN_MSG), "请求微信出错", reqId);
        final String resultCode = wxCloseResMap.get(K_RESULT_CODE);
        if (!isSuccess(resultCode))
            return helper.clientErrResponse(BAD_REQUEST, orderId, null, orderStatus, storedPayStatus,
                wxCloseResMap.get(K_ERR_CODE), wxCloseResMap.get(K_ERR_CODE_DES),
                "关闭请求失败: " + resultCode + " " + wxCloseResMap.get(K_RESULT_MSG), reqId);
        // all ok, update
        orderPay.setPayStatus(PayStatus.CLOSE);
        orderPay.setCloseTime(ZonedDateTime.now());
        orderPay.setClosed(true);
        orderPay.setRemark(orderPay.getPayStatus() + " by client");
        dbService.updateOrderPayRecord(orderPay);
        return helper.clientOkResponse(new WxClientResponse(orderId, orderStatus, orderPay.getPayStatus(), reqId));
    }

    // todo lack data validation & user auth, do not use in product!
    @PostMapping(path = "/refund", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> refund(@RequestBody String clientReqJson) {
        if (!config.isRefundEnabled())
            return helper.textResponse(NOT_FOUND, TEXT_PLAIN, "未开放退款功能");
        LOGGER.debug("new refund request\n " + clientReqJson);
        WxClientRequest clientReq = helper.deserializeClientJson(clientReqJson);
        if (clientReq == null) return helper.clientErrResponse(BAD_REQUEST, "JSON解析失败");
        String reqId = clientReq.getId();
        String orderId = clientReq.getOrderId();
        PmsOrderPay orderPay = dbService.getOrderPayRecord(orderId);
        if (orderPay == null || !orderPay.isClosed() || PayStatus.SUCCESS != orderPay.getPayStatus())
            return helper.clientErrResponse(BAD_REQUEST, orderId, "订单不存在或不符合退款条件", reqId);
        Integer refunded = dbService.getRefundedTotalFee(orderId);
        Integer totalPaied = orderPay.getOrderTotalFee();
        Integer thisRefund = clientReq.getRefundFee();
        if (thisRefund == null || thisRefund <= 0 || thisRefund > (totalPaied - refunded))
            return helper.clientErrResponse(BAD_REQUEST, orderId, "退款金额有误或超出范围", reqId);
        WxPayDataWrapper wrapper = dataBuilder.buildRefundData(orderId, thisRefund);
        final PmsRefund refund = wrapper.getRefund();
        final String reqXml = helper.serializeWxXml(wrapper.getWxParams());
        logHttp(orderId, OrderPayOp.REFUND, true, config.getRefundUrl(), reqXml);
        dbService.newRefundRecord(refund);
        // todo till this
        return handleRefundResult(helper.sendWxPostWithCert(config.getRefundUrl(), reqXml), refund, reqId);
    }

    // todo
    private ResponseEntity<String> handleRefundResult(String wxRefundResBody, PmsRefund refund, String reqId) {
        final String orderId = refund.getOrderId();
        final String refundNo = refund.getRefundNo();
        LOGGER.debug("order " + orderId + " refund " + refundNo + " result: " + wxRefundResBody);
        Map<String, String> resMap = helper.deserializeWxXml(wxRefundResBody);
        if (resMap == null || !dataBuilder.checkSign(resMap))
            return helper.clientErrResponse(BAD_GATEWAY, orderId, "微信返回结果异常或解析失败", reqId);
        logHttp(orderId, OrderPayOp.REFUND, false, config.getRefundUrl(), wxRefundResBody);

        String resultCode = resMap.get(K_RESULT_CODE);
        RefundStatus status;
        WxClientResponse clientRes = new WxClientResponse(orderId);
        // todo
        if (isSuccess(resultCode)) {
            status = RefundStatus.WAIT;
            // update refund
            String wxRefundId = resMap.get(K_REFUND_ID);
            // todo wx refund detail
            PmsRefundDetailWx wxRefund = new PmsRefundDetailWx(refundNo);
            wxRefund.setRefundId(wxRefundId);
            wxRefund.setRefundFee(WxPayUtil.parseFee(resMap.get(K_REFUND_FEE)));
            wxRefund.setCashRefundFee(WxPayUtil.parseFee(resMap.get(K_CASH_REFUND_FEE)));
            dbService.newWxRefundRecord(wxRefund);
            // response to client
            clientRes.setRefundNo(refundNo);
            clientRes.setWxRefundId(wxRefundId);
        } else {
            status = RefundStatus.FAIL;
            String returnCode = resMap.get(K_RETURN_CODE);
            String errCode = resMap.getOrDefault(K_ERR_CODE, returnCode);
            String errDesc = resMap.getOrDefault(K_ERR_CODE_DES, resMap.get(K_RETURN_MSG));
            // update refund
            refund.setRefundErrCode(errCode);
            refund.setRefundErrDesc(errDesc);
            refund.setIsClosed(true);
            refund.setCloseTime(ZonedDateTime.now());
            if (!isSuccess(returnCode)) refund.setRemark("http response err"); // todo
            // response to client
            clientRes.setRemark("退款请求失败");
            // no wx refund detail
        }
        // update refund
        refund.setRefundStatus(status);
        dbService.updateRefundRecord(refund);
        // response to client
        clientRes.setRefundFee(refund.getRefundFee());
        clientRes.setRefundStatus(status);
        return RefundStatus.WAIT == status ?
            helper.clientOkResponse(clientRes) : helper.clientErrResponse(BAD_GATEWAY, clientRes);
    }

    @GetMapping(value = "/refund/query", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> refundQuery(@RequestParam("refundno") String refundNo) {
        if (!config.isRefundEnabled())
            return helper.textResponse(NOT_FOUND, TEXT_PLAIN, "未开放退款功能");
        LOGGER.debug("new refund query for " + refundNo);
        if (refundNo == null || !WxPayUtil.isValidRefoundNo(refundNo))
            return helper.clientErrResponse(BAD_GATEWAY, "退款单号错误");
        PmsRefund refund = dbService.getRefundRecord(refundNo);
        if (refund == null)
            return helper.clientErrResponse(NOT_FOUND, refundNo, "该订单无退款记录", null);
        WxPayDataWrapper wrapper = dataBuilder.buildRefundQueryData(refundNo);
        String resBody = helper.sendWxPostWithCert(config.getRefundQueryUrl(), helper.serializeWxXml(wrapper.getWxParams()));
        return handleRefundQueryResult(resBody, refund);
        // return helper.textResponse(OK, TEXT_XML, resBody);
    }

    // todo logic not well sorted!
    private ResponseEntity<String> handleRefundQueryResult(String refunsQueryResBody, PmsRefund refund) {
        String refundNo = refund.getRefundNo();
        String orderId = refund.getOrderId();
        LOGGER.debug("refund " + refundNo + " query result " + refunsQueryResBody);
        logHttp(refundNo, OrderPayOp.QUERY_REFUND, false, config.getRefundQueryUrl(), refunsQueryResBody);
        HttpStatus status;
        WxClientResponse clientRes = new WxClientResponse(orderId, refundNo);
        Map<String, String> resMap = helper.deserializeWxXml(refunsQueryResBody);
        if (resMap != null) {
            final String resultCode = resMap.get(K_RESULT_CODE);
            String errCode = resMap.get(K_ERR_CODE);
            String errDesc = resMap.get(K_ERR_CODE_DES);
            LOGGER.error("refund query for " + refundNo + " result " + resultCode
                + " err code: " + errCode + " desc: " + errDesc);
            if (isSuccess(resultCode)) {
                status = OK;
                boolean allOk = true;
                for (PmsRefundDetailWx wxRefund : WxPayControllerHelper.parseRefundResultData(refund, resMap)) {
                    LOGGER.debug("refund no " + refundNo + " n=" + wxRefund.getRefundIdSn()
                        + " fee " + wxRefund.getRefundFee() + " status " + wxRefund.getRefundStatus());
                    dbService.newWxRefundRecord(wxRefund);
                    if (!isSuccess(wxRefund.getRefundStatus())) allOk = false;
                }
                if (allOk) {
                    refund.setRefundStatus(RefundStatus.SUCCESS);
                    refund.setIsClosed(true);
                    refund.setCloseTime(ZonedDateTime.now());
                    dbService.updateRefundRecord(refund);
                    clientRes.setRefundStatus(RefundStatus.SUCCESS);
                    clientRes.setRefundFee(refund.getRefundFee());
                    clientRes.setRefundTotalFee(dbService.getRefundedTotalFee(orderId));
                    clientRes.setOrderTotalFee(WxPayUtil.parseFee(resMap.get(K_TOTAL_FEE)));
                } else {
                    // todo
                    refund.setRefundStatus(RefundStatus.WAIT);
                    refund.setSyncTime(ZonedDateTime.now());
                    dbService.updateRefundRecord(refund);
                    clientRes.setRefundStatus(RefundStatus.WAIT);
                }
            } else {
                status = FORBIDDEN;
                refund.setRefundErrCode(errCode);
                refund.setRefundErrDesc(errDesc);
                refund.setSyncTime(ZonedDateTime.now());
                dbService.updateRefundRecord(refund);
                clientRes.setRemark("微信查询退款失败");
            }
        } else {
            status = BAD_GATEWAY;
            clientRes.setRemark("微信查询退款出错");
        }
        return helper.clientErrResponse(status, clientRes);
    }

    public void logHttp(String orderId, OrderPayOp op, boolean isReq, URI url, String body) {
        logHttp(orderId, op, isReq, url.toString(), body);
    }

    public void logHttp(String orderId, OrderPayOp op, boolean isReq, String path, String body) {
        dbService.log(new PmsOrderPayHttpLog(orderId, op, isReq, path, body));
    }
}
