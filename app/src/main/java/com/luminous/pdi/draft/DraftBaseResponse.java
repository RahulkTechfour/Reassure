
package com.luminous.pdi.draft;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    private List<GetVisitDatum> getVisitData = null;

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

    public List<GetVisitDatum> getGetVisitData() {
        return getVisitData;
    }

    public void setGetVisitData(List<GetVisitDatum> getVisitData) {
        this.getVisitData = getVisitData;
    }

}
