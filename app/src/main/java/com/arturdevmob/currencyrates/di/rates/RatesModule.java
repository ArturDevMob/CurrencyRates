package com.arturdevmob.currencyrates.di.rates;

import com.arturdevmob.currencyrates.business.core.repositories.CurrencyRepository;
import com.arturdevmob.currencyrates.business.core.repositories.SettingsRepository;
import com.arturdevmob.currencyrates.business.core.system.SyncRates;
import com.arturdevmob.currencyrates.business.rates.RatesInteractor;
import com.arturdevmob.currencyrates.presentation.rates.mvp.RatesPresenter;
import com.arturdevmob.currencyrates.presentation.rates.ui.RatesAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class RatesModule {
    @Provides
    @RatesScope
    RatesAdapter provideRateAdapter() {
        return new RatesAdapter();
    }

    @Provides
    @RatesScope
    RatesPresenter provideRatePresenter(RatesInteractor interactor) {
        return new RatesPresenter(interactor);
    }

    @Provides
    @RatesScope
    RatesInteractor provideRateInteractor(SyncRates syncRates, CurrencyRepository repository, SettingsRepository settingsRepository) {
        return new RatesInteractor(syncRates, repository, settingsRepository);
    }
}
