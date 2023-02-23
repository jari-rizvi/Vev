
package com.teamx.vevae.Models.storeprofile;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Category {

    @SerializedName("categoriesId")
    @Expose
    private Integer categoriesId;
    @SerializedName("categoriesName")
    @Expose
    private String categoriesName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("listbycategory")
    @Expose
    private ArrayList<Listbycategory> listbycategory = null;

    public Integer getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(Integer categoriesId) {
        this.categoriesId = categoriesId;
    }

    public String getCategoriesName() {
        return categoriesName;
    }

    public void setCategoriesName(String categoriesName) {
        this.categoriesName = categoriesName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Listbycategory> getListbycategory() {
        return listbycategory;
    }

    public void setListbycategory(ArrayList<Listbycategory> listbycategory) {
        this.listbycategory = listbycategory;
    }

}
