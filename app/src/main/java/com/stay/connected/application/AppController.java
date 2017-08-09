package com.stay.connected.application;

import android.content.Context;
import android.util.Log;

import com.stay.connected.network.ResponseListener;
import com.stay.connected.network.RestServiceFactory;
import com.stay.connected.network.StayConnectedService;
import com.stay.connected.network.model.RegistrationRequest;
import com.stay.connected.network.model.SignInRequest;
import com.stay.connected.network.model.VerifyOtpRequest;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by karthikeyan on 8/8/17.
 */

public class AppController {
    private static final String TAG = AppController.class.getName();

    private Context mContext;
    private RestServiceFactory mRestServiceFactory;
    private AppPreference mAppPreference;
    private boolean userLogInState = false;

    private final StayConnectedService mClientService;

    public AppController(Context mContext, RestServiceFactory mRestServiceFactory, AppPreference appPreference) {
        this.mContext = mContext;
        this.mRestServiceFactory = mRestServiceFactory;
        this.mAppPreference = appPreference;
        mClientService = mRestServiceFactory.create(StayConnectedService.class);
    }


    public boolean getUserLogInState() {
        return userLogInState;
    }

    /**
     * To perform user registration
     *
     * @param name
     * @param email
     * @param mobile
     * @param password
     * @param listener
     */
    public void registerUser(String name, String email, String mobile, String password, ResponseListener<Boolean> listener) {

        RegistrationRequest requestBody = new RegistrationRequest(name, email, mobile, password);
        Observable.defer(() -> Observable.just(requestBody))
                .map(registrationRequest -> {
                    try {
                        Call<RegistrationRequest> requestCall = mClientService.doRegistration(registrationRequest);
                        Response<RegistrationRequest> response = requestCall.execute();
                        if (response.code() == 200) {
                            mAppPreference.setUserEmail(email);
                            mAppPreference.setUserName(name);

                            // reset the user verification state for every registration
                            mAppPreference.setUserVerificationState(false);
                            Log.d(TAG, "User registration success");
                            return true;
                        } else {
                            Log.i(TAG, "User registration failed:  Server Error Code " + response.code());
                            return false;
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "User registration error " + e.getMessage());
                        return false;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            Log.i(TAG, "onNext");
                            listener.onResponse(result);
                        },
                        throwable -> {
                            Log.e(TAG, "onError", throwable);
                            listener.onError(throwable != null ? throwable.getMessage() : "");
                        },
                        () -> {
                            Log.i(TAG, "onCompleted");
                            listener.onCompleted();
                        }
                );
    }

    /**
     * To perform otp verification
     *
     * @param email
     * @param otp
     * @param listener
     */
    public void verifyOTP(String email, String otp, ResponseListener<Boolean> listener) {

        VerifyOtpRequest requestBody = new VerifyOtpRequest(email, otp);
        Observable.defer(() -> Observable.just(requestBody))
                .map(verifyOtpRequest -> {
                    try {
                        Call<ResponseBody> requestCall = mClientService.verifyOtp(verifyOtpRequest);
                        Response<ResponseBody> response = requestCall.execute();
                        if (response.code() == 200) {
                            mAppPreference.setUserVerificationState(true);
                            Log.d(TAG, "Otp verification success");
                            return true;
                        } else {
                            mAppPreference.setUserVerificationState(false);
                            Log.i(TAG, "Otp verification failed:  Server Error Code " + response.code());
                            return false;

                        }
                    } catch (IOException e) {
                        mAppPreference.setUserVerificationState(false);
                        Log.e(TAG, "Otp verification error " + e.getMessage());
                        return false;
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            Log.i(TAG, "onNext");
                            listener.onResponse(result);
                        },
                        throwable -> {
                            Log.e(TAG, "onError", throwable);
                            listener.onError(throwable != null ? throwable.getMessage() : "");
                        },
                        () -> {
                            Log.i(TAG, "onCompleted");
                            listener.onCompleted();
                        });

    }


    /**
     * Request server to resend OTP
     *
     * @param email
     * @param listener
     */
    public void resendOtp(String email, ResponseListener<Boolean> listener) {
        Observable.defer(() -> Observable.just(email))
                .map(emailId -> {
                    try {
                        Call<ResponseBody> requestBody = mClientService.resendOtp(emailId);
                        Response<ResponseBody> response = requestBody.execute();
                        if (response.code() == 200) {
                            Log.d(TAG, "Otp resend success");
                            return true;
                        } else {
                            Log.i(TAG, "Otp resend failed:  Server Error Code " + response.code());
                            return false;
                        }

                    } catch (IOException e) {
                        return false;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            Log.i(TAG, "onNext");
                            listener.onResponse(result);
                        },
                        throwable -> {
                            Log.e(TAG, "onError", throwable);
                            listener.onError(throwable != null ? throwable.getMessage() : "");
                        },
                        () -> {
                            Log.i(TAG, "onCompleted");
                            listener.onCompleted();
                        });

    }


    /**
     * To perform otp verification
     *
     * @param email
     * @param password
     * @param listener
     */
    public void signInUser(String email, String password, ResponseListener<Boolean> listener) {

        SignInRequest requestBody = new SignInRequest(email, password);
        Observable.defer(() -> Observable.just(requestBody))
                .map(signInRequest -> {
                    try {
                        Call<ResponseBody> requestCall = mClientService.doSignIn(signInRequest);
                        Response<ResponseBody> response = requestCall.execute();
                        if (response.code() == 200) {
                            userLogInState = true;
                            Log.d(TAG, "User login success");
                            return true;
                        } else {
                            userLogInState = true;
                            Log.i(TAG, "User login failed:  Server Error Code " + response.code());
                            return false;

                        }
                    } catch (IOException e) {
                        userLogInState = true;
                        Log.e(TAG, "User login error " + e.getMessage());
                        return false;
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            Log.i(TAG, "onNext");
                            listener.onResponse(result);
                        },
                        throwable -> {
                            Log.e(TAG, "onError", throwable);
                            listener.onError(throwable != null ? throwable.getMessage() : "");
                        },
                        () -> {
                            Log.i(TAG, "onCompleted");
                            listener.onCompleted();
                        });

    }
}
