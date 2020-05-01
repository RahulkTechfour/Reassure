package com.luminous.pdi.addproductLight.LightRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceQntyEntity {

    @SerializedName("BillingDocumentNumner")
    @Expose
    private String BillingDocumentNumner;
    @SerializedName("AvailableQuantity")
    @Expose
    private String AvailableQuantity;

    public InvoiceQntyEntity(String invoiceNo_, String availableQuantity_, String pdiQnty_) {
        this.BillingDocumentNumner = invoiceNo_;
            this.AvailableQuantity = availableQuantity_;
            this.SoldQuantity = pdiQnty_;
        }


    public String getSoldQuantity() {
        return SoldQuantity;
    }

    public void setSoldQuantity(String soldQuantity) {
        SoldQuantity = soldQuantity;
    }

    private String SoldQuantity;


    public String getBillingDocumentNumner() {
        return BillingDocumentNumner;
    }

    public void setBillingDocumentNumner(String billingDocumentNumner) {
        BillingDocumentNumner = billingDocumentNumner;
    }

    public String getAvailableQuantity() {
        return AvailableQuantity;
    }

    public void setAvailableQuantity(String availableQuantity) {
        AvailableQuantity = availableQuantity;
    }
}