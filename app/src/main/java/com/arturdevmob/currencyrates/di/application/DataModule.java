package com.arturdevmob.currencyrates.di.application;

import android.content.Context;
import android.content.SharedPreferences;
import com.arturdevmob.currencyrates.business.core.repositories.CurrencyRepository;
import com.arturdevmob.currencyrates.business.core.repositories.SettingsRepository;
import com.arturdevmob.currencyrates.business.core.system.SyncRates;
import com.arturdevmob.currencyrates.data.sources.db.AppDatabase;
import com.arturdevmob.currencyrates.data.sources.local.LocalStorage;
import com.arturdevmob.currencyrates.data.sources.network.CbrRateApi;
import com.arturdevmob.currencyrates.data.repositories.CurrencyRepositoryImpl;
import com.arturdevmob.currencyrates.data.repositories.SettingsRepositoryImpl;
import com.arturdevmob.currencyrates.data.systemfiles.syncrates.SyncRatesImpl;
import javax.inject.Singleton;
import androidx.preference.PreferenceManager;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@Module
public class DataModule {
    public static final String DB_NAME = "currency_rates_database";
    public static final String API_CURRENCY_URL = "https://www.cbr.ru/scripts/";

    @Provides
    @Singleton
    SyncRates provideDataSync(@AppContext Context context, CurrencyRepository currencyRepository, SettingsRepository settingsRepository) {
        return new SyncRatesImpl(context, currencyRepository, settingsRepository);
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(@AppContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(API_CURRENCY_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    CbrRateApi provideCbrRateApi(Retrofit retrofit) {
        return retrofit.create(CbrRateApi.class);
    }

    @Provides
    @Singleton
    LocalStorage provideLocalStorage(SharedPreferences preferences) {
        return new LocalStorage(preferences);
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(@AppContext Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    CurrencyRepository provideCurrencyRepository(AppDatabase database, CbrRateApi cbrRateApi) {
        return new CurrencyRepositoryImpl(database, cbrRateApi);
    }

    @Provides
    @Singleton
    SettingsRepository provideSettingsRepository(LocalStorage localStorage) {
        return new SettingsRepositoryImpl(localStorage);
    }
}
