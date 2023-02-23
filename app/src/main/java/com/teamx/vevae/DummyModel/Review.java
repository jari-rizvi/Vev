package com.teamx.vevae.DummyModel;

public class Review {

    int thumbnail;
    String name;
    String description;
    String date;
    float rating;


    public Review(int thumbnail, String name, String description, String date, float rating) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.description = description;
        this.date = date;
        this.rating = rating;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
