package com.arturdevmob.currencyrates.di.converter;

import com.arturdevmob.currencyrates.business.converter.ChoiceCurrencyInteractor;
import com.arturdevmob.currencyrates.business.core.repositories.CurrencyRepository;
import com.arturdevmob.currencyrates.presentation.converter.mvp.ChoiceCurrencyPresenter;
import com.arturdevmob.currencyrates.presentation.converter.ui.ChoiceCurrencyAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class ChoiceCurrencyModule {
    @Provides
    @ChoiceCurrencyScope
    ChoiceCurrencyPresenter provideChoiceCurrencyPresenter(ChoiceCurrencyInteractor interactor) {
        return new ChoiceCurrencyPresenter(interactor);
    }

    @Provides
    @ChoiceCurrencyScope
    ChoiceCurrencyInteractor provideChoiceCurrencyInteractor(CurrencyRepository currencyRepository) {
        return new ChoiceCurrencyInteractor(currencyRepository);
    }

    @Provides
    @ChoiceCurrencyScope
    ChoiceCurrencyAdapter provideChoiceCurrencyAdapter() {
        return new ChoiceCurrencyAdapter();
    }
}
