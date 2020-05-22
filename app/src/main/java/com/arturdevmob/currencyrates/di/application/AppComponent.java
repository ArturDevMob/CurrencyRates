package com.arturdevmob.currencyrates.di.application;

import android.content.Context;
import com.arturdevmob.currencyrates.business.core.repositories.CurrencyRepository;
import com.arturdevmob.currencyrates.business.core.repositories.SettingsRepository;
import javax.inject.Singleton;
import dagger.Component;

@Component(modules = {AppModule.class, DataModule.class})
@Singleton
public interface AppComponent {
    @AppContext Context getContext();

    CurrencyRepository getCurrencyRepository();
    SettingsRepository getSettingsRepository();
}