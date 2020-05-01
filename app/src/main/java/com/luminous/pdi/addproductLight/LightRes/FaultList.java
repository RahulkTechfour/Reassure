
package com.luminous.pdi.addproductLight.LightRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaultList {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("FaultName")
    @Expose
    private String faultName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFaultName() {
        return faultName;
    }

    public void setFaultName(String faultName) {
        this.faultName = faultName;
    }

}
