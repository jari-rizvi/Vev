
package com.teamx.vevae.Models.preview;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("cart")
    @Expose
    private Integer cart;
    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("product_images")
    @Expose
    private ArrayList<ProductImage> productImages = null;
    @SerializedName("ModifierName1")
    @Expose
    private String modifierName1;
    @SerializedName("Modifiers1")
    @Expose
    private ArrayList<Modifiers1> modifiers1 = null;
    @SerializedName("ModifierName2")
    @Expose
    private String modifierName2;
    @SerializedName("Modifiers2")
    @Expose
    private ArrayList<Modifiers2> modifiers2 = null;

    public Integer getCart() {
        return cart;
    }

    public void setCart(Integer cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ArrayList<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(ArrayList<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public String getModifierName1() {
        return modifierName1;
    }

    public void setModifierName1(String modifierName1) {
        this.modifierName1 = modifierName1;
    }

    public ArrayList<Modifiers1> getModifiers1() {
        return modifiers1;
    }

    public void setModifiers1(ArrayList<Modifiers1> modifiers1) {
        this.modifiers1 = modifiers1;
    }

    public String getModifierName2() {
        return modifierName2;
    }

    public void setModifierName2(String modifierName2) {
        this.modifierName2 = modifierName2;
    }

    public ArrayList<Modifiers2> getModifiers2() {
        return modifiers2;
    }

    public void setModifiers2(ArrayList<Modifiers2> modifiers2) {
        this.modifiers2 = modifiers2;
    }

}
