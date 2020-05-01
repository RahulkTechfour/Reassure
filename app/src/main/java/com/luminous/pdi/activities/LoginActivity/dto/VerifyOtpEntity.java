package com.luminous.pdi.activities.LoginActivity.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VerifyOtpEntity {

    @SerializedName("Message")
    @Expose
    private String Message;
    @SerializedName("Status")
    @Expose
    private String Status;

    @SerializedName("Token")
    @Expose
    private String Token;

    @SerializedName("get_user_profile")
    @Expose
    private List<UserProfileEntity> get_user_profile;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public List<UserProfileEntity> getGet_user_profile() {
        return get_user_profile;
    }

    public void setGet_user_profile(List<UserProfileEntity> get_user_profile) {
        this.get_user_profile = get_user_profile;
    }
}
