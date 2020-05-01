
package com.luminous.pdi.home.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PendingvisitDetail implements Serializable {

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
    @SerializedName("status")
    @Expose
    private String status;


    @SerializedName("requestno")
    @Expose
    private String requestno;
    @SerializedName("requesttype")
    @Expose
    private String requesttype;
    @SerializedName("quantity")
    @Expose
    private String quantity;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("ContactNo")
    @Expose
    private  String ContactNo;

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRequestno() {
        return requestno;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
    }

    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PendingvisitDetail{" +
                "id=" + id +
                ", visitdate='" + visitdate + '\'' +
                ", divisionid=" + divisionid +
                ", distributorid=" + distributorid +
                ", distributorname='" + distributorname + '\'' +
                ", divisionname='" + divisionname + '\'' +
                ", status='" + status + '\'' +
                ", requestno='" + requestno + '\'' +
                ", requesttype='" + requesttype + '\'' +
                ", quantity='" + quantity + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
