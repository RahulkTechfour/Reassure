package com.luminous.pdi.draft.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WarrantyDto {

    @SerializedName("PRODUCT_ID")
    @Expose
    private String PRODUCT_ID;
    @SerializedName("Warranty")
    @Expose
    private String Warranty;
    @SerializedName("Productdescription")
    @Expose
    private String Productdescription;

    public String getPRODUCT_ID() {
        return PRODUCT_ID;
    }

    public void setPRODUCT_ID(String PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }

    public String getWarranty() {
        return Warranty;
    }

    public void setWarranty(String warranty) {
        Warranty = warranty;
    }

    public String getProductdescription() {
        return Productdescription;
    }

    public void setProductdescription(String productdescription) {
        Productdescription = productdescription;
    }
}
