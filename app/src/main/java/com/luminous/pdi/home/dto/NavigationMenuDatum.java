
package com.luminous.pdi.home.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NavigationMenuDatum {

    @SerializedName("ModuleID")
    @Expose
    private String moduleID;
    @SerializedName("ModuleName")
    @Expose
    private String moduleName;
    @SerializedName("ModuleImage")
    @Expose
    private String moduleImage;

    public String getModuleID() {
        return moduleID;
    }

    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

    public String getModuleName() {
        return moduleName;
    }

   /* public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }*/

    public String setModuleName(String moduleName) {
        return moduleName;
    }


    public String getModuleImage() {
        return moduleImage;
    }

    public void setModuleImage(String moduleImage) {
        this.moduleImage = moduleImage;
    }

}
