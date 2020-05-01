
package com.luminous.pdi.calendar.responce.newDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompleteDistyDetail {

    @SerializedName("DistributorID")
    @Expose
    private String distributorID;
    @SerializedName("DistributerName")
    @Expose
    private String distributerName;
    @SerializedName("TotalQnty")
    @Expose
    private String totalQnty;
    @SerializedName("DivisionName")
    @Expose
    private String divisionName;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("RequestNo")
    @Expose
    private String requestNo;
    @SerializedName("CustomerAddress")
    @Expose
    private String customerAddress;
    @SerializedName("RequestType")
    @Expose
    private String requestType;
    @SerializedName("RequestDate")
    @Expose
    private String requestDate;

    public String getDistributorID() {
        return distributorID;
    }

    public void setDistributorID(String distributorID) {
        this.distributorID = distributorID;
    }

    public String getDistributerName() {
        return distributerName;
    }

    public void setDistributerName(String distributerName) {
        this.distributerName = distributerName;
    }

    public String getTotalQnty() {
        return totalQnty;
    }

    public void setTotalQnty(String totalQnty) {
        this.totalQnty = totalQnty;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

}
