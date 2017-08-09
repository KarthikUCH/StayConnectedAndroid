package com.stay.connected.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stay.connected.application.AppController;
import com.stay.connected.application.AppPreference;
import com.stay.connected.application.ApplicationComponent;
import com.stay.connected.application.StayConnected;

import javax.inject.Inject;

/**
 * Created by karthikeyan on 7/8/17.
 */

public abstract class InjectableActivity extends AppCompatActivity {

    @Inject
    protected AppController mAppController;

    @Inject
    protected AppPreference mAppPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectComponent(((StayConnected) getApplication()).getComponent());
    }

    abstract void injectComponent(ApplicationComponent component);


    protected void startVerifyOtpActivity(boolean finish) {
        Intent intent = new Intent(this, VerifyOtpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }

    protected void startSignInActivity(boolean finish) {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }

    protected void startMainActivity(boolean finish) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }
}
