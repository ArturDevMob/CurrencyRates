package com.arturdevmob.currencyrates.business.rates;

import com.arturdevmob.currencyrates.business.core.repositories.CurrencyRepository;
import com.arturdevmob.currencyrates.business.core.models.Currency;
import com.arturdevmob.currencyrates.business.core.repositories.SettingsRepository;
import com.arturdevmob.currencyrates.business.core.system.SyncRates;
import java.util.List;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class RatesInteractor {
    private SyncRates syncRates;
    private CurrencyRepository currencyRepository;
    private SettingsRepository settingsRepository;

    public RatesInteractor(SyncRates syncRates, CurrencyRepository currencyRepository, SettingsRepository settingsRepository) {
        this.syncRates = syncRates;
        this.currencyRepository = currencyRepository;
        this.settingsRepository = settingsRepository;
    }

    public Single<List<Currency>> getAllCurrency() {
        return currencyRepository.getListCurrency()
                .subscribeOn(Schedulers.io());
    }

    public Single<List<Currency>> getAllCurrencyBySearch(String query) {
        return currencyRepository.getListCurrencyBySearch(query)
                .subscribeOn(Schedulers.io());
    }

    // Создает задачу на синхроризацию БД курсов валют с API, если задачи ещё нет
    public void setDataSyncPeriodicalTime() {
        String idWork = settingsRepository.getIdWorkSyncCurrencyRates();
        boolean isAutoSync = settingsRepository.isAutoSyncCurrencyRate();

        if (idWork.isEmpty() && isAutoSync)
            syncRates.setWorkSync();
    }
}