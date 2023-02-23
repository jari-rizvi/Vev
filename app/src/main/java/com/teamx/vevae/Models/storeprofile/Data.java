
package com.teamx.vevae.Models.storeprofile;

import java.util.ArrayList;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("stores")
    @Expose
    private Stores stores;
    @SerializedName("category")
    @Expose
    private ArrayList<Category> category = null;
    @SerializedName("Ratings")
    @Expose
    private ArrayList<Rating> ratings = null;

    public Stores getStores() {
        return stores;
    }

    public void setStores(Stores stores) {
        this.stores = stores;
    }

    public ArrayList<Category> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<Category> category) {
        this.category = category;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<Rating> ratings) {
        this.ratings = ratings;
    }

}
