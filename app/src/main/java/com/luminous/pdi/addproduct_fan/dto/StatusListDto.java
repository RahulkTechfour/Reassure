package com.luminous.pdi.addproduct_fan.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusListDto {

    @SerializedName("ID")
    @Expose
    private int ID;
    @SerializedName("Name")
    @Expose
    private String Name;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
