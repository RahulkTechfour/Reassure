
package com.luminous.pdi.calendar.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PenidngResponse {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("pendingVisitData")
    @Expose
    private List<PendingVisitDatum> pendingVisitData = null;

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

    public List<PendingVisitDatum> getPendingVisitData() {
        return pendingVisitData;
    }

    public void setPendingVisitData(List<PendingVisitDatum> pendingVisitData) {
        this.pendingVisitData = pendingVisitData;
    }

}
