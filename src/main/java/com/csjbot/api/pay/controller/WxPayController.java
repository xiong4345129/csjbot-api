package com.csjbot.api.pay.controller;

import com.csjbot.api.pay.model.*;
import com.csjbot.api.pay.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.util.Map;

import static com.csjbot.api.pay.model.ReturnStatus.isSuccess;
import static com.csjbot.api.pay.service.WxPayParamName.*;
import static com.csjbot.api.pay.util.WxPayUtil.isValidOrderId;
import static com.csjbot.api.pay.util.WxPayUtil.parseDateTime;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/pay/wx")
public class WxPayController {
    @RequestMapping(produces = TEXT_PLAIN_VALUE)
    @ResponseStatus(OK)
    public String hello() {
        return "hello from " + WxPayController.class.getName();
    }

    @RequestMapping("/echo")
    @ResponseStatus(OK)
    public String echo(@RequestParam("word") String echo) {
        return echo;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(WxPayController.class);
    private static final String WX_XML_ROOT_NAME = "xml";

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

    /**
     * 二维码支付下单请求：设备->后台
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> orderQR(@RequestBody String orderReqBody) {
        LOGGER.debug("order request " + orderReqBody);
        WxClientOrderRequest orderReq =
            jsonParser.deserialize(orderReqBody, WxClientOrderRequest.class);
        if (orderReq == null)
            return wrapOrderErr(BAD_REQUEST, null, "下单请求解析失败", null);
        final String pseudoNo = orderReq.getOrderPseudoNo();
        final String reqId = orderReq.getId();
        WxPayDataWrapper wxDataWrapper = dataBuilder.buildPayData(orderReq);
        if (wxDataWrapper == null)
            return wrapOrderErr(BAD_REQUEST, pseudoNo, "获取下单数据失败", reqId);
        // get data ok
        final PmsOrderPay orderPay = wxDataWrapper.getOrderPayData();
        final PmsPayDetailWx wxDetail = wxDataWrapper.getWxDetailData();
        final Map<String, String> params = wxDataWrapper.getWxParams();
        final String reqXml = xmlParser.serialize(params, WX_XML_ROOT_NAME);
        final String orderId = orderPay.getOrderId();

        LOGGER.debug("orderId=" + orderId + " req xml\n" + reqXml);
        dbService.newOrderPayRecord(orderPay);
        dbService.newWxPayRecord(wxDetail);
        dbService.insertOrderItems(wxDataWrapper.getItems());
        dbService.log(new PmsOrderPayHttpLog(orderId, OrderPayOp.NEW_ORDER,
            true, "/pay/wx/qr", orderReqBody));
        dbService.log(new PmsOrderPayHttpLog(orderId, OrderPayOp.NEW_PAY,
            true, config.getOrderUrl().toString(), reqXml));
        ResponseEntity<String> wxHttpRes =
            restClient.doPost(config.getOrderUrl(), reqXml, APPLICATION_XML);
        return handleOrderResult(wxHttpRes, orderPay, wxDetail, reqId);
    }

    private ResponseEntity<String> handleOrderResult(ResponseEntity<String> wxHttpRes,
                                                     PmsOrderPay orderPay, PmsPayDetailWx wxDetail,
                                                     String reqId) {
        final String pseudoNo = orderPay.getOrderPseudoNo();
        final String orderId = orderPay.getOrderId();
        String wxOrderResBody = wxHttpRes.getBody();
        LOGGER.debug(System.getProperty("file.encoding"));
        LOGGER.debug(Charset.defaultCharset().name());
        LOGGER.debug("orderId=" + orderId + " 微信统一下单返回\n" + wxOrderResBody);
        // get response data
        Map<String, String> wxOrderResMap = xmlParser.deserializeToMap(wxOrderResBody);
        if (wxOrderResMap == null)
            return wrapOrderErr(BAD_GATEWAY, pseudoNo, "解析返回数据失败", reqId);
        // check sign
        if (!dataBuilder.checkSign(wxOrderResMap)) {
            LOGGER.error("check sign fail " + wxOrderResMap.get(K_SIGN));
            return wrapOrderErr(BAD_GATEWAY, pseudoNo, "协议数据异常", reqId);
        }
        // check return status
        final String returnCode = wxOrderResMap.get(K_RETURN_CODE); // todo
        final String returnMsg = wxOrderResMap.get(K_RETURN_MSG);
        if (!isSuccess(returnCode)) {
            return wrapOrderErr(BAD_GATEWAY, pseudoNo,
                returnCode, returnMsg, "请求出错", reqId);
        }
        // check result
        final String resultCode = wxOrderResMap.get(K_RESULT_CODE);
        if (OrderStatus.isSuccess(resultCode)) {
            final String codeUrl = wxOrderResMap.get(K_CODE_URL);
            LOGGER.info("\n pseudoNo: " + pseudoNo +
                "  orderId: " + orderId + "\n" + codeUrl);
            orderPay.setOrderStatus(OrderStatus.SUCCESS);
            orderPay.setPayStatus(PayStatus.WAIT);
            wxDetail.setCodeUrl(codeUrl);
            wxDetail.setPrepayId(wxOrderResMap.get(K_PREPAY_ID));
            dbService.updateOrderPayRecord(orderPay);
            dbService.updateWxPayRecord(wxDetail);
            return wrapOrderOk(orderId, pseudoNo, codeUrl, reqId);
        } else {
            final String errCode = wxOrderResMap.get(K_ERR_CODE);
            final String errDesc = wxOrderResMap.get(K_ERR_CODE_DES);
            orderPay.setOrderStatus(OrderStatus.FAIL);
            orderPay.setPayStatus(PayStatus.FAIL);
            orderPay.setOrderErrCode(errCode);
            orderPay.setOrderErrDesc(errDesc);
            dbService.updateOrderPayRecord(orderPay);
            return wrapOrderErr(FORBIDDEN, pseudoNo,
                errCode, errDesc, "支付请求失败", reqId);
        }
    }

    private ResponseEntity<String> wrapOrderOk(String orderId, String pseudoNo,
                                               String codeUrl, String reqId) {
        WxClientOrderResponse res =
            new WxClientOrderResponse(OrderStatus.SUCCESS, pseudoNo, reqId);
        res.setOrderId(orderId);
        res.setCodeUrl(codeUrl);
        return jsonResponse(OK, res);
    }

    private ResponseEntity<String> wrapOrderErr(HttpStatus status, String pseudoNo,
                                                String remark, String reqId) {
        return wrapOrderErr(status, pseudoNo,
            String.valueOf(status.value()), status.getReasonPhrase(),
            remark, reqId);
    }

    private ResponseEntity<String> wrapOrderErr(HttpStatus status, String pseudoNo,
                                                String errCode, String errDesc,
                                                String remark, String reqId) {
        WxClientOrderResponse res =
            new WxClientOrderResponse(OrderStatus.FAIL, pseudoNo, reqId);
        res.setErrCode(errCode);
        res.setErrDesc(errDesc);
        res.setRemark(remark);
        return jsonResponse(status, res);
    }

    /**
     * 微信平台异步通知结果：微信->后台
     */
    @RequestMapping(value = "/callback", method = RequestMethod.POST, consumes = TEXT_XML_VALUE)
    public ResponseEntity<String> callback(@RequestBody String callbackBody) {
        LOGGER.debug("pay callback\n" + callbackBody);
        // todo check sign!
        return xmlResponse(OK, handleNotifyCallback(callbackBody));
    }

    private WxPayCallbackResponse handleNotifyCallback(String callbackBody) {
        // only easier to use
        final WxPayCallbackResponse failRes = new WxPayCallbackResponse(ReturnStatus.FAIL);
        final WxPayCallbackResponse okRes = new WxPayCallbackResponse(ReturnStatus.SUCCESS);
        // check sign
        Map<String, String> map = xmlParser.deserializeToMap(callbackBody);
        if (map == null || !dataBuilder.checkSign(map)) return failRes;
        // parse data & check return status
        WxPayCallback payCallback = xmlParser.deserialize(callbackBody, WxPayCallback.class);
        if (payCallback == null || !isSuccess(payCallback.getReturnCode())) return failRes;
        // check order id
        final String orderId = payCallback.getOutTradeNo();
        if (!(isValidOrderId(orderId) && dbService.wxPayRecordExists(orderId))) return failRes;
        // log message
        dbService.log(new PmsOrderPayHttpLog(orderId,
            OrderPayOp.NOTIFY_PAY_RESULT, true, "/wx/pay/callback", callbackBody));
        // get wx detail data
        PmsOrderPay orderPay = dbService.getOrderPayRecord(orderId); // should exists
        PmsPayDetailWx wxDetail = dbService.getWxPayRecord(orderId); // should exists
        final String receivedTranId = payCallback.getTransactionId();
        final String storedTranId = wxDetail.getTransactionId();
        // todo check other important data
        if (storedTranId == null) {
            // receive first time
            final String result = payCallback.getResultCode();
            if (PayStatus.isSuccess(result)) {
                final ZonedDateTime timeEnd = parseDateTime(payCallback.getTimeEnd());
                orderPay.setPayStatus(PayStatus.SUCCESS);
                wxDetail.setTimeEnd(timeEnd);
                orderPay.setPayCloseTime(timeEnd); // todo use which?
                wxDetail.setTransactionId(receivedTranId);
                wxDetail.setOpenid(payCallback.getOpenid());
                wxDetail.setIsSubscribe(payCallback.getIsSubscribe());
                wxDetail.setOutTradeNo(payCallback.getOutTradeNo());
                wxDetail.setBankType(payCallback.getBankType());
                wxDetail.setCashFee(payCallback.getCashFee());
                wxDetail.setCashFeeType(payCallback.getCashFeeType());
                wxDetail.setCouponFee(payCallback.getCouponFee());
            } else {
                orderPay.setPayStatus(PayStatus.FAIL);
                orderPay.setPayErrCode(payCallback.getErrCode());
                orderPay.setPayErrDesc(payCallback.getErrCodeDes());
            }
            dbService.updateOrderPayRecord(orderPay);
            dbService.updateWxPayRecord(wxDetail);
            return okRes;
        } else {
            // has received before
            if (!storedTranId.equals(receivedTranId)) {
                // shouldn't happen!
                LOGGER.error("TRANSACTION ID changed! orderID=" + orderId +
                    " tranId: store=" + storedTranId + " receive=" + receivedTranId);
            }
            return okRes;
        }
    }

    /**
     * 订单状况查询：设备->后台
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ResponseEntity<String> query(@RequestParam("orderid") String orderId) {
        LOGGER.debug("new query for " + orderId);
        HttpStatus status;
        WxClientQueryResponse res = new WxClientQueryResponse(orderId);
        if (isValidOrderId(orderId)) {
            PmsOrderPay orderPay = dbService.getOrderPayRecord(orderId);
            if (orderPay != null) {
                status = OK;
                PmsPayDetailWx wxDetail = dbService.getWxPayRecord(orderId);
                res.setOrderTime(orderPay.getOrderTime());
                res.setOrderTotalFee(orderPay.getOrderTotalFee());
                res.setPayStatus(orderPay.getPayStatus());
                res.setPayTimeStart(wxDetail.getTimeStart());
                res.setPayTimeExpire(wxDetail.getTimeExpire());
                res.setPayTimeEnd(wxDetail.getTimeEnd());
                res.setPayCashFee(wxDetail.getCashFee());
                res.setPayCouponFee(wxDetail.getCouponFee());
                res.setPayRefundFee(wxDetail.getRefundFee());
                res.setWxOpenId(wxDetail.getOpenid());
                res.setWxTransactionId(wxDetail.getTransactionId());
            } else {
                status = NOT_FOUND;
                res.setRemark("未找到记录");
            }
        } else {
            status = BAD_REQUEST;
            res.setRemark("订单号格式不符");
        }
        return jsonResponse(status, res);
    }

    /**
     * 关闭订单：设备->后台
     */
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public ResponseEntity<String> close(@RequestBody String closeReqBody){
        // todo
        return null;
    }

    private ResponseEntity<String> jsonResponse(HttpStatus status, Object obj) {
        return textResponse(status, APPLICATION_JSON, jsonParser.serialize(obj));
    }

    private ResponseEntity<String> xmlResponse(HttpStatus status, Object obj) {
        return textResponse(status, APPLICATION_XML, xmlParser.serialize(obj));
    }

    private ResponseEntity<String> textResponse(HttpStatus status, MediaType mediaType, String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new ResponseEntity<>(jsonBody, headers, status);
    }


}
