package com.luminous.pdi.home.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpComingVisitDto {

    @SerializedName("Date")
    @Expose
    private String Date;
    @SerializedName("CustomerName")
    @Expose
    private String CustomerName;
    @SerializedName("Division")
    @Expose
    private String Division;
    @SerializedName("Status")
    @Expose
    private String Status;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
