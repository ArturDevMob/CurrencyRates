package com.arturdevmob.currencyrates.di.converter;

import com.arturdevmob.currencyrates.di.application.AppComponent;
import com.arturdevmob.currencyrates.presentation.converter.ui.ConverterFragment;
import dagger.Component;

@Component(dependencies = AppComponent.class, modules = {ConverterModule.class})
@ConverterScope
public interface ConverterComponent {
    void inject(ConverterFragment converterFragment);
}
