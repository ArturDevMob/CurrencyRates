package com.arturdevmob.currencyrates.presentation.converter.mvp;

import com.arturdevmob.currencyrates.business.core.models.Currency;
import java.util.List;

public interface ChoiceCurrencyView {
    void showProgress(boolean show);
    void showListCurrency(List<Currency> currencyList);
}
