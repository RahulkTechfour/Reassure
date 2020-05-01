
package com.luminous.pdi.home.dto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("navigationMenuData")
    @Expose
    private List<NavigationMenuDatum> navigationMenuData = null;
    @SerializedName("bottomMenu")
    @Expose
    private List<BottomMenu> bottomMenu = null;

    public List<NavigationMenuDatum> getNavigationMenuData() {
        return navigationMenuData;
    }

    public void setNavigationMenuData(List<NavigationMenuDatum> navigationMenuData) {
        this.navigationMenuData = navigationMenuData;
    }

    public List<BottomMenu> getBottomMenu() {
        return bottomMenu;
    }

    public void setBottomMenu(List<BottomMenu> bottomMenu) {
        this.bottomMenu = bottomMenu;
    }

}
