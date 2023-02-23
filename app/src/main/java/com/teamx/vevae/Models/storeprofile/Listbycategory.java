
package com.teamx.vevae.Models.storeprofile;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Listbycategory {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("storeType")
    @Expose
    private String storeType;
    @SerializedName("storeId")
    @Expose
    private Integer storeId;
    @SerializedName("categoriesId")
    @Expose
    private Integer categoriesId;
    @SerializedName("productImages")
    @Expose
    private String productImages=null;
    @SerializedName("servicesImage")
    @Expose
    private String servicesImage = null;
    @SerializedName("price")
    @Expose
    private Integer price=null;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getServicesImage() {
        return servicesImage;
    }

    public void setServicesImage(String servicesImage) {
        this.servicesImage = servicesImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(Integer categoriesId) {
        this.categoriesId = categoriesId;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
