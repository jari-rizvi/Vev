package com.teamx.vevae.DummyModel;

public class Data {

    int thumbnail;
    String name;
    String price;

    public Data(int thumbnail, String name, String price) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.price = price;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
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
}
