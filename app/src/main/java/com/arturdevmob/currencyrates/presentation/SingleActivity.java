package com.arturdevmob.currencyrates.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.arturdevmob.currencyrates.R;
import com.arturdevmob.currencyrates.di.application.AppComponent;
import com.arturdevmob.currencyrates.di.application.AppModule;
import com.arturdevmob.currencyrates.di.application.DaggerAppComponent;
import com.arturdevmob.currencyrates.presentation.converter.ui.ConverterFragment;
import com.arturdevmob.currencyrates.presentation.rates.ui.RatesFragment;
import com.arturdevmob.currencyrates.presentation.settings.ui.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SingleActivity extends AppCompatActivity {
    private AppComponent appComponent;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        setupDi();

        ButterKnife.bind(this);

        startFragment();
    }

    private void startFragment() {
        createFragment(RatesFragment.newInstance(), RatesFragment.TAG);

        mBottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.rate_action:
                    createFragment(RatesFragment.newInstance(), RatesFragment.TAG);
                    break;

                case R.id.converter_action:
                    createFragment(ConverterFragment.newInstance(), ConverterFragment.TAG);
                    break;

                case R.id.settings_action:
                    createFragment(SettingsFragment.newInstance(), SettingsFragment.TAG);
                    break;
            }
            return true;
        });
    }

    private void setupTheme() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isDarkTheme = preferences.getBoolean("dark_theme_key", true);

        if (isDarkTheme) setTheme(R.style.AppThemeDark);
        else setTheme(R.style.AppThemeLight);
    }

    private void setupDi() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();
    }

    private void createFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_frame, fragment, tag)
                .commit();
    }
}
