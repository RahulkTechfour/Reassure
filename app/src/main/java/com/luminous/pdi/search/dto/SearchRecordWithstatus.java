
package com.luminous.pdi.search.dto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchRecordWithstatus {

    @SerializedName("SearchRecord_ByEngineerVisit")
    @Expose
    private List<SearchRecordByEngineerVisit> searchRecordByEngineerVisit = null;

    public List<SearchRecordByEngineerVisit> getSearchRecordByEngineerVisit() {
        return searchRecordByEngineerVisit;
    }

    public void setSearchRecordByEngineerVisit(List<SearchRecordByEngineerVisit> searchRecordByEngineerVisit) {
        this.searchRecordByEngineerVisit = searchRecordByEngineerVisit;
    }

}
