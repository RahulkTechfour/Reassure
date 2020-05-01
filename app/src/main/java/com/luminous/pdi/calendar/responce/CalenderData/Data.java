
package com.luminous.pdi.calendar.responce.CalenderData;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("visitcount")
    @Expose
    private String visitcount;

    public void setVisitcount(String visitcount) {
        this.visitcount = visitcount;
    }

    public List<PendingvisitDetail> getPendingvisitDetails() {
        return pendingvisitDetails;
    }

    @SerializedName("pendingvisit_details")
    @Expose
    private List<PendingvisitDetail> pendingvisitDetails = null;
    @SerializedName("completed_details")
    @Expose
    private List<Object> completedDetails = null;
    @SerializedName("draft_details")
    @Expose
    private Object draftDetails;

    public Object getVisitcount() {
        return visitcount;
    }



    public void setPendingvisitDetails(List<PendingvisitDetail> pendingvisitDetails) {
        this.pendingvisitDetails = pendingvisitDetails;
    }

    public List<Object> getCompletedDetails() {
        return completedDetails;
    }

    public void setCompletedDetails(List<Object> completedDetails) {
        this.completedDetails = completedDetails;
    }

    public Object getDraftDetails() {
        return draftDetails;
    }

    public void setDraftDetails(Object draftDetails) {
        this.draftDetails = draftDetails;
    }

}
