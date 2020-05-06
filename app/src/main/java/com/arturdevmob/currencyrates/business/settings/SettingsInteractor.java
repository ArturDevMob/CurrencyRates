package com.arturdevmob.currencyrates.business.settings;

import com.arturdevmob.currencyrates.business.core.repositories.CurrencyRepository;
import com.arturdevmob.currencyrates.business.core.repositories.SettingsRepository;
import com.arturdevmob.currencyrates.business.core.system.SyncRates;

public class SettingsInteractor {
    private SettingsRepository settingsRepository;
    private SyncRates syncRates;

    public SettingsInteractor(SettingsRepository settingsRepository, SyncRates syncRates) {
        this.settingsRepository = settingsRepository;
        this.syncRates = syncRates;
    }

    // Запускает WorkManager для установки задачи по БД валютных курсов с API
    public void syncCurrencyRates() {
        syncRates.runSync();
    }

    // Изменяет настройку автосинхроризации валютных курсов
    // И удаляет или устанавливает задачу по синхроризации в зависимости от настройки
    public void changeSettingAutoSyncCurrencyRates() {
        boolean isAutoSync = settingsRepository.isAutoSyncCurrencyRate();

        if (isAutoSync) {
            syncRates.setWorkSync();
        } else {
            syncRates.removeWorkSync();
        }
    }

    public boolean isSetLightThemeApp() {
        return settingsRepository.isSetLightThemeApp();
    }
}
