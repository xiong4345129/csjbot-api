package com.csjbot.api.pay.model;

public enum PayStatus {
    /** 已收到设备请求，暂未向三方请求 */
    PRE,
    /** 三方支付请求已创建，等待买家付款 */
    WAIT,
    /** 支付请求失败或未创建、取消支付 */
    CANCEL,
    /** 支付请求已创建、关闭支付 */
    CLOSE,
    /** 支付超时失效 */
    EXPIRE,
    /** 支付成功 */
    SUCCESS,
    /** 支付失败或出错 */
    FAIL;

    public static boolean isSuccess(String val) {
        return SUCCESS.name().equals(val);
    }
}
