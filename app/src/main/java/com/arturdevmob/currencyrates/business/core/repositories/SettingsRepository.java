package com.arturdevmob.currencyrates.business.core.repositories;

public interface SettingsRepository {
    void setIdWorkSyncCurrencyRates(String value);
    String getIdWorkSyncCurrencyRates();
    long getDateLastUpdateRates();
    void setDateLastUpdateRates(long date);
    boolean isAutoSyncCurrencyRate();
    boolean isSetDarkThemeApp();
}
