package com.csjbot.api.pay.controller;

import com.csjbot.api.pay.model.*;
import com.csjbot.api.pay.service.*;
import com.csjbot.api.pay.util.OrderIdGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/pay/wx")
public class WxController {
    @RequestMapping(produces = TEXT_PLAIN_VALUE)
    @ResponseStatus(OK)
    public String hello() {
        return "hello from " + WxController.class.getName();
    }

    @RequestMapping("/echo")
    @ResponseStatus(OK)
    public String echo(@RequestParam("word") String echo) {
        return echo;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(WxController.class);
    private static final String FMT_JSON = "JSON";
    private static final String FMT_XML = "XML";

    // todo
    private final URI wxOrderUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final WxPayDBService dbService;
    private final WxPayRequestBuilder reqBuilder;
    private final MediaTypeParser mapper;

    @Autowired
    public WxController(WxConfig config,
                        @Qualifier("wxPayDBServiceQ") WxPayDBService wxPayDBService,
                        MediaTypeParser mapper) {
        wxOrderUrl = config.getOrderUrl();
        if (wxOrderUrl == null) throw new NullPointerException("order url");
        this.dbService = wxPayDBService;
        this.mapper = mapper;
        this.reqBuilder = new WxPayRequestBuilder(config, wxPayDBService); // todo
        this.restTemplate.getMessageConverters()
            .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

    private ResponseEntity<String> doPost(URI uri,
                                          String body,
                                          MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        RequestEntity<String> req = new RequestEntity<>(body, HttpMethod.POST, uri);
        ResponseEntity<String> res = restTemplate.postForEntity(uri, req, String.class);
        return res;
    }

    /**
     * 二维码支付下单请求：设备->后台
     */
    @RequestMapping(value = "/qr", method = RequestMethod.POST)
    public ResponseEntity<String> orderQR(@RequestBody String orderReqBody) {
        LOGGER.debug("qr order request " + orderReqBody);
        ResponseEntity<String> clientHttpRes;
        WxClientOrderRequest orderReq =
            mapper.deserialize(orderReqBody, WxClientOrderRequest.class, FMT_JSON);
        final String pseudoNo = orderReq.getData().getOrderPseudoNo();
        final String reqId = orderReq.getId();
        try {
            PmsOrderPay orderPay = reqBuilder.buildData(orderReq, WxTradeType.NATIVE);
            if (orderPay != null) {
                LOGGER.debug("orderId=" + orderPay.getOrderId() +
                    " 微信支付请求\n" + orderPay.getPayRequestText());
                orderPay.setOrderRequestText(orderReqBody); // keep track
                dbService.newOrder(orderPay);
                dbService.insertOrderList(orderPay.getOrderDetails());
                ResponseEntity<String> wxHttpRes =
                    doPost(wxOrderUrl, orderPay.getPayRequestText(), APPLICATION_XML);
                clientHttpRes = handleOrderResult(wxHttpRes, orderPay, reqId);
            } else {
                clientHttpRes = wrapOrderErr(BAD_REQUEST, "下单请求解析失败", pseudoNo, reqId);// todo
            }
        } catch (Exception e) {
            LOGGER.error("build data" + reqId, e);
            clientHttpRes = wrapOrderErr(INTERNAL_SERVER_ERROR, "下单请求解析失败", pseudoNo, reqId);
        }
        return clientHttpRes;
    }

    private ResponseEntity<String> handleOrderResult(ResponseEntity<String> wxHttpRes,
                                                     PmsOrderPay orderPay, String reqId) {
        ResponseEntity<String> clientHttpRes;
        final String pseudoNo = orderPay.getOrderPseudoNo();
        final String orderId = orderPay.getOrderId();
        String wxOrderResBody = wxHttpRes.getBody();
        LOGGER.debug(System.getProperty("file.encoding"));
        LOGGER.debug(Charset.defaultCharset().name());
        LOGGER.debug("orderId=" + orderId + " 微信统一下单返回\n" + wxOrderResBody);
        WxPayResponse wxOrderRes =
            mapper.deserialize(wxOrderResBody, WxPayResponse.class, FMT_XML);
        if (wxOrderRes != null) {
            final String returnCode = wxOrderRes.getReturnCode(); // todo
            final String returnMsg = wxOrderRes.getReturnMsg();
            final String result = wxOrderRes.getResultCode();
            if (ReturnStatus.isSuccess(returnCode)) {
                if (OrderStatus.isSuccess(result)) {
                    LOGGER.info("\n pseudoNo: " + pseudoNo +
                        "  orderId: " + orderId + "\n" + wxOrderRes.getCodeUrl());
                    orderPay.setOrderStatus(OrderStatus.SUCCESS);
                    orderPay.setPayStatus(PayStatus.WAIT);
                    orderPay.setPayCodeUrl(wxOrderRes.getCodeUrl());
                    orderPay.setPrepayId(wxOrderRes.getPrepayId());
                    dbService.updateOrder(orderPay);
                    clientHttpRes = wrapOrderOk(orderPay, reqId);
                } else {
                    final String errCode = wxOrderRes.getErrCode();
                    final String errDesc = wxOrderRes.getErrCodeDes();
                    orderPay.setOrderStatus(OrderStatus.FAIL);
                    orderPay.setPayStatus(PayStatus.FAIL);
                    orderPay.setOrderErrCode(errCode);
                    orderPay.setOrderErrDesc(errDesc);
                    dbService.updateOrder(orderPay);
                    clientHttpRes = wrapOrderErr(FORBIDDEN, pseudoNo,
                        errCode, errDesc, "支付请求失败", reqId);
                }
            } else {
                clientHttpRes = wrapOrderErr(BAD_GATEWAY, pseudoNo,
                    returnCode, returnMsg, "通信异常", reqId);
            }
        } else {
            LOGGER.error("json error");
            clientHttpRes = wrapOrderErr(BAD_GATEWAY, pseudoNo, "通信异常", reqId);
        }
        return clientHttpRes;
    }

    private ResponseEntity<String> wrapOrderOk(PmsOrderPay orderPay, String reqId) {
        return wrapOrderOk(orderPay.getOrderId(), orderPay.getOrderPseudoNo(),
            orderPay.getPayCodeUrl(), reqId);
    }

    // todo
    private ResponseEntity<String> wrapOrderOk(String orderId, String pseudoNo,
                                               String codeUrl, String reqId) {
        WxClientOrderResponse res =
            new WxClientOrderResponse(OrderStatus.SUCCESS, pseudoNo);
        res.setId(reqId);
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
            new WxClientOrderResponse(OrderStatus.FAIL, pseudoNo);
        res.setId(reqId);
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
        WxPayCallbackResponse res = new WxPayCallbackResponse(ReturnStatus.FAIL);
        WxPayCallback payCallback = mapper.deserialize(callbackBody, WxPayCallback.class, FMT_XML);
        if (payCallback != null && ReturnStatus.isSuccess(payCallback.getReturnCode())) {
            final String orderId = payCallback.getOutTradeNo();
            if (OrderIdGen.check(orderId) && dbService.orderExists(orderId)) {
                if (!dbService.resultExists(orderId)) {
                    PmsOrderPay orderPay = new PmsOrderPay(orderId);
                    final String result = payCallback.getResultCode();
                    if (PayStatus.isSuccess(result)) {
                        orderPay.setPayStatus(PayStatus.SUCCESS);
                        orderPay.setPayTimeEnd(parseDate(payCallback.getTimeEnd()));
                    } else {
                        orderPay.setPayStatus(PayStatus.FAIL);
                        orderPay.setPayErrCode(payCallback.getErrCode());
                        orderPay.setPayErrDesc(payCallback.getErrCodeDes());
                    }
                    dbService.updateOrder(orderPay);
                    dbService.storePayResult(payCallback);
                }
                res = new WxPayCallbackResponse(ReturnStatus.SUCCESS);
            }
        }
        // todo check sign!
        return xmlResponse(OK, res);
    }

    /**
     * 订单状况查询：设备->后台
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ResponseEntity<String> getAllGroup(@RequestParam("orderid") String orderId) {
        HttpStatus status;
        WxQueryResponse res = new WxQueryResponse(orderId);
        if (OrderIdGen.check(orderId)) {
            PmsOrderPay orderPay = dbService.getOrderPayRecord(orderId);
            if (orderPay != null) {
                status = OK;
                res.setOrderTime(formatDate(orderPay.getCreateTime()));
                res.setPayStatus(orderPay.getPayStatus());
                Date payEndTime = orderPay.getPayTimeEnd();
                if (payEndTime != null) res.setPayTimeEnd(formatDate(payEndTime));
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

    // todo do not use Date
    private String formatDate(Date date) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dt.format(date);
    }

    private Date parseDate(String str) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = dt.parse(str);
        } catch (ParseException e) {
            LOGGER.error("parse date from " + str, e);
        }
        return date;
    }

    private ResponseEntity<String> jsonResponse(HttpStatus status, Object obj) {
        return textResponse(status, APPLICATION_JSON, mapper.serialize(obj, FMT_JSON));
    }

    private ResponseEntity<String> xmlResponse(HttpStatus status, Object obj) {
        return textResponse(status, APPLICATION_XML, mapper.serialize(obj, FMT_XML));
    }

    private ResponseEntity<String> textResponse(HttpStatus status, MediaType mediaType, String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new ResponseEntity<>(jsonBody, headers, status);
    }


}
