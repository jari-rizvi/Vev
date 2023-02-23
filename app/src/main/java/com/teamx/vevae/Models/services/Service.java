
package com.teamx.vevae.Models.services;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Service {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("serviceDetail")
    @Expose
    private String serviceDetail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceDetail() {
        return serviceDetail;
    }

    public void setServiceDetail(String serviceDetail) {
        this.serviceDetail = serviceDetail;
    }

}
