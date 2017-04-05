package com.csjbot.api.pay.controller;

import com.csjbot.api.pay.model.*;
import com.csjbot.api.pay.service.MediaTypeParser;
import com.csjbot.api.pay.service.OrderPayDBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
    private static final String SUCCESS = "SUCCESS"; // default
    private static final String FAIL = "FAIL";

    // todo
    // private final String wxOrderUrlStr = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";
    private final URI wxOrderUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final OrderPayDBService dbService;
    private final MediaTypeParser mapper;
    private final WxPayRequestBuilder builder;

    public WxController(WxConfig config,
                        OrderPayDBService dbService,
                        MediaTypeParser mapper) {
        wxOrderUrl = config.getOrderUrl();
        if (wxOrderUrl == null) throw new NullPointerException("order url");
        this.dbService = dbService;
        this.mapper = mapper;
        this.builder = new WxPayRequestBuilder(config, dbService);
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
     * 二维码支付
     */
    @RequestMapping(value = "/qr", method = RequestMethod.POST)
    public ResponseEntity<String> orderQR(@RequestBody String orderReqBody) {
        LOGGER.debug("qr order request " + orderReqBody);
        ResponseEntity<String> clientHttpRes;
        WxClientOrderRequest orderReq =
            mapper.deserialize(orderReqBody, WxClientOrderRequest.class, FMT_JSON);
        final String pseudoNo = orderReq.getData().getOrderPseudoNo();
        final String reqId = orderReq.getId();
        PmsOrderPay orderPay = null;
        try {
            orderPay = builder.buildData(orderReq, WxTradeType.NATIVE);
            if (orderPay != null) {
                LOGGER.debug("orderId="+orderPay.getOrderId()+
                    " 微信支付请求\n"+orderPay.getPayRequestText());
                orderPay.setOrderRequestText(orderReqBody); // keep track
                dbService.newOrder(orderPay);
                ResponseEntity<String> wxHttpRes =
                    doPost(wxOrderUrl, orderPay.getPayRequestText(), APPLICATION_XML);
                clientHttpRes = handleOrderResult(wxHttpRes, orderPay, reqId);
            } else {
                clientHttpRes = wrapErr(BAD_REQUEST, "下单请求解析失败", pseudoNo, reqId);// todo
            }
        } catch (Exception e) {
            LOGGER.error("build data" + reqId, e);
            clientHttpRes = wrapErr(INTERNAL_SERVER_ERROR, "下单请求解析失败", pseudoNo, reqId);
        }
        return clientHttpRes;
    }

    private ResponseEntity<String> handleOrderResult(ResponseEntity<String> wxHttpRes,
                                                     PmsOrderPay orderPay, String reqId) {
        ResponseEntity<String> clientHttpRes;
        final String pseudoNo = orderPay.getOrderPseudoNo();
        final String orderId = orderPay.getOrderId();
        if (OK == wxHttpRes.getStatusCode()) {
            String wxOrderResBody = wxHttpRes.getBody();
            LOGGER.debug("orderId=" + orderId + " 微信统一下单返回\n" + wxOrderResBody);
            WxPayResponse wxOrderRes =
                mapper.deserialize(wxOrderResBody, WxPayResponse.class, FMT_XML);
            if (wxOrderRes != null) {
                if (isSuccess(wxOrderRes)) {
                    orderPay.setOrderStatus(OrderStatus.SUCCESS);
                    orderPay.setPayStatus(PayStatus.WAIT);
                    orderPay.setPayCodeUrl(wxOrderRes.getCodeUrl());
                    orderPay.setPrePayId(wxOrderRes.getPrepayId());
                    dbService.updateOrder(orderPay);
                    clientHttpRes = wrapOk(orderPay, reqId);
                } else {
                    final String errCode = wxOrderRes.getReturnCode(); // todo
                    final String errDesc = wxOrderRes.getReturnMsg();
                    orderPay.setOrderStatus(OrderStatus.FAIL);
                    orderPay.setPayStatus(PayStatus.FAIL);
                    orderPay.setOrderErrCode(errCode);
                    orderPay.setOrderErrDesc(errDesc);
                    dbService.updateOrder(orderPay);
                    clientHttpRes = wrapErr(FORBIDDEN,
                        "向微信请求支付失败，错误码: " + errCode, pseudoNo, reqId);
                }
            } else {
                LOGGER.error("json error");
                clientHttpRes = wrapErr(BAD_GATEWAY, "协议异常", pseudoNo, reqId);
            }
        } else {
            clientHttpRes = wrapErr(BAD_GATEWAY, "向微信请求失败", pseudoNo, reqId);
        }
        return clientHttpRes;
    }

    private static boolean isSuccess(WxPayResponse res) {
        return SUCCESS.equals(res.getResultCode()) && SUCCESS.equals(res.getResultCode());
    }

    private ResponseEntity<String> wrapOk(PmsOrderPay orderPay, String reqId) {
        return wrapOk(orderPay.getOrderId(), orderPay.getOrderPseudoNo(),
            orderPay.getPayCodeUrl(), reqId);
    }

    // todo
    private ResponseEntity<String> wrapOk(String orderId, String pseudoNo,
                                          String codeUrl, String reqId) {
        WxClientOrderResponse.Data data =
            new WxClientOrderResponse.Data(orderId, pseudoNo, codeUrl);
        WxClientOrderResponse res =
            new WxClientOrderResponse(OrderStatus.SUCCESS, data);
        res.setId(reqId);
        return jsonResponse(OK, res);
    }

    private ResponseEntity<String> wrapErr(HttpStatus status, String msg,
                                           String pseudoNo, String reqId) {
        WxClientOrderResponse.Error error =
            new WxClientOrderResponse.Error(status.value(), msg);
        error.setOrderPseudoNo(pseudoNo);
        WxClientOrderResponse res =
            new WxClientOrderResponse(OrderStatus.FAIL, error);
        res.setId(reqId);
        return jsonResponse(status, res);
    }

    /**
     * 微信平台异步通知结果
     */
    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public void callback(@RequestBody String callbackBody) {
        LOGGER.debug("callback " + callbackBody);
        WxPayCallback payCallback = mapper.deserialize(callbackBody, WxPayCallback.class, FMT_JSON);
        if (SUCCESS.equals(payCallback.getReturnCode())) {
            final String orderId = payCallback.getOutTradeNo();
            PmsOrderPay orderPay = new PmsOrderPay(orderId);
            orderPay.setPayStatus(PayStatus.SUCCESS);
        } else {

        }
    }

    // 获得订单状态
    @RequestMapping(value = "/getOrderStatus", method = RequestMethod.GET)
    public ResponseEntity<String> getAllGroup(@RequestParam("order_id") String orderId) {
        Map<String, String> msgMap = new HashMap<>();
        String orderStatus = dbService.getOrderStatus(orderId);
        msgMap.put("order_id", orderId);
        msgMap.put("order_status", orderStatus);
        msgMap.put("message", "ok");
        return jsonResponse(OK, msgMap);
    }

    private ResponseEntity<String> jsonResponse(HttpStatus status, Object obj) {
        return jsonResponse(status, mapper.serialize(obj, FMT_JSON));
    }

    private ResponseEntity<String> jsonResponse(HttpStatus status, String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        return new ResponseEntity<>(jsonBody, headers, status);
    }

}
