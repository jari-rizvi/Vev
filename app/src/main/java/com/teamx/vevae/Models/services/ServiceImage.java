
package com.teamx.vevae.Models.services;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ServiceImage {

    @SerializedName("imageId")
    @Expose
    private Integer imageId;
    @SerializedName("servicesImage")
    @Expose
    private String servicesImage;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getServicesImage() {
        return servicesImage;
    }

    public void setServicesImage(String servicesImage) {
        this.servicesImage = servicesImage;
    }

}
