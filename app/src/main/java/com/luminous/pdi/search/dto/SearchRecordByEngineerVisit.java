
package com.luminous.pdi.search.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchRecordByEngineerVisit {

    @SerializedName("DistributorID")
    @Expose
    private String distributorID;
    @SerializedName("DivisionName")
    @Expose
    private String divisionName;
    @SerializedName("DistributorName")
    @Expose
    private String distributorName;
    @SerializedName("DistributorAddress")
    @Expose
    private String distributorAddress;
    @SerializedName("Qnty")
    @Expose
    private String qnty;
    @SerializedName("VisitDate")
    @Expose
    private String visitDate;
    @SerializedName("SerialNo")
    @Expose
    private String serialNo;
    @SerializedName("InspectionDate")
    @Expose
    private String inspectionDate;
    @SerializedName("MaterialName")
    @Expose
    private String materialName;
    @SerializedName("FaultID")
    @Expose
    private String faultID;

    public String getFaultQnty() {
        return FaultQnty;
    }

    public void setFaultQnty(String faultQnty) {
        FaultQnty = faultQnty;
    }

    @SerializedName("FaultQnty")
    @Expose
    private String FaultQnty;

    public String getFaultName() {
        return FaultName;
    }

    public void setFaultName(String faultName) {
        FaultName = faultName;
    }

    @SerializedName("FaultName")
    @Expose
    private String FaultName;

    @SerializedName("InvoiceID")
    @Expose
    private String invoiceID;
    @SerializedName("ProductStatus")
    @Expose
    private String productStatus;
    @SerializedName("WarrantyStatus")
    @Expose
    private String warrantyStatus;
    @SerializedName("DateofPurchase")
    @Expose
    private String dateofPurchase;
    @SerializedName("ClaimStatus")
    @Expose
    private String claimStatus;

    @SerializedName("MFG_Date")
    @Expose
    private String MFG_Date;
    @SerializedName("BatchNo")
    @Expose
    private String BatchNo;

    @SerializedName("FaultAccept")
    @Expose
    private String FaultAccept;

    public String getFaultAccept() {
        return FaultAccept;
    }

    public void setFaultAccept(String faultAccept) {
        FaultAccept = faultAccept;
    }

    public String getFaultReject() {
        return FaultReject;
    }

    public void setFaultReject(String faultReject) {
        FaultReject = faultReject;
    }

    @SerializedName("FaultReject")
    @Expose
    private String FaultReject;

    public String getMFG_Date() {
        return MFG_Date;
    }

    public void setMFG_Date(String MFG_Date) {
        this.MFG_Date = MFG_Date;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }





    public String getDistributorID() {
        return distributorID;
    }

    public void setDistributorID(String distributorID) {
        this.distributorID = distributorID;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDistributorAddress() {
        return distributorAddress;
    }

    public void setDistributorAddress(String distributorAddress) {
        this.distributorAddress = distributorAddress;
    }

    public String getQnty() {
        return qnty;
    }

    public void setQnty(String qnty) {
        this.qnty = qnty;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getFaultID() {
        return faultID;
    }

    public void setFaultID(String faultID) {
        this.faultID = faultID;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getWarrantyStatus() {
        return warrantyStatus;
    }

    public void setWarrantyStatus(String warrantyStatus) {
        this.warrantyStatus = warrantyStatus;
    }

    public String getDateofPurchase() {
        return dateofPurchase;
    }

    public void setDateofPurchase(String dateofPurchase) {
        this.dateofPurchase = dateofPurchase;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

}
