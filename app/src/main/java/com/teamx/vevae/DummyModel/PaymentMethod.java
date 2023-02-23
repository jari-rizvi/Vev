package com.teamx.vevae.DummyModel;

public class PaymentMethod {

    int paymentId;
    int paymentImage;
    String paymentName;
    Boolean isSelected = false;

    public PaymentMethod(int paymentId, int paymentImage, String paymentName) {
        this.paymentId = paymentId;
        this.paymentImage = paymentImage;
        this.paymentName = paymentName;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getPaymentImage() {
        return paymentImage;
    }

    public void setPaymentImage(int paymentImage) {
        this.paymentImage = paymentImage;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
