package com.stay.connected.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.stay.connected.R;
import com.stay.connected.application.ApplicationComponent;
import com.stay.connected.network.ResponseListener;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerifyOtpActivity extends InjectableActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView((R.id.edt_otp))
    EditText edtOtp;

    @BindView((R.id.tv_otp_error))
    TextView tvOtpError;

    private static ActivityState mActivityState = ActivityState.INACTIVE;

    private enum ActivityState {
        VERIFYING_OTP,
        REQUESTING_OTP,
        INACTIVE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    void injectComponent(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mActivityState != ActivityState.INACTIVE) {
            showProgressDialog(null, false);
        } else if (mAppPreference.getUserVerificationState()) {
            startSignInActivity(true);
        }
    }

    @OnClick(R.id.btn_resend)
    public void onClickResend() {
        mActivityState = ActivityState.REQUESTING_OTP;
        showProgressDialog(null, false);
        mAppController.resendOtp(mAppPreference.getUserEmail(), new ResendOtpListener(this));
    }

    @OnClick(R.id.btn_verify)
    public void onClickVerify() {
        String otp = edtOtp.getText().toString().trim();
        tvOtpError.setVisibility(View.INVISIBLE);
        if (TextUtils.isEmpty(otp)) {
            edtOtp.requestFocus();
            return;
        }

        mActivityState = ActivityState.VERIFYING_OTP;
        showProgressDialog(null, false);
        mAppController.verifyOTP(mAppPreference.getUserEmail(), otp, new VerifyOtpListener(this));
    }

    private void setInvalidOtp() {
        tvOtpError.setVisibility(View.VISIBLE);
    }

    /**
     * Listen to verify otp request
     */
    private static class VerifyOtpListener implements ResponseListener<Integer> {

        private final WeakReference<VerifyOtpActivity> mReference;

        public VerifyOtpListener(VerifyOtpActivity activity) {
            mReference = new WeakReference<VerifyOtpActivity>(activity);
        }

        @Override
        public void onResponse(Integer responseCode) {
            if (mReference.get() != null) {
                switch (responseCode) {
                    case 200:
                        mReference.get().startSignInActivity(true);
                        break;
                    case 404:
                        mReference.get().setInvalidOtp();
                        break;
                    case 400:
                    default:
                        mReference.get().showAlertDialog(R.string.text_alert_verify_otp_error);
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

    /**
     * Listen to resend otp request
     */
    private static class ResendOtpListener implements ResponseListener<Integer> {

        private final WeakReference<VerifyOtpActivity> mReference;

        public ResendOtpListener(VerifyOtpActivity activity) {
            mReference = new WeakReference<VerifyOtpActivity>(activity);
        }

        @Override
        public void onResponse(Integer responseCode) {
            if (mReference.get() != null) {
                switch (responseCode) {
                    case 200:
                        mReference.get().showAlertDialog(R.string.text_alert_otp_resend_success);
                        break;
                    case 400:
                    default:
                        mReference.get().showAlertDialog(R.string.text_alert_resend_otp_error);
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

}
