package com.arturdevmob.currencyrates.business.core.system;

public interface SyncRates {
    void runSync();
    void setWorkSync();
    void removeWorkSync();
}
