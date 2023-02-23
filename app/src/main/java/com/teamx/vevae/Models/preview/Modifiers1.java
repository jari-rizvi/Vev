
package com.teamx.vevae.Models.preview;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Modifiers1 {

    @SerializedName("Value")
    @Expose
    private String value;
    @SerializedName("ModifierId")
    @Expose
    private Integer modifierId;

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getModifierId() {
        return modifierId;
    }

    public void setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
    }

}
