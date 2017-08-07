package com.stay.connected.ui;

import android.os.Bundle;

import com.stay.connected.R;
import com.stay.connected.application.ApplicationComponent;

public class MainActivity extends InjectableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    void injectComponent(ApplicationComponent component) {
        component.inject(this);
    }
}
