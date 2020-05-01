
package com.luminous.pdi.home.dto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("visitcount")
    @Expose
    private List<Visitcount> visitcount = null;
    @SerializedName("pendingvisit_details")
    @Expose
    private List<PendingvisitDetail> pendingvisitDetails = null;

    public List<Visitcount> getVisitcount() {
        return visitcount;
    }

    public void setVisitcount(List<Visitcount> visitcount) {
        this.visitcount = visitcount;
    }

    public List<PendingvisitDetail> getPendingvisitDetails() {
        return pendingvisitDetails;
    }

    public void setPendingvisitDetails(List<PendingvisitDetail> pendingvisitDetails) {
        this.pendingvisitDetails = pendingvisitDetails;
    }

}
