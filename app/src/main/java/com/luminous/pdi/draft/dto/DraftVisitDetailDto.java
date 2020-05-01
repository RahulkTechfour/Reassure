package com.luminous.pdi.draft.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DraftVisitDetailDto {

    @SerializedName("pendingvisit_details")
    @Expose
    private String pendingvisit_details;
    @SerializedName("completed_details")
    @Expose
    private String completed_details;
    @SerializedName("visitcount")
    @Expose
    private List<DraftVisitCountDto>  visitcount;

    @SerializedName("draft_details")
    @Expose
    private List<DraftVisitDto> draft_details = null;

    public String getPendingvisit_details() {
        return pendingvisit_details;
    }

    public void setPendingvisit_details(String pendingvisit_details) {
        this.pendingvisit_details = pendingvisit_details;
    }

    public String getCompleted_details() {
        return completed_details;
    }

    public void setCompleted_details(String completed_details) {
        this.completed_details = completed_details;
    }

    public List<DraftVisitCountDto> getVisitcount() {
        return visitcount;
    }

    public void setVisitcount(List<DraftVisitCountDto> visitcount) {
        this.visitcount = visitcount;
    }

    public List<DraftVisitDto> getDraft_details() {
        return draft_details;
    }

    public void setDraft_details(List<DraftVisitDto> draft_details) {
        this.draft_details = draft_details;
    }
}
