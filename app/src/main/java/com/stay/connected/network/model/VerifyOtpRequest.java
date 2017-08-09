package com.stay.connected.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by karthikeyan on 8/8/17.
 */

public class VerifyOtpRequest {

    @SerializedName("email")
    private String userEmail;

    @SerializedName("otp")
    private String otp;

    public VerifyOtpRequest(String userEmail, String otp) {
        this.userEmail = userEmail;
        this.otp = otp;
    }
}
