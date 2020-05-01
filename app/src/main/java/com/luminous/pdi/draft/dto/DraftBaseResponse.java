package com.luminous.pdi.draft.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.luminous.pdi.completed.dto.GetVisitDatum;

import java.util.List;

public class DraftBaseResponse {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("get_visit_data")
    @Expose
    private List<DraftVisitDetailDto> getVisitData = null;


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

    public List<DraftVisitDetailDto> getGetVisitData() {
        return getVisitData;
    }

    public void setGetVisitData(List<DraftVisitDetailDto> getVisitData) {
        this.getVisitData = getVisitData;
    }
}
