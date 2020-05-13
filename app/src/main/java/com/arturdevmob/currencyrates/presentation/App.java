package com.arturdevmob.currencyrates.presentation;

import android.app.Application;

import com.arturdevmob.currencyrates.di.application.AppComponent;
import com.arturdevmob.currencyrates.di.application.AppModule;
import com.arturdevmob.currencyrates.di.application.DaggerAppComponent;
import com.arturdevmob.currencyrates.di.application.DataModule;

public class App extends Application {
    private static App instance;
    private AppComponent appComponent;

    public static App getInstance() {
        return instance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        setupDi();
    }

    private void setupDi() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule())
                .build();
    }
}
