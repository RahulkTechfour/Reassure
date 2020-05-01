
package com.luminous.pdi.addproduct_fan.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {

    @SerializedName("MaterialList")
    @Expose
    private List<Object> materialList = null;
    @SerializedName("FaultList")
    @Expose
    private List<FaultList> faultList = null;

    public List<Object> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Object> materialList) {
        this.materialList = materialList;
    }

    public List<FaultList> getFaultList() {
        return faultList;
    }

    public void setFaultList(List<FaultList> faultList) {
        this.faultList = faultList;
    }
}
