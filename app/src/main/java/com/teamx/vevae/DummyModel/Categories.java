package com.teamx.vevae.DummyModel;

public class Categories {

    String name;
    String price;
    int categoryImage;

    public Categories(String name, String price, int categoryImage) {
        this.name = name;
        this.price = price;
        this.categoryImage = categoryImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }
}
