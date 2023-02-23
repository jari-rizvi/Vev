
package com.teamx.vevae.Models.services;

import java.util.ArrayList;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("service")
    @Expose
    private ArrayList<Service> service = null;
    @SerializedName("Service_images")
    @Expose
    private ArrayList<ServiceImage> serviceImages = null;
    @SerializedName("servicesDates")
    @Expose
    private ArrayList<ServicesDate> servicesDates = null;
    @SerializedName("servicesData")
    @Expose
    private ArrayList<ServicesDatum> servicesData = null;

    public ArrayList<Service> getService() {
        return service;
    }

    public void setService(ArrayList<Service> service) {
        this.service = service;
    }

    public ArrayList<ServiceImage> getServiceImages() {
        return serviceImages;
    }

    public void setServiceImages(ArrayList<ServiceImage> serviceImages) {
        this.serviceImages = serviceImages;
    }

    public ArrayList<ServicesDate> getServicesDates() {
        return servicesDates;
    }

    public void setServicesDates(ArrayList<ServicesDate> servicesDates) {
        this.servicesDates = servicesDates;
    }

    public ArrayList<ServicesDatum> getServicesData() {
        return servicesData;
    }

    public void setServicesData(ArrayList<ServicesDatum> servicesData) {
        this.servicesData = servicesData;
    }

}
