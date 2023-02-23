
package com.teamx.vevae.Models.checkout;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CheckOut {

    @SerializedName("Flag")
    @Expose
    private Integer flag;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private Data data;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

}
