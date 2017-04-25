package com.csjbot.api.pay.model;

import java.time.ZonedDateTime;

/**
 * @author
 */
public class PmsOrderPayHttpLog{
    /**
     * 数据库记录产生时间
     */
    private ZonedDateTime createTime;

    /**
     * 正式订单流水号或退款单流水号
     */
    private String id;

    /**
     * 服务操作名
     */
    private String operation;

    /**
     * 请求还是回复
     */
    private boolean isRequest;

    /**
     * http请求路径
     */
    private String path;

    /**
     * http请求消息体
     */
    private String body;

    public PmsOrderPayHttpLog() { }

    public PmsOrderPayHttpLog(String id) {
        this.id = id;
    }

    public PmsOrderPayHttpLog(String id, OrderPayOp operation,
                              boolean isRequest, String path, String body) {
        this.id = id;
        this.operation = operation.name();
        this.isRequest = isRequest;
        this.path = path;
        this.body = body;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public String getId() {
        return id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(OrderPayOp operation) {
        this.operation = operation.name();
    }

    public boolean isRequest() {
        return isRequest;
    }

    public void setRequest(boolean request) {
        isRequest = request;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

