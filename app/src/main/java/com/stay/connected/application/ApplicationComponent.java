package com.stay.connected.application;

import com.stay.connected.ui.MainActivity;
import com.stay.connected.ui.SignInActivity;
import com.stay.connected.ui.VerifyOtpActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by karthikeyan on 7/8/17.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(SignInActivity activity);

    void inject(MainActivity activity);

    void inject(VerifyOtpActivity activity);
}
