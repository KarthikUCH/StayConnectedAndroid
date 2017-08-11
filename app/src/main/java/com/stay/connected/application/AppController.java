package com.stay.connected.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.stay.connected.network.ResponseListener;
import com.stay.connected.network.RestServiceFactory;
import com.stay.connected.network.StayConnectedService;
import com.stay.connected.network.model.RegistrationRequest;
import com.stay.connected.network.model.SignInRequest;
import com.stay.connected.network.model.VerifyOtpRequest;
import com.stay.connected.util.ImageUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    private StayConnectedService mClientService;

    public AppController(Context mContext, RestServiceFactory mRestServiceFactory, AppPreference appPreference) {
        this.mContext = mContext;
        this.mRestServiceFactory = mRestServiceFactory;
        this.mAppPreference = appPreference;
        updateRestServiceFactory();
    }


    public void updateRestServiceFactory() {
        String url = mAppPreference.getAppUrl();
        if (!TextUtils.isEmpty(url)) {
            mClientService = mRestServiceFactory.create(StayConnectedService.class, url);
        }
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
    public void registerUser(String name, String email, String mobile, String password, ResponseListener<Integer> listener) {

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
                            userLogInState = false;
                            Log.d(TAG, "User registration success");
                            return response.code();
                        } else {
                            Log.i(TAG, "User registration failed:  Server Error Code " + response.code());
                            return response.code();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "User registration error " + e.getMessage());
                        return -1;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        resultCode -> {
                            Log.i(TAG, "onNext");
                            listener.onResponse(resultCode);
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
    public void verifyOTP(String email, String otp, ResponseListener<Integer> listener) {

        VerifyOtpRequest requestBody = new VerifyOtpRequest(email, otp);
        Observable.defer(() -> Observable.just(requestBody))
                .map(verifyOtpRequest -> {
                    try {
                        Call<ResponseBody> requestCall = mClientService.verifyOtp(verifyOtpRequest);
                        Response<ResponseBody> response = requestCall.execute();
                        if (response.code() == 200) {
                            mAppPreference.setUserVerificationState(true);
                            Log.d(TAG, "Otp verification success");
                            return response.code();
                        } else {
                            mAppPreference.setUserVerificationState(false);
                            Log.i(TAG, "Otp verification failed:  Server Error Code " + response.code());
                            return response.code();

                        }
                    } catch (Exception e) {
                        mAppPreference.setUserVerificationState(false);
                        Log.e(TAG, "Otp verification error " + e.getMessage());
                        return -1;
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultCode -> {
                            Log.i(TAG, "onNext");
                            listener.onResponse(resultCode);
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
    public void resendOtp(String email, ResponseListener<Integer> listener) {
        Observable.defer(() -> Observable.just(email))
                .map(emailId -> {
                    try {
                        Call<ResponseBody> requestBody = mClientService.resendOtp(emailId);
                        Response<ResponseBody> response = requestBody.execute();
                        if (response.code() == 200) {
                            Log.d(TAG, "Otp resend success");
                            return response.code();
                        } else {
                            Log.i(TAG, "Otp resend failed:  Server Error Code " + response.code());
                            return response.code();
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "Otp resend error " + e.getMessage());
                        return -1;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultCode -> {
                            Log.i(TAG, "onNext");
                            listener.onResponse(resultCode);
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
    public void signInUser(String email, String password, ResponseListener<Integer> listener) {

        SignInRequest requestBody = new SignInRequest(email, password);
        Observable.defer(() -> Observable.just(requestBody))
                .map(signInRequest -> {
                    try {
                        Call<ResponseBody> requestCall = mClientService.doSignIn(signInRequest);
                        Response<ResponseBody> response = requestCall.execute();
                        if (response.code() == 200) {
                            //TODO : Check for previous logged in user first
                            mAppPreference.setUserEmail(email);
                            userLogInState = true;
                            Log.d(TAG, "User login success");
                            return response.code();
                        } else {
                            userLogInState = false;
                            Log.i(TAG, "User login failed:  Server Error Code " + response.code());
                            return response.code();

                        }
                    } catch (Exception e) {
                        userLogInState = false;
                        Log.e(TAG, "User login error " + e.getMessage());
                        return -1;
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultCode -> {
                            Log.i(TAG, "onNext");
                            listener.onResponse(resultCode);
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
     * To upload user avatar
     *
     * @param avatarBmp
     * @param email
     * @param listener
     */
    public void uploadAvatar(Bitmap avatarBmp, String email, ResponseListener<Boolean> listener) {

        Observable.defer(() -> Observable.just(storeAvatar(avatarBmp, email)))
                .map(filePath -> {
                    try {
                        File file = new File(ImageUtil.storeAvatar(mContext, avatarBmp, "karthikeyan027@gmail.com"));

                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

                        RequestBody fullName = RequestBody.create(MediaType.parse("multipart/form-data"), email);

                        Call<ResponseBody> requestCall = mClientService.updateAvatar(body, fullName);
                        Response<ResponseBody> response = requestCall.execute();
                        if (response.code() == 200) {
                            Log.d(TAG, "Upload avatar success");
                            return true;
                        } else {
                            Log.i(TAG, "Upload avatar failed:  Server Error Code " + response.code());
                            return false;

                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Upload avatar error " + e.getMessage());
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

    private String storeAvatar(Bitmap avatarBmp, String email) {
        return ImageUtil.storeAvatar(mContext, avatarBmp, email);
    }
}
