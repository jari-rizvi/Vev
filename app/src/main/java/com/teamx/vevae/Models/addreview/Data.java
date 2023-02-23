
package com.teamx.vevae.Models.addreview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("data")
    @Expose
    private ReviewData reviewData;

    public ReviewData getReviewData() {
        return reviewData;
    }

    public void setData(ReviewData reviewData) {
        this.reviewData = reviewData;
    }

}
