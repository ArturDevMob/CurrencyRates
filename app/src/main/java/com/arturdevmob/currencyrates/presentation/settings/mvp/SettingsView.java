package com.arturdevmob.currencyrates.presentation.settings.mvp;

public interface SettingsView {
    void showSnackbar(String message);
    void setLightThemeApp();
    void setDarkThemeApp();
}
