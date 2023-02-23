
package com.teamx.vevae.Models.orderDetails;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("OrderDetail")
    @Expose
    private ArrayList<OrderDetail> orderDetail = null;
    @SerializedName("Address")
    @Expose
    private ArrayList<Address> address = null;
    @SerializedName("Orderitem")
    @Expose
    private ArrayList<Orderitem> orderitem = null;


    public ArrayList<OrderDetail> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(ArrayList<OrderDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public ArrayList<Address> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Address> address) {
        this.address = address;
    }

    public ArrayList<Orderitem> getOrderitem() {
        return orderitem;
    }

    public void setOrderitem(ArrayList<Orderitem> orderitem) {
        this.orderitem = orderitem;
    }

}
