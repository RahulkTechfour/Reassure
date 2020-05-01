package com.luminous.pdi.addproductLight.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceQntyEntity {

    @SerializedName("BillingDocumentNumner")
    @Expose
    private String BillingDocumentNumner;
    @SerializedName("AvailableQuantity")
    @Expose
    private String AvailableQuantity;

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
