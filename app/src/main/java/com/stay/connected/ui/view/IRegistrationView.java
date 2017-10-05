package com.stay.connected.ui.view;

/**
 * Created by karthikeyan on 5/10/17.
 */

public interface IRegistrationView {

    void setInvalidName(boolean requestFocus);

    void setInvalidEmail(boolean requestFocus);

    void setInvalidNumber(boolean requestFocus);

    void setInvalidPassword(boolean requestFocus);

    void registeringUser();

    void registrationResponse(Integer responseCode);

    void showRegistrationError(String errorMsg);
}
