package com.teamx.vevae.DummyModel;

public class OrderList {

    String orderType;
    String orderId;
    String orderAmount;
    String dateTime;

    public OrderList(String orderType, String orderId, String orderAmount, String dateTime) {
        this.orderType = orderType;
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.dateTime = dateTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
