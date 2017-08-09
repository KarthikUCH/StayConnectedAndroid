package com.stay.connected.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

    @OnClick(R.id.btn_resend)
    public void onClickResend() {
        mAppController.resendOtp(mAppPreference.getUserEmail(), new ResendOtpListener(this));
    }

    @OnClick(R.id.btn_verify)
    public void onClickVerify() {
        String otp = edtOtp.getText().toString().trim();
        if (TextUtils.isEmpty(otp)) {
            edtOtp.requestFocus();
            return;
        }

        mAppController.verifyOTP(mAppPreference.getUserEmail(), otp, new VerifyOtpListener(this));
    }

    private void setInvalidOtp() {
        tvOtpError.setVisibility(View.VISIBLE);
    }

    /**
     * Listen to verify otp request
     */
    private static class VerifyOtpListener implements ResponseListener<Boolean> {

        private final WeakReference<VerifyOtpActivity> mReference;

        public VerifyOtpListener(VerifyOtpActivity activity) {
            mReference = new WeakReference<VerifyOtpActivity>(activity);
        }

        @Override
        public void onResponse(Boolean response) {
            if (mReference.get() != null) {
                if (response) {
                    mReference.get().startSignInActivity(true);
                } else {
                    mReference.get().setInvalidOtp();
                }
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
     * Listen to resend otp request
     */
    private static class ResendOtpListener implements ResponseListener<Boolean> {

        private final WeakReference<VerifyOtpActivity> mReference;

        public ResendOtpListener(VerifyOtpActivity activity) {
            mReference = new WeakReference<VerifyOtpActivity>(activity);
        }

        @Override
        public void onResponse(Boolean response) {

        }

        @Override
        public void onError(String errorMessage) {

        }

        @Override
        public void onCompleted() {

        }
    }

}
