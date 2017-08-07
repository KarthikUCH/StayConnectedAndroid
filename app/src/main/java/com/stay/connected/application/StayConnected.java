package com.stay.connected.application;

import android.app.Application;

/**
 * Created by karthikeyan on 7/8/17.
 */

public class StayConnected extends Application {

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return mComponent;
    }
}
