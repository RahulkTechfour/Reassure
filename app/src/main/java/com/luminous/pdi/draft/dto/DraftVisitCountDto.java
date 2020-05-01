package com.luminous.pdi.draft.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DraftVisitCountDto {


    @SerializedName("pending")
    @Expose
    private String pending;
    @SerializedName("completed")
    @Expose
    private String completed;

    @SerializedName("draft")
    @Expose
    private String draft;

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }
}
