
package com.luminous.pdi.activities.LoginActivity.CreateOTP;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseData {

    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("smsresponse")
    @Expose
    private String smsresponse;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getSmsresponse() {
        return smsresponse;
    }

    public void setSmsresponse(String smsresponse) {
        this.smsresponse = smsresponse;
    }

}
