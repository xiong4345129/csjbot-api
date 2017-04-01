package com.csjbot.pay.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 
 */
public class PmsOrderPayWithBLOBs extends PmsOrderPay implements Serializable {
    private String orderRequest;

    private String payRequest;

    private static final long serialVersionUID = 1L;

    public PmsOrderPayWithBLOBs(String orderId) {
        super(orderId);
    }


    public String getOrderRequestText() {
        return orderRequest;
    }

    public void setOrderRequestText(String orderRequest) {
        this.orderRequest = orderRequest;
    }

    public String getPayRequestText() {
        return payRequest;
    }

    public void setPayRequestText(String payRequest) {
        this.payRequest = payRequest;
    }
}