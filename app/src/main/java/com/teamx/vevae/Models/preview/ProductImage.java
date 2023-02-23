
package com.teamx.vevae.Models.preview;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ProductImage {

    @SerializedName("imageId")
    @Expose
    private Integer imageId;
    @SerializedName("productImages")
    @Expose
    private String productImages;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getProductImages() {
        return productImages;
    }

    public void setProductImages(String productImages) {
        this.productImages = productImages;
    }

}
