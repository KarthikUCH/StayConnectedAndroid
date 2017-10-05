package com.stay.connected.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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
    private static ActivityState mActivityState = ActivityState.INACTIVE;

    private static WeakReference<SignInActivity> mReference;

    private enum ActivityState {
        REGISTERING,
        SIGNING_IN,
        INACTIVE
    }

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
        mReference = new WeakReference<SignInActivity>(this);

        showSignInFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAppController.getUserLogInState()) {
            startInviteUserActivity(true);
        } else if (!TextUtils.isEmpty(mAppPreference.getUserEmail())) {
            if (!mAppPreference.getUserVerificationState()) {
                startVerifyOtpActivity(true);
            }
        }

        if (mActivityState != ActivityState.INACTIVE) {
            showProgressDialog(null, false);
        }
    }

    @Override
    void injectComponent(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.url_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.menu_url) {
            showUrlAlertDialog();
        }
        return super.onOptionsItemSelected(item);
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
        mActivityState = ActivityState.SIGNING_IN;
        showProgressDialog(null, false);
        mAppController.signInUser(email, password, new UserSignInListener(this));
    }

    @Override
    public void registeringUser() {
        mActivityState = ActivityState.REGISTERING;
        showProgressDialog(null, false);
    }

    @Override
    public void registrationResponse(Integer responseCode) {
        resetActivityState();
        if (mReference.get() != null) {
            switch (responseCode) {
                case 200:
                    mReference.get().startVerifyOtpActivity(true);
                    break;
                case 409:
                    mReference.get().showAlertDialog(R.string.text_alert_user_registered_already);
                    break;
                case 400:
                default:
                    mReference.get().showAlertDialog(R.string.text_alert_user_registration_error);
                    break;
            }
        }
    }

    @Override
    public void showRegistrationError(String errorMsg) {
        resetActivityState();
    }

    private void resetActivityState() {
        mActivityState = ActivityState.INACTIVE;
        if (mReference.get() != null) {
            mReference.get().dismissProgressDialog(null);
        }
    }

    /**
     * Listen to user sign in request
     */
    private static class UserSignInListener implements ResponseListener<Integer> {

        private final WeakReference<SignInActivity> mReference;

        public UserSignInListener(SignInActivity activity) {
            mReference = new WeakReference<SignInActivity>(activity);
        }

        @Override
        public void onResponse(Integer responseCode) {
            if (mReference.get() != null) {
                switch (responseCode) {
                    case 200:
                        mReference.get().startInviteUserActivity(true);
                        break;
                    case 401:
                    case 404:
                        mReference.get().setSignInError(responseCode);
                        break;
                    case 400:
                    default:
                        mReference.get().showAlertDialog(R.string.text_alert_user_login_error);
                        break;
                }
            }
        }

        @Override
        public void onError(String errorMessage) {

        }

        @Override
        public void onCompleted() {
            mActivityState = ActivityState.INACTIVE;
            if (mReference.get() != null) {
                mReference.get().dismissProgressDialog(null);
            }
        }
    }

    public void setSignInError(int errorCode) {

        SignInFragment fragment = (SignInFragment) getSupportFragmentManager().findFragmentByTag(SignInFragment.class.getName());
        if (fragment != null) {
            fragment.setError(errorCode);
        }

    }
}
