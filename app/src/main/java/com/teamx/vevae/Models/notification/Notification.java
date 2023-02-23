
package com.teamx.vevae.Models.notification;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Notification {

  /*  @SerializedName("Messages")
    @Expose
    private String messages;
    *//*@SerializedName("BoldWords")
    @Expose
    private List<String> boldWords = null;*//*

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("time")
    @Expose
    private String time;

    public String getTime() {
        return time;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

  public List<String> getBoldWords() {
        return boldWords;
    }

    public void setBoldWords(List<String> boldWords) {
        this.boldWords = boldWords;
    }
 */

  @SerializedName("ReferenceType")
    @Expose
    private String referenceType;
    @SerializedName("ReferenceId")
    @Expose
    private Integer referenceId;
    @SerializedName("DisplayImage")
    @Expose
    private String displayImage;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("MessageType")
    @Expose
    private String messageType;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("BoldWords")
    @Expose
    private String boldWords;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("status")
    @Expose
    private String status;

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public String getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBoldWords() {
        return boldWords;
    }

    public void setBoldWords(String boldWords) {
        this.boldWords = boldWords;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
