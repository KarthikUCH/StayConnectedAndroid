package com.stay.connected.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.stay.connected.R;
import com.stay.connected.application.ApplicationComponent;
import com.stay.connected.network.ResponseListener;
import com.stay.connected.ui.fragment.RegistrationFragment;
import com.stay.connected.ui.fragment.SignInFragment;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends InjectableActivity implements RegistrationFragment.OnFragmentRegistrationListener, SignInFragment.OnFragmentSignInListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_sing_in)
    TextView tvSignIn;

    @BindView(R.id.tv_register)
    TextView tvRegister;

    private SignInFragment mSignInFragment;
    private RegistrationFragment mRegistrationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSignInFragment = SignInFragment.newInstance();
        mRegistrationFragment = RegistrationFragment.newInstance();

        showSignInFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAppController.getUserLogInState()) {
            startInviteUeserActivity(true);
        } else if (!TextUtils.isEmpty(mAppPreference.getUserEmail())) {
            if (!mAppPreference.getUserVerificationState()) {
                startVerifyOtpActivity(true);
            }
        }
    }

    @Override
    void injectComponent(ApplicationComponent component) {
        component.inject(this);
    }


    @OnClick(R.id.tv_sing_in)
    public void showSignInFragment() {

        tvSignIn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_accent));
        tvRegister.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_grey));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mSignInFragment, SignInFragment.class.getName());
        transaction.commit();
    }


    @OnClick(R.id.tv_register)
    public void showRegistrationFragment() {

        tvSignIn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_grey));
        tvRegister.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_accent));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mRegistrationFragment, RegistrationFragment.class.getName());
        transaction.commit();
    }

    @Override
    public void signInUser(String email, String password) {
        mAppController.signInUser(email, password, new UserSignInListener(this));
    }

    @Override
    public void registerUser(String name, String email, String mobile, String password) {
        mAppController.registerUser(name, email, mobile, password, new UserRegistrationListner(this));
    }

    /**
     * Listen to user registration request
     */
    private static class UserRegistrationListner implements ResponseListener<Boolean> {

        private final WeakReference<SignInActivity> mReference;

        public UserRegistrationListner(SignInActivity activity) {
            mReference = new WeakReference<SignInActivity>(activity);
        }

        @Override
        public void onResponse(Boolean response) {
            if (mReference.get() != null && response) {
                mReference.get().startVerifyOtpActivity(true);
            }
        }

        @Override
        public void onError(String errorMessage) {

        }

        @Override
        public void onCompleted() {

        }
    }

    /**
     * Listen to user sign in request
     */
    private static class UserSignInListener implements ResponseListener<Boolean> {

        private final WeakReference<SignInActivity> mReference;

        public UserSignInListener(SignInActivity activity) {
            mReference = new WeakReference<SignInActivity>(activity);
        }

        @Override
        public void onResponse(Boolean response) {
            if (mReference.get() != null && response) {
                mReference.get().startInviteUeserActivity(true);
            }
        }

        @Override
        public void onError(String errorMessage) {

        }

        @Override
        public void onCompleted() {

        }
    }
}
