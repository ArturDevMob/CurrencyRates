package com.arturdevmob.currencyrates.data.repositories;

import com.arturdevmob.currencyrates.business.core.repositories.SettingsRepository;
import com.arturdevmob.currencyrates.data.sources.local.LocalStorage;

public class SettingsRepositoryImpl implements SettingsRepository {
    private static final String KEY_ID_WORK_SYNC_CURRENCY_RATES = "id_word_sync_currency_rates"; // ID задачи WorkManager'а синхр. вал. курс.
    private static final String KEY_AUTO_UPDATE_RATES = "auto_sync_currency_rates"; // автоматическое обновление курсов
    private static final String KEY_SHOW_LIGHT_THEME = "show_light_theme"; // утановлена светлая тема
    private LocalStorage localStorage;

    public SettingsRepositoryImpl(LocalStorage localStorage) {
        this.localStorage = localStorage;
    }

    // Сохраняет ID задачи по синхроризации валютных курсов
    @Override
    public void setIdWorkSyncCurrencyRates(String value) {
        localStorage.putString(KEY_ID_WORK_SYNC_CURRENCY_RATES, value);
    }

    // Возвращает ID задачи по синхроризации валютных курсов
    @Override
    public String getIdWorkSyncCurrencyRates() {
        return localStorage.getString(KEY_ID_WORK_SYNC_CURRENCY_RATES, "");
    }

    // Сохраняет булевое значение на разрешение автосинхроризации валютных курсов
    @Override
    public void setAutoSyncCurrencyRate(boolean isAuto) {
        localStorage.putBoolean(KEY_AUTO_UPDATE_RATES, isAuto);
    }

    // Возвращает булевое значение на разрешение автосинхроризации валютных курсов
    @Override
    public boolean isAutoSyncCurrencyRate() {
        return localStorage.getBoolean(KEY_AUTO_UPDATE_RATES, true);
    }

    // Возвращает булевое значение установлена ли светлая тема приложения по умолчанию
    @Override
    public boolean isSetLightThemeApp() {
        return localStorage.getBoolean(KEY_SHOW_LIGHT_THEME, false);
    }
}
