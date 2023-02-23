
package com.teamx.vevae.Models.services;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ServicesDate {

    @SerializedName("ServicesDate")
    @Expose
    private String servicesDate;
    @SerializedName("id")
    @Expose
    private Integer id;

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getServicesDate() {
        return servicesDate;
    }

    public void setServicesDate(String servicesDate) {
        this.servicesDate = servicesDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
