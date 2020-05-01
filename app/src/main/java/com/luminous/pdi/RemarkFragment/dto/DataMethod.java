package com.luminous.pdi.RemarkFragment.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataMethod {

    @SerializedName("method")
    @Expose
    private String imagemethod;

    @SerializedName("case_id")
    @Expose
    private String case_id;

    public String getImagemethod() {
        return imagemethod;
    }

    public void setImagemethod(String imagemethod) {
        this.imagemethod = imagemethod;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }
}
