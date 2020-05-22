package com.arturdevmob.currencyrates.business.settings;

import com.arturdevmob.currencyrates.business.core.repositories.SettingsRepository;

public class SettingsInteractor {
    private SettingsRepository settingsRepository;

    public SettingsInteractor(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public boolean isSetDarkThemeApp() {
        return settingsRepository.isSetDarkThemeApp();
    }
}
