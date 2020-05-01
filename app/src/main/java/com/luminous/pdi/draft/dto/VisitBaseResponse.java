package com.luminous.pdi.draft.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitBaseResponse {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Token")
    @Expose
    private String token;

    @SerializedName("DeleteRecordStatus")
    @Expose
    private String DeleteRecordStatus;

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

    public String getDeleteRecordStatus() {
        return DeleteRecordStatus;
    }

    public void setDeleteRecordStatus(String deleteRecordStatus) {
        DeleteRecordStatus = deleteRecordStatus;
    }
}
