package com.teamx.vevae.DummyModel;

public class OrderDetails {

    String itemName;
    String itemIdentifier;
    int quantity;

    public OrderDetails(String itemName, String itemIdentifier, int quantity) {
        this.itemName = itemName;
        this.itemIdentifier = itemIdentifier;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
