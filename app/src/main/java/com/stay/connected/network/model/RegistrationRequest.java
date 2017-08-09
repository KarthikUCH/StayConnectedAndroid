package com.stay.connected.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by karthikeyan on 8/8/17.
 */

public class RegistrationRequest {

    @SerializedName("name")
    private String userName;

    @SerializedName("email")
    private String userEmail;

    @SerializedName("mobile")
    private String userMobile;

    @SerializedName("password")
    private String userPassword;

    public RegistrationRequest(String userName, String userEmail, String userMobile, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userMobile = userMobile;
        this.userPassword = userPassword;
    }
}
