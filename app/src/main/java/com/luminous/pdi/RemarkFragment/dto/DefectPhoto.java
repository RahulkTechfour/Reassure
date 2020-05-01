package com.luminous.pdi.RemarkFragment.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DefectPhoto {

    @SerializedName("defect_photos")
    @Expose
    private String defect_image;

    @SerializedName("data")
    @Expose
    private List<DataMethod> datamethod;

    public String getDefect_image() {
        return defect_image;
    }

    public void setDefect_image(String defect_image) {
        this.defect_image = defect_image;
    }

    public List<DataMethod> getDatamethod() {
        return datamethod;
    }

    public void setDatamethod(List<DataMethod> datamethod) {
        this.datamethod = datamethod;
    }
}
