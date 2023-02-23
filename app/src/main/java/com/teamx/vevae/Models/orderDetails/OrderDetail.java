
package com.teamx.vevae.Models.orderDetails;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderDetail {

    @SerializedName("OrderCode")
    @Expose
    private String orderCode;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;

    @SerializedName("PaymentMethodId")
    @Expose
    private String paymentMethodId;

    @SerializedName("transaction_id")
    @Expose
    private String transaction_id;


    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }


}
