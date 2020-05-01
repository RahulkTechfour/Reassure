package com.luminous.pdi.home.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpComingVisitBaseResponse {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("visitData")
    @Expose
    private List<UpComingVisitDto> visitData = null;

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

    public List<UpComingVisitDto> getVisitData() {
        return visitData;
    }

    public void setVisitData(List<UpComingVisitDto> visitData) {
        this.visitData = visitData;
    }
}
