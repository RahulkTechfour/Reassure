
package com.luminous.pdi.calendar.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingCountDetails {

    @SerializedName("RequestDate")
    @Expose
    private String requestDate;
    @SerializedName("Count")
    @Expose
    private String count;

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

}
