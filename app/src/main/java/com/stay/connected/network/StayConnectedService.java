package com.stay.connected.network;

import com.stay.connected.network.model.RegistrationRequest;
import com.stay.connected.network.model.SignInRequest;
import com.stay.connected.network.model.VerifyOtpRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by karthikeyan on 8/8/17.
 */

public interface StayConnectedService {

    @POST("/registration/")
    Call<RegistrationRequest> doRegistration(@Body RegistrationRequest registrationRequest);

    @POST("/otp/verify/")
    Call<ResponseBody> verifyOtp(@Body VerifyOtpRequest verifyOtpRequest);

    @GET("/otp/resend/")
    Call<ResponseBody> resendOtp(@Query("email") String email);

    @POST("/login/")
    Call<ResponseBody> doSignIn(@Body SignInRequest signInRequest);


}
