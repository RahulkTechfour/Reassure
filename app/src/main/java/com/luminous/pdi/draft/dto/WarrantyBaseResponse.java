package com.luminous.pdi.draft.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.luminous.pdi.addproductLight.LightRes.Datum;

import java.io.Serializable;
import java.util.List;

public class WarrantyBaseResponse implements Serializable {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("Data")
    @Expose
    private List<WarrantyDto> dataResult = null;

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

    public List<WarrantyDto> getDataResult() {
        return dataResult;
    }

    public void setDataResult(List<WarrantyDto> dataResult) {
        this.dataResult = dataResult;
    }
}
