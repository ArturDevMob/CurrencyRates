package com.arturdevmob.currencyrates.business.core.repositories;

import com.arturdevmob.currencyrates.business.core.models.Currency;
import java.util.List;
import io.reactivex.Single;

public interface CurrencyRepository {
    Single<List<Currency>> getListCurrency();
    Single<List<Currency>> getListCurrencyBySearch(String query);
    Single<List<Currency>> getListCurrencyWithRus();
    Single<Currency> getCurrencyByCharCode(String charCode);
    Single<List<Currency>> loadAndGetNewCurrencyRates();
}