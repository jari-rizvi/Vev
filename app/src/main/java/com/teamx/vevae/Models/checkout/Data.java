
package com.teamx.vevae.Models.checkout;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("ShippingAddress")
    @Expose
    private ArrayList<ShippingAddress> shippingAddress = null;
    @SerializedName("Payment")
    @Expose
    private Payment payment;
    @SerializedName("CartItem")
    @Expose
    private ArrayList<CartItem> cartItem = null;

    public ArrayList<ShippingAddress> getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ArrayList<ShippingAddress> shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public ArrayList<CartItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(ArrayList<CartItem> cartItem) {
        this.cartItem = cartItem;
    }

}
