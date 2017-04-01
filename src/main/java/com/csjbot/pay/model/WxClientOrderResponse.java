package com.csjbot.pay.model;

public class WxClientOrderResponse {
    private String id;

    private String status;
    private Data data;
    private Error error;

    public WxClientOrderResponse(String status, Data data) {
        this.status = status;
        this.data = data;
    }

    public WxClientOrderResponse(String status, Error error) {
        this.status = status;
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    public Error getError() {
        return error;
    }

    public static class Data {
        private final String orderPseudoNo;
        private final String orderId;
        private final String codeUrl;

        public Data(String orderId,
                    String orderPseudoNo,
                    String codeUrl) {
            this.orderPseudoNo = orderPseudoNo;
            this.orderId = orderId;
            this.codeUrl = codeUrl;
        }

        public String getOrderPseudoNo() {
            return orderPseudoNo;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getCodeUrl() {
            return codeUrl;
        }
    }

    public static class Error {
        private String orderPseudoNo;
        private final Integer code;
        private final String message;

        public Error(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getOrderPseudoNo() {
            return orderPseudoNo;
        }

        public void setOrderPseudoNo(String orderPseudoNo) {
            this.orderPseudoNo = orderPseudoNo;
        }

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
