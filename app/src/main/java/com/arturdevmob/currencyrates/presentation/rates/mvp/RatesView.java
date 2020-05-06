package com.arturdevmob.currencyrates.presentation.rates.mvp;

import com.arturdevmob.currencyrates.business.core.models.Currency;
import java.util.List;

public interface RatesView {
    void showProgress();
    void showErrorLoad();
    void showCurrencyRates(List<Currency> currencyList);
}
