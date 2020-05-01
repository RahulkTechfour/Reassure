package com.luminous.pdi.addproductLight.LightRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InvoiceQntyBaseResponse {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Token")
    @Expose
    private String token;

    private List<InvoiceQntyEntity> get_invoice_qty_sold;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<InvoiceQntyEntity> getGet_invoice_qty_sold() {
        return get_invoice_qty_sold;
    }

    public void setGet_invoice_qty_sold(List<InvoiceQntyEntity> get_invoice_qty_sold) {
        this.get_invoice_qty_sold = get_invoice_qty_sold;
    }
}