package com.arturdevmob.currencyrates.data.systemfiles.syncrates;

import android.content.Context;
import com.arturdevmob.currencyrates.business.core.system.SyncRates;
import com.arturdevmob.currencyrates.di.application.AppModule;
import com.arturdevmob.currencyrates.di.application.DaggerAppComponent;
import com.arturdevmob.currencyrates.di.application.DataModule;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SyncRatesWorker extends Worker {
    private SyncRates syncRates;

    public SyncRatesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        syncRates = DaggerAppComponent.builder()
                .dataModule(new DataModule())
                .appModule(new AppModule(context))
                .build()
                .getDataSync();
    }

    @NonNull
    @Override
    public Result doWork() {
        syncRates.runSync(); // Запуск синхроризации валютных курсов
        syncRates.setWorkSync(); // Установка новой задачи для следующей синхроризации валютных курсов

        return Result.success();
    }
}
