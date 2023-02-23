
package com.teamx.vevae.Models.storeprofile;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Stores {

    @SerializedName("storeId")
    @Expose
    private Integer storeId;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("storedetail")
    @Expose
    private String storedetail;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getStoredetail() {
        return storedetail;
    }

    public void setStoredetail(String storedetail) {
        this.storedetail = storedetail;
    }

}
