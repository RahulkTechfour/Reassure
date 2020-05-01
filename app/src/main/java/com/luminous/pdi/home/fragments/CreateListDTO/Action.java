
package com.luminous.pdi.home.fragments.CreateListDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Action {

    @SerializedName("edit")
    @Expose
    private Boolean edit;
    @SerializedName("view")
    @Expose
    private Boolean view;

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

}
