package com.teamx.vevae.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddAddress {

    @SerializedName("Flag")
    @Expose
    private Integer flag;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Errors")
    @Expose
    private Object errors;


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

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }
}
