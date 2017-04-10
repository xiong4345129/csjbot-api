package com.csjbot.api.pay.model;

public enum OrderStatus {
    /**
     * 已审核接收客户端提交的下单请求
     *
     * @see PayStatus#PRE
     */
    ACCEPT,
    /**
     * 向三方创建支付订单成功、下单流程完成、等待买家付款中
     *
     * @see PayStatus#WAIT
     */
    SUCCESS,
    /** 主动取消下单 */
    CLOSE,
    /** 下单失败或出错 */
    FAIL;

    public static boolean isSuccess(String val) {
        return SUCCESS.name().equals(val);
    }
}
