package com.arturdevmob.currencyrates.business.core.repositories;

public interface SettingsRepository {
    void setIdWorkSyncCurrencyRates(String value);
    String getIdWorkSyncCurrencyRates();

    void setAutoSyncCurrencyRate(boolean isAuto);
    boolean isAutoSyncCurrencyRate();

    boolean isSetLightThemeApp();
}
