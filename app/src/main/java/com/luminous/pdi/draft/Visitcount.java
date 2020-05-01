
package com.luminous.pdi.draft;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Visitcount {

    @SerializedName("pending")
    @Expose
    private Integer pending;
    @SerializedName("completed")
    @Expose
    private Integer completed;
    @SerializedName("draft")
    @Expose
    private Integer draft;

    public Integer getPending() {
        return pending;
    }

    public void setPending(Integer pending) {
        this.pending = pending;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Integer getDraft() {
        return draft;
    }

    public void setDraft(Integer draft) {
        this.draft = draft;
    }

}
