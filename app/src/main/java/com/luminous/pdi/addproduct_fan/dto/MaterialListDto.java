package com.luminous.pdi.addproduct_fan.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MaterialListDto {

    @SerializedName("PRODUCT_ID")
    @Expose
    private String PRODUCT_ID;
    @SerializedName("Productdescription")
    @Expose
    private String Productdescription;
    @SerializedName("InvoiceNo")
    @Expose
    private List<InvoiceNoDto> InvoiceNo = null;

    public String getPRODUCT_ID() {
        return PRODUCT_ID;
    }

    public void setPRODUCT_ID(String PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }

    public String getProductdescription() {
        return Productdescription;
    }

    public void setProductdescription(String productdescription) {
        Productdescription = productdescription;
    }

    public List<InvoiceNoDto> getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(List<InvoiceNoDto> invoiceNo) {
        InvoiceNo = invoiceNo;
    }
}
