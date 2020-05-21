package com.arturdevmob.currencyrates.presentation.rates.mvp;

import com.arturdevmob.currencyrates.business.core.models.Currency;
import java.util.List;

public interface RatesView {
    void showProgress(boolean isShow);
    void showErrorLoad(boolean isShow);
    void showCurrencyRates(boolean isShow);
    void showCurrencyRates(List<Currency> currencyList);
    void showSnackBar(String message);
    void showIconUpdateRates();
}
