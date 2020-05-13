package com.arturdevmob.currencyrates.presentation.settings.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import com.arturdevmob.currencyrates.R;
import com.arturdevmob.currencyrates.di.settings.DaggerSettingsComponent;
import com.arturdevmob.currencyrates.presentation.SingleActivity;
import com.arturdevmob.currencyrates.presentation.settings.mvp.SettingsPresenter;
import com.arturdevmob.currencyrates.presentation.settings.mvp.SettingsView;
import com.google.android.material.snackbar.Snackbar;
import javax.inject.Inject;

public class SettingsFragment extends PreferenceFragmentCompat implements SettingsView, PreferenceManager.OnPreferenceTreeClickListener {
    public static final String TAG = "SettingsFragment";

    // Ключ у настройки в XML
    private static final String KEY_AUTO_UPDATE_RATES = "auto_sync_currency_rates"; // автоматическое обновление курсов
    private static final String KEY_UPDATE_RATES = "sync_currency_rates"; // обновить курсы вручную
    private static final String KEY_SET_DARK_THEME = "set_dark_theme"; // утановлена светлая тема

    @Inject SettingsPresenter presenter;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        setupDi();

        setupToolbar();

        presenter.bind(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unbind();
    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(((SingleActivity) getActivity()).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()) {
            case KEY_AUTO_UPDATE_RATES:
                presenter.onClickAutoUpdateCurrencyRates();
            break;

            case KEY_UPDATE_RATES:
                presenter.onClickUpdateCurrencyRates();
            break;

            case KEY_SET_DARK_THEME:
                presenter.onClickChangeTheme();
            break;
        }

        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void changeThemeOnDark() {
        ((SingleActivity) getActivity()).changeTheme();
    }

    @Override
    public void changeThemeOnLight() {
        ((SingleActivity) getActivity()).changeTheme();
    }

    private void setupDi() {
        DaggerSettingsComponent.builder()
                .appComponent(((SingleActivity) getActivity()).getAppComponent())
                .build()
                .inject(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = ((SingleActivity) getActivity()).getToolbar();
        ((SingleActivity) getActivity()).setSupportActionBar(toolbar);

        // Да, тулбар может быть null и приложение падает
        // случается это при перезапуске активити, когда пользователь меняет тему
        if (toolbar != null)
            toolbar.setTitle(R.string.settings);
    }
}