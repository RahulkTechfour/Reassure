
package com.luminous.pdi.draft;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetVisitDatum {

    @SerializedName("visitcount")
    @Expose
    private List<Visitcount> visitcount = null;
    @SerializedName("pendingvisit_details")
    @Expose
    private Object pendingvisitDetails;
    @SerializedName("completed_details")
    @Expose
    private Object completedDetails;
    @SerializedName("draft_details")
    @Expose
    private List<DraftDetail> draftDetails = null;

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

    public Object getCompletedDetails() {
        return completedDetails;
    }

    public void setCompletedDetails(Object completedDetails) {
        this.completedDetails = completedDetails;
    }

    public List<DraftDetail> getDraftDetails() {
        return draftDetails;
    }

    public void setDraftDetails(List<DraftDetail> draftDetails) {
        this.draftDetails = draftDetails;
    }

}
