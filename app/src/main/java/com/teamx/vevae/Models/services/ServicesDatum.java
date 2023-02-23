
package com.teamx.vevae.Models.services;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ServicesDatum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("Servicesduration")
    @Expose
    private String servicesduration;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("servicesId")
    @Expose
    private Integer servicesId;
    @SerializedName("serviceType")
    @Expose
    private String serviceType;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount=0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServicesduration() {
        return servicesduration;
    }

    public void setServicesduration(String servicesduration) {
        this.servicesduration = servicesduration;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getServicesId() {
        return servicesId;
    }

    public void setServicesId(Integer servicesId) {
        this.servicesId = servicesId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}
