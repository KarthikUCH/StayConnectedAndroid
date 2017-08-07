package com.stay.connected.application;

import android.app.Application;
import android.content.Context;

import com.stay.connected.network.RestServiceFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by karthikeyan on 7/8/17.
 */
@Module
public class ApplicationModule {

    private Application mApp;

    ApplicationModule(Application app) {
        mApp = app;
    }


    @Provides
    @Singleton
    Context providesApplicationContext(){
        return mApp;
    }

    @Provides
    @Singleton
    RestServiceFactory providesRestServiceFactory(){
        return new RestServiceFactory.Impl();
    }
}
