package com.arturdevmob.currencyrates.di.settings;

import com.arturdevmob.currencyrates.di.application.AppComponent;
import com.arturdevmob.currencyrates.presentation.settings.ui.SettingsFragment;
import dagger.Component;

@Component(dependencies = AppComponent.class, modules = {SettingsModule.class})
@SettingsScope
public interface SettingsComponent {
    void inject(SettingsFragment settingsFragment);
}
