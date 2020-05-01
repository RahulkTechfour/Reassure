package com.luminous.pdi.RemarkFragment.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageUpload {

    @SerializedName("responseMessage")
    @Expose
    private String message;
    @SerializedName("responseCode")
    @Expose
    private String status;
    @SerializedName("filename")
    private String filename;

    @SerializedName("defect_photos")
    @Expose
    private String defect_image;
    @SerializedName("method")
    @Expose
    private String imagemethod;

    @SerializedName("case_id")
    @Expose
    private String case_id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDefect_image() {
        return defect_image;
    }

    public void setDefect_image(String defect_image) {
        this.defect_image = defect_image;
    }

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
