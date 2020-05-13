package com.arturdevmob.currencyrates.di.converter;

import com.arturdevmob.currencyrates.business.converter.ConverterInteractor;
import com.arturdevmob.currencyrates.business.core.repositories.CurrencyRepository;
import com.arturdevmob.currencyrates.presentation.converter.mvp.ConverterPresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class ConverterModule {
    @Provides
    @ConverterScope
    ConverterPresenter provideConverterPresenter(ConverterInteractor interactor) {
        return new ConverterPresenter(interactor);
    }

    @Provides
    @ConverterScope
    ConverterInteractor provideConverterInteractor(CurrencyRepository currencyRepository) {
        return new ConverterInteractor(currencyRepository);
    }
}
