
package com.teamx.vevae.Models.orderList;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Datum {


    @SerializedName("orderId")
    @Expose
    private Integer orderId;

    @SerializedName("orderDate")
    @Expose
    private String orderDate;

    @SerializedName("total")
    @Expose
    private Integer price;

    @SerializedName("type")
    @Expose
    private String type;


    @SerializedName("OrderCode")
    @Expose
    private String OrderCode;

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}
