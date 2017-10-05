package com.stay.connected.application;

import com.stay.connected.ui.ContactsSearchActivity;
import com.stay.connected.ui.InviteUserActivity;
import com.stay.connected.ui.MainActivity;
import com.stay.connected.ui.SignInActivity;
import com.stay.connected.ui.UploadAvatarActivity;
import com.stay.connected.ui.VerifyOtpActivity;
import com.stay.connected.ui.fragment.RegistrationFragment;

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

    void inject(ContactsSearchActivity activity);

    void inject(InviteUserActivity activity);

    void inject(UploadAvatarActivity activity);

    void inject(RegistrationFragment fragment);

}
