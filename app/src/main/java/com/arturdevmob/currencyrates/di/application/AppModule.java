package com.arturdevmob.currencyrates.di.application;

import android.content.Context;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    @AppContext
    Context provideContext() {
        return this.context;
    }
}
