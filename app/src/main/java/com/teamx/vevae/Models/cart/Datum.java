
package com.teamx.vevae.Models.cart;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {


    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cartId")
    @Expose
    private Integer cartId;

    @SerializedName("modifier1")
    @Expose
    private String modifier1;
    @SerializedName("modifier2")
    @Expose
    private String modifier2;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("ProductId")
    @Expose
    private Integer productId;
    @SerializedName("productImages")
    @Expose
    private String productImages;
    @SerializedName("price")
    @Expose
    private Integer price;

    private float totalAmount;

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductImages() {
        return productImages;
    }

    public void setProductImages(String productImages) {
        this.productImages = productImages;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}
