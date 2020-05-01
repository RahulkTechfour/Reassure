
package com.luminous.pdi.addproductLight.LightRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MaterialList {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("MaterialName")
    @Expose
    private String materialName;


    public String getMaterialID() {
        return MaterialID;
    }

    public void setMaterialID(String materialID) {
        MaterialID = materialID;
    }

    @SerializedName("MaterialID")
    @Expose
    private String MaterialID;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

}
