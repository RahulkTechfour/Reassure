package com.luminous.pdi.addproduct_fan.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MaterialBaseResponse implements Serializable {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("product_details")
    @Expose
    private List<MaterialListDto> product_details = null;

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

    public List<MaterialListDto> getProduct_details() {
        return product_details;
    }

    public void setProduct_details(List<MaterialListDto> product_details) {
        this.product_details = product_details;
    }
}
