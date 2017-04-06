package com.csjbot.api.pay.model;

public enum  ReturnStatus {
    SUCCESS,
    FAIL;

    public static boolean isSuccess(String val){
        return SUCCESS.name().equals(val);
    }
}
