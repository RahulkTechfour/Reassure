package com.luminous.pdi.activities.LoginActivity.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileEntity {

    @SerializedName("Employeeid")
    @Expose
    private String Employeeid;
    @SerializedName("Username")
    @Expose
    private String Username;

    @SerializedName("PhoneNumber")
    @Expose
    private String PhoneNumber;

    @SerializedName("State")
    @Expose
    private String State;

    @SerializedName("Pincode")
    @Expose
    private String Pincode;

    @SerializedName("Address")
    @Expose
    private String Address;

    @SerializedName("City")
    @Expose
    private String City;


    public String getEmployeeid() {
        return Employeeid;
    }

    public void setEmployeeid(String employeeid) {
        Employeeid = employeeid;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
