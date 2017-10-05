package com.stay.connected.ui.Presenter;

import com.stay.connected.ui.view.IRegistrationView;

/**
 * Created by karthikeyan on 5/10/17.
 */

public interface IRegistrationPresenter {

    void attachView(IRegistrationView registrationView);

    void registerUser(String name, String email, boolean isValidMobile, String mobile, String password);

    void detachView();

}
