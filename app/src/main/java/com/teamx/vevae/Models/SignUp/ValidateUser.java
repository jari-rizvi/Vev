package com.teamx.vevae.Models.SignUp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidateUser {

    @SerializedName("Flag")
    @Expose
    private Integer flag;
    @SerializedName("Message")
    @Expose
    private String message;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
