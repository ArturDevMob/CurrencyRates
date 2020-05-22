package com.arturdevmob.currencyrates.di.settings;

import com.arturdevmob.currencyrates.business.core.repositories.SettingsRepository;
import com.arturdevmob.currencyrates.business.settings.SettingsInteractor;
import com.arturdevmob.currencyrates.presentation.settings.mvp.SettingsPresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class SettingsModule {
    @Provides
    @SettingsScope
    SettingsPresenter provideSettingsPresenter(SettingsInteractor interactor) {
        return new SettingsPresenter(interactor);
    }

    @Provides
    @SettingsScope
    SettingsInteractor provideSettingsInteractor(SettingsRepository settingsRepository) {
        return new SettingsInteractor(settingsRepository);
    }
}
