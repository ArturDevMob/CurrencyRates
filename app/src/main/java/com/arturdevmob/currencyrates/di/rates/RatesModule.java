package com.arturdevmob.currencyrates.di.rates;

import com.arturdevmob.currencyrates.business.core.repositories.CurrencyRepository;
import com.arturdevmob.currencyrates.business.core.repositories.SettingsRepository;
import com.arturdevmob.currencyrates.business.rates.RatesInteractor;
import com.arturdevmob.currencyrates.presentation.rates.mvp.RatesPresenterImpl;
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
    RatesPresenterImpl provideRatePresenter(RatesInteractor interactor) {
        return new RatesPresenterImpl(interactor);
    }

    @Provides
    @RatesScope
    RatesInteractor provideRateInteractor(CurrencyRepository repository, SettingsRepository settingsRepository) {
        return new RatesInteractor(repository, settingsRepository);
    }
}
