package com.stay.connected.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stay.connected.application.ApplicationComponent;
import com.stay.connected.application.StayConnected;

/**
 * Created by karthikeyan on 7/8/17.
 */

public abstract class InjectableActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        injectComponent(((StayConnected)getApplication()).getComponent());
    }

    abstract void injectComponent(ApplicationComponent component);
}
