package com.csjbot.pay.controller;

import com.csjbot.pay.model.*;
import com.csjbot.pay.service.MediaTypeParser;
import com.csjbot.pay.service.OrderPayDBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

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
    private final String wxOrderUrlStr = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private final URI wxOrderUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final OrderPayDBService dbService;
    private final MediaTypeParser mapper;
    private final WxOrderRequestBuilder builder;

    public WxController(WxConfig config,
                        OrderPayDBService dbService,
                        MediaTypeParser mapper) {
        this.dbService = dbService;
        this.mapper = mapper;
        this.builder = new WxOrderRequestBuilder(config, dbService);
        try {
            wxOrderUrl = new URI(wxOrderUrlStr);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("uri parse", e);
        }
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
        PmsOrderPay orderPay = builder.buildData(orderReq, WxTradeType.NATIVE);
        if (orderPay != null) {
            orderPay.setOrderRequestText(orderReqBody);
            dbService.newOrder(orderPay);
            // send http request
            final String orderId = orderPay.getOrderId();
            ResponseEntity<String> wxHttpRes =
                doPost(wxOrderUrl, orderPay.getPayRequestText(), APPLICATION_XML);
            clientHttpRes = handleOrderResult(wxHttpRes, orderId, pseudoNo, reqId);
        } else {
            clientHttpRes = wrapErr(BAD_REQUEST, "下单请求解析失败", pseudoNo, reqId);// todo
        }
        return clientHttpRes;
    }

    private ResponseEntity<String> handleOrderResult(ResponseEntity<String> wxHttpRes,
                                                     String orderId, String pseudoNo,
                                                     String reqId) {
        ResponseEntity<String> clientHttpRes;
        if (OK == wxHttpRes.getStatusCode()) {
            String wxOrderResBody = wxHttpRes.getBody();
            WxOrderResponse wxOrderRes =
                mapper.deserialize(wxOrderResBody, WxOrderResponse.class, FMT_XML);
            if (wxOrderRes != null) {
                if (isSuccess(wxOrderRes)) {
                    final String codeUrl = wxOrderRes.getCodeUrl();
                    final String prepayId = wxOrderRes.getPrepayId();
                    // todo update in db
                    clientHttpRes = wrapOk(orderId, pseudoNo, codeUrl, reqId);
                } else {
                    clientHttpRes = wrapErr(FORBIDDEN,
                        "向微信请求下单失败，错误码" + wxOrderRes.getErrCode(),
                        pseudoNo, reqId);
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

    private static boolean isSuccess(WxOrderResponse res) {
        return SUCCESS.equals(res.getResultCode()) && SUCCESS.equals(res.getResultCode());
    }

    // todo
    private ResponseEntity<String> wrapOk(String orderId, String pseudoNo,
                                          String codeUrl, String reqId) {
        WxClientOrderResponse.Data data =
            new WxClientOrderResponse.Data(orderId, pseudoNo, codeUrl);
        WxClientOrderResponse res =
            new WxClientOrderResponse(OrderStatus.SUCCESS.name(), data);
        res.setId(reqId);
        return jsonResponse(OK, res);
    }

    private ResponseEntity<String> wrapErr(HttpStatus status, String msg,
                                           String pseudoNo, String reqId) {
        WxClientOrderResponse.Error error =
            new WxClientOrderResponse.Error(status.value(), msg);
        error.setOrderPseudoNo(pseudoNo);
        WxClientOrderResponse res =
            new WxClientOrderResponse(OrderStatus.FAIL.name(), error);
        res.setId(reqId);
        return jsonResponse(status, res);
    }

    /**
     * 微信平台异步通知结果
     */
    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public void callback(@RequestBody String callbackBody) {
        LOGGER.debug("callback " + callbackBody);

    }

    private ResponseEntity<String> jsonResponse(HttpStatus status, Object obj) {
        return jsonResponse(status, mapper.serialize(obj, FMT_JSON));
    }

    private ResponseEntity<String> jsonResponse(HttpStatus status, String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        return new ResponseEntity<String>(jsonBody, headers, status);
    }

}
