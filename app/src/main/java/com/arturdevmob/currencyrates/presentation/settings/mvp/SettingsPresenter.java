package com.arturdevmob.currencyrates.presentation.settings.mvp;

import com.arturdevmob.currencyrates.business.settings.SettingsInteractor;

public class SettingsPresenter {
    private SettingsInteractor interactor;
    private SettingsView view;

    public SettingsPresenter(SettingsInteractor interactor) {
        this.interactor = interactor;
    }

    public void bind(SettingsView view) {
        this.view = view;
    }

    public void unbind() {
        this.view = null;
    }

    public SettingsView getView() {
        return view;
    }

    // Показывает сообщение о запуске синхроризации валютных курсов
    // И запускает саму синхроризацию
    public void onClickUpdateCurrencyRates() {
        getView().showSnackbar("Получаю актуальный курс валют ...");

        interactor.syncCurrencyRates();
    }

    // Изменение настройки автоматического обновления валютных курсов
    public void onClickAutoUpdateCurrencyRates() {
        interactor.changeSettingAutoSyncCurrencyRates();
    }

    // Изменение темы приложения на светлую или темную
    public void onClickChangeTheme() {
        boolean isSetLightThemeApp = interactor.isSetDarkThemeApp();

        if (isSetLightThemeApp)
            getView().changeThemeOnDark();
        else
            getView().changeThemeOnLight();
    }
}
