
package com.teamx.vevae.Models.checkout;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Payment {

    @SerializedName("String")
    @Expose
    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

}
