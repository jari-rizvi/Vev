
package com.teamx.vevae.Models.orderDetails;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Orderitem {

    @SerializedName("Modifier1")
    @Expose
    private String modifier1;

    @SerializedName("Modifier2")
    @Expose
    private String modifier2;

    @SerializedName("Quantity")
    @Expose
    private Integer quantity;

    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("total_price")
    @Expose
    private Integer itemPrice;


    @SerializedName("total")
    @Expose
    private Integer total;


    public Integer getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Integer itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getModifier1() {
        return modifier1;
    }

    public void setModifier1(String modifier1) {
        this.modifier1 = modifier1;
    }

    public String getModifier2() {
        return modifier2;
    }

    public void setModifier2(String modifier2) {
        this.modifier2 = modifier2;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
