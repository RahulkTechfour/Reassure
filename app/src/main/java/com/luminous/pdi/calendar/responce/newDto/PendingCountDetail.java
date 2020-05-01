
package com.luminous.pdi.calendar.responce.newDto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingCountDetail {

    @SerializedName("RequestDate")
    @Expose
    private String requestDate;
    @SerializedName("PendingCount")
    @Expose
    private String pendingCount;
    @SerializedName("Pending_DistyDetails")
    @Expose
    private List<PendingDistyDetail> pendingDistyDetails = null;

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(String pendingCount) {
        this.pendingCount = pendingCount;
    }

    public List<PendingDistyDetail> getPendingDistyDetails() {
        return pendingDistyDetails;
    }

    public void setPendingDistyDetails(List<PendingDistyDetail> pendingDistyDetails) {
        this.pendingDistyDetails = pendingDistyDetails;
    }

}
