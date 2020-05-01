
package com.luminous.pdi.draft;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DraftDetail implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("visitdate")
    @Expose
    private String visitdate;
    @SerializedName("divisionid")
    @Expose
    private Integer divisionid;
    @SerializedName("distributorid")
    @Expose
    private Integer distributorid;
    @SerializedName("distributorname")
    @Expose
    private String distributorname;
    @SerializedName("divisionname")
    @Expose
    private String divisionname;
    @SerializedName("requestno")
    @Expose
    private String requestno;
    @SerializedName("requesttype")
    @Expose
    private Object requesttype;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Request_No")
    @Expose
    private Object requestNo;
    @SerializedName("ContactNo")
    @Expose
    private Object contactNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVisitdate() {
        return visitdate;
    }

    public void setVisitdate(String visitdate) {
        this.visitdate = visitdate;
    }

    public Integer getDivisionid() {
        return divisionid;
    }

    public void setDivisionid(Integer divisionid) {
        this.divisionid = divisionid;
    }

    public Integer getDistributorid() {
        return distributorid;
    }

    public void setDistributorid(Integer distributorid) {
        this.distributorid = distributorid;
    }

    public String getDistributorname() {
        return distributorname;
    }

    public void setDistributorname(String distributorname) {
        this.distributorname = distributorname;
    }

    public String getDivisionname() {
        return divisionname;
    }

    public void setDivisionname(String divisionname) {
        this.divisionname = divisionname;
    }

    public String getRequestno() {
        return requestno;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
    }

    public Object getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(Object requesttype) {
        this.requesttype = requesttype;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(Object requestNo) {
        this.requestNo = requestNo;
    }

    public Object getContactNo() {
        return contactNo;
    }

    public void setContactNo(Object contactNo) {
        this.contactNo = contactNo;
    }

}
