package com.luminous.pdi.addproductLight.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {




    public String getFaultList() {
        return faultList;
    }

    public void setFaultList(String faultList) {
        this.faultList = faultList;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelcted_quntity() {
        return selcted_quntity;
    }

    public void setSelcted_quntity(String selcted_quntity) {
        this.selcted_quntity = selcted_quntity;
    }

    public String getSelcted_batchNo() {
        return selcted_batchNo;
    }

    public void setSelcted_batchNo(String selcted_batchNo) {
        this.selcted_batchNo = selcted_batchNo;
    }

    public String getWarranty_status() {
        return warranty_status;
    }

    public void setWarranty_status(String warranty_status) {
        this.warranty_status = warranty_status;
    }

    public String getWarranty_accepted_quan() {
        return warranty_accepted_quan;
    }

    public void setWarranty_accepted_quan(String warranty_accepted_quan) {
        this.warranty_accepted_quan = warranty_accepted_quan;
    }

    public String getWarranty_rejected_quan() {
        return warranty_rejected_quan;
    }

    public void setWarranty_rejected_quan(String warranty_rejected_quan) {
        this.warranty_rejected_quan = warranty_rejected_quan;
    }

    private String faultList;
    private String selectedDate;
    private String selcted_quntity;
    private String selcted_batchNo;
    private String warranty_status;
    private String warranty_accepted_quan;
    private String warranty_rejected_quan;



    public Product(String faultList_, String selectedDate_, String selcted_quntity_, String selcted_batchNo_,
                   String warranty_status_, String warranty_accepted_quan,
                   String warranty_rejected_quan) {

        this.faultList = faultList_;
        this.selectedDate = selectedDate_;
        this.selcted_quntity = selcted_quntity_;

        this.selcted_batchNo = selcted_batchNo_;
        this.warranty_status = warranty_status_;
        this.warranty_accepted_quan = warranty_accepted_quan;
        this.warranty_rejected_quan = warranty_rejected_quan;



    }
}
