package com.arturdevmob.currencyrates.di.rates;

import com.arturdevmob.currencyrates.di.application.AppComponent;
import com.arturdevmob.currencyrates.presentation.rates.ui.RatesFragment;
import dagger.Component;

@Component(dependencies = AppComponent.class, modules = {RatesModule.class})
@RatesScope
public interface RatesComponent {
    void inject(RatesFragment rateCbrFragment);
}
