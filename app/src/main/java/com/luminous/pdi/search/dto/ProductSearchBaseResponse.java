
package com.luminous.pdi.search.dto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductSearchBaseResponse {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("SearchRecordWithStatus")
    @Expose
    private List<SearchRecordWithstatus> searchRecordWithStatus = null;

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

    public List<SearchRecordWithstatus> getSearchRecordWithStatus() {
        return searchRecordWithStatus;
    }

    public void setSearchRecordWithStatus(List<SearchRecordWithstatus> searchRecordWithStatus) {
        this.searchRecordWithStatus = searchRecordWithStatus;
    }

}
