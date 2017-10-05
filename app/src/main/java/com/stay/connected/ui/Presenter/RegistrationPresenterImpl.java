package com.stay.connected.ui.Presenter;

import android.text.TextUtils;

import com.stay.connected.application.AppController;
import com.stay.connected.network.ResponseListener;
import com.stay.connected.ui.view.IRegistrationView;
import com.stay.connected.util.AppUtil;

/**
 * Created by karthikeyan on 5/10/17.
 */

public class RegistrationPresenterImpl implements IRegistrationPresenter {

    private AppController mAppController;
    private IRegistrationView mRegistrationView;

    public RegistrationPresenterImpl(AppController appController) {
        this.mAppController = appController;
    }

    @Override
    public void attachView(IRegistrationView registrationView) {
        this.mRegistrationView = registrationView;
    }

    @Override
    public void registerUser(String name, String email, boolean isValidMobile, String mobile, String password) {
        boolean validationSuccess = true;
        if (mRegistrationView == null) {
            return;
        }

        if (TextUtils.isEmpty(name)) {
            mRegistrationView.setInvalidName(validationSuccess);
            validationSuccess = false;
        }
        if (!AppUtil.verifyUserEmail(email)) {
            mRegistrationView.setInvalidEmail(validationSuccess);
            validationSuccess = false;
        }
        if (!isValidMobile) {
            mRegistrationView.setInvalidNumber(validationSuccess);
            validationSuccess = false;
        }
        if (!AppUtil.verifyUserPassword(password)) {
            mRegistrationView.setInvalidPassword(validationSuccess);
            validationSuccess = false;
        }

        if (validationSuccess) {
            mRegistrationView.registeringUser();
            doRegistration(name, email, mobile, password);
        }
    }

    private void doRegistration(String name, String email, String mobile, String password) {
        mAppController.registerUser(name, email, mobile, password, new ResponseListener<Integer>() {
            @Override
            public void onResponse(Integer responseCode) {
                mRegistrationView.registrationResponse(responseCode);
            }

            @Override
            public void onError(String errorMessage) {
                mRegistrationView.showRegistrationError(errorMessage);
            }

            @Override
            public void onCompleted() {

            }
        });
    }

    @Override
    public void detachView() {
        mRegistrationView = null;
    }
}
