package com.arturdevmob.currencyrates.business.converter;

import com.arturdevmob.currencyrates.business.core.models.Currency;
import com.arturdevmob.currencyrates.business.core.repositories.CurrencyRepository;
import java.util.List;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ChoiceCurrencyInteractor {
    private CurrencyRepository currencyRepository;

    public ChoiceCurrencyInteractor(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public Single<List<Currency>> getAllCurrency() {
        return currencyRepository.getListCurrencyWithRus()
                .subscribeOn(Schedulers.io());
    }
}
