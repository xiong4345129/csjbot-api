package com.csjbot.api.pay.model;

public enum OrderStatus {
    ACCEPT,
    // REJECT,
    FAIL,
    SUCCESS;

    public static boolean isSuccess(String val) {
        return SUCCESS.name().equals(val);
    }
}
