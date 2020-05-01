
package com.luminous.pdi.completed.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.luminous.pdi.home.dto.Visitcount;

import java.io.Serializable;
import java.util.List;

public class GetVisitDatum implements Serializable {
    @SerializedName("visitcount")
    @Expose
    private List<Visitcount> visitcount = null;
    @SerializedName("pendingvisit_details")
    @Expose
    private Object pendingvisitDetails;
    @SerializedName("completed_details")
    @Expose
    private List<CompletedDetail> completedDetails = null;
    @SerializedName("draft_details")
    @Expose
    private Object draftDetails;

    public List<Visitcount> getVisitcount() {
        return visitcount;
    }

    public void setVisitcount(List<Visitcount> visitcount) {
        this.visitcount = visitcount;
    }

    public Object getPendingvisitDetails() {
        return pendingvisitDetails;
    }

    public void setPendingvisitDetails(Object pendingvisitDetails) {
        this.pendingvisitDetails = pendingvisitDetails;
    }

    public List<CompletedDetail> getCompletedDetails() {
        return completedDetails;
    }

    public void setCompletedDetails(List<CompletedDetail> completedDetails) {
        this.completedDetails = completedDetails;
    }

    public Object getDraftDetails() {
        return draftDetails;
    }

    public void setDraftDetails(Object draftDetails) {
        this.draftDetails = draftDetails;
    }

}



