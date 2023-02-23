
package com.teamx.vevae.Models.storeprofile;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Rating {

    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("UserImage")
    @Expose
    private String userImage;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;


    public Rating(String userName, String userImage, String comments, String rating, String createdAt) {
        this.userName = userName;
        this.userImage = userImage;
        this.comments = comments;
        this.rating = rating;

        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
