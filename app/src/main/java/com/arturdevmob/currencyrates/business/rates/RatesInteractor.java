package com.arturdevmob.currencyrates.business.rates;

import com.arturdevmob.currencyrates.business.core.presenters.RatesPresenter;
import com.arturdevmob.currencyrates.business.core.repositories.CurrencyRepository;
import com.arturdevmob.currencyrates.business.core.models.Currency;
import com.arturdevmob.currencyrates.business.core.repositories.SettingsRepository;
import java.util.Calendar;
import java.util.List;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class RatesInteractor {
    private RatesPresenter presenter;
    private CurrencyRepository currencyRepository;
    private SettingsRepository settingsRepository;

    public RatesInteractor(CurrencyRepository currencyRepository, SettingsRepository settingsRepository) {
        this.currencyRepository = currencyRepository;
        this.settingsRepository = settingsRepository;
    }

    public void setPresenter(RatesPresenter presenter) {
        this.presenter = presenter;
    }

    public Single<List<Currency>> getAllCurrency() {
        if (settingsRepository.isAutoSyncCurrencyRate()) {
            if (isOldCurrencyRates()) {
                return this.getNewCurrencyRates();
            }
        } else {
            presenter.allowUpdateManuallyRates();
        }

        return currencyRepository.getListCurrency()
                .subscribeOn(Schedulers.io());
    }

    public Single<List<Currency>> getAllCurrencyBySearch(String query) {
        return currencyRepository.getListCurrencyBySearch(query)
                .subscribeOn(Schedulers.io());
    }

    public Single<List<Currency>> getNewCurrencyRates() {
        return currencyRepository.loadAndGetNewCurrencyRates()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(currencies -> settingsRepository.setDateLastUpdateRates(System.currentTimeMillis()));
    }

    private boolean isOldCurrencyRates() {
        // Дата прошлого обновления курсов
        Calendar lastUpdateDateRates = Calendar.getInstance();
        long time = settingsRepository.getDateLastUpdateRates();
        lastUpdateDateRates.setTimeInMillis(time);
        // Увеличиваем на 3 (время актуальности курсов) часа чтобы проверить с текущей датой
        lastUpdateDateRates.roll(Calendar.HOUR_OF_DAY, 3);

        if (lastUpdateDateRates.getTimeInMillis() < System.currentTimeMillis())
            return true; // Курсы устарели
        else
            return false; // Курсы свежие
    }
}