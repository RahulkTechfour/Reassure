
package com.luminous.pdi.addproductLight.LightRes;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("MaterialList")
    @Expose
    private List<MaterialList> materialList = null;
    @SerializedName("FaultList")
    @Expose
    private List<FaultList> faultList = null;

    public List<MaterialList> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<MaterialList> materialList) {
        this.materialList = materialList;
    }

    public List<FaultList> getFaultList() {
        return faultList;
    }

    public void setFaultList(List<FaultList> faultList) {
        this.faultList = faultList;
    }

}
