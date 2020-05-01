
package com.luminous.pdi.calendar.responce.newDto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingVisitDatum {

    @SerializedName("PendingCountDetails")
    @Expose
    private List<PendingCountDetail> pendingCountDetails = null;
    @SerializedName("CompleteCountDetails")
    @Expose
    private List<CompleteCountDetail> completeCountDetails = null;

    public List<PendingCountDetail> getPendingCountDetails() {
        return pendingCountDetails;
    }

    public void setPendingCountDetails(List<PendingCountDetail> pendingCountDetails) {
        this.pendingCountDetails = pendingCountDetails;
    }

    public List<CompleteCountDetail> getCompleteCountDetails() {
        return completeCountDetails;
    }

    public void setCompleteCountDetails(List<CompleteCountDetail> completeCountDetails) {
        this.completeCountDetails = completeCountDetails;
    }

}
