package com.arturdevmob.currencyrates.business.core.presenters;

public interface RatesPresenter {
    void notifyAboutOutdatedRates();
    void allowUpdateManuallyRates();
}
