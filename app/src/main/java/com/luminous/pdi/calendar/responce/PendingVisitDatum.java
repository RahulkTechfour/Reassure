
package com.luminous.pdi.calendar.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingVisitDatum {

    @SerializedName("PendingCountDetails")
    @Expose
    private PendingCountDetails pendingCountDetails;
    @SerializedName("DistyDetails")
    @Expose
    private List<DistyDetail> distyDetails = null;

    public PendingCountDetails getPendingCountDetails() {
        return pendingCountDetails;
    }

    public void setPendingCountDetails(PendingCountDetails pendingCountDetails) {
        this.pendingCountDetails = pendingCountDetails;
    }

    public List<DistyDetail> getDistyDetails() {
        return distyDetails;
    }

    public void setDistyDetails(List<DistyDetail> distyDetails) {
        this.distyDetails = distyDetails;
    }

}
