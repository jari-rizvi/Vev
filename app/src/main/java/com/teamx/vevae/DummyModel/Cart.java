package com.teamx.vevae.DummyModel;

public class Cart {

    String itemName;
    String itemIdentifier;
    float itemAmount;
    int itemImage;
    int quantity;


    public Cart(String itemName, String itemIdentifier, float itemAmount, int itemImage, int quantity) {
        this.itemName = itemName;
        this.itemIdentifier = itemIdentifier;
        this.itemAmount = itemAmount;
        this.itemImage = itemImage;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemIdentifier() {
        return itemIdentifier;
    }

    public void setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public float getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(float itemAmount) {
        this.itemAmount = itemAmount;
    }

    public int getItemImage() {
        return itemImage;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }
}
