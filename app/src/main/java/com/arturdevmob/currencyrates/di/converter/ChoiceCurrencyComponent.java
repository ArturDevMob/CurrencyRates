package com.arturdevmob.currencyrates.di.converter;

import com.arturdevmob.currencyrates.di.application.AppComponent;
import com.arturdevmob.currencyrates.presentation.converter.ui.ChoiceCurrencyDialog;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = {ChoiceCurrencyModule.class})
@ChoiceCurrencyScope
public interface ChoiceCurrencyComponent {
    void inject(ChoiceCurrencyDialog choiceCurrencyDialog);
}
