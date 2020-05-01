
package com.luminous.pdi.calendar.responce.newDto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompleteCountDetail {

    @SerializedName("RequestDate")
    @Expose
    private String requestDate;
    @SerializedName("CompleteCount")
    @Expose
    private String completeCount;
    @SerializedName("Complete_DistyDetails")
    @Expose
    private List<CompleteDistyDetail> completeDistyDetails = null;

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(String completeCount) {
        this.completeCount = completeCount;
    }

    public List<CompleteDistyDetail> getCompleteDistyDetails() {
        return completeDistyDetails;
    }

    public void setCompleteDistyDetails(List<CompleteDistyDetail> completeDistyDetails) {
        this.completeDistyDetails = completeDistyDetails;
    }

}
