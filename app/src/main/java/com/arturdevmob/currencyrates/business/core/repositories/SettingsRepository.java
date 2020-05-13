package com.arturdevmob.currencyrates.business.core.repositories;

public interface SettingsRepository {
    void setIdWorkSyncCurrencyRates(String value);
    String getIdWorkSyncCurrencyRates();
    boolean isAutoSyncCurrencyRate();
    boolean isSetDarkThemeApp();
}
