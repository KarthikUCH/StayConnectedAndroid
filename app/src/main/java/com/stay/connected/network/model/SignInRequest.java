package com.stay.connected.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by karthikeyan on 8/8/17.
 */

public class SignInRequest {

    @SerializedName("email")
    private String userEmail;

    @SerializedName("password")
    private String password;

    public SignInRequest(String userEmail, String password) {
        this.userEmail = userEmail;
        this.password = password;
    }
}
