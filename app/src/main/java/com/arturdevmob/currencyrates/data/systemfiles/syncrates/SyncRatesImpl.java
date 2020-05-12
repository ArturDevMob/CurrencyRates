package com.arturdevmob.currencyrates.data.systemfiles.syncrates;

import android.content.Context;
import com.arturdevmob.currencyrates.business.core.models.Currency;
import com.arturdevmob.currencyrates.business.core.repositories.CurrencyRepository;
import com.arturdevmob.currencyrates.business.core.repositories.SettingsRepository;
import com.arturdevmob.currencyrates.business.core.system.SyncRates;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SyncRatesImpl implements SyncRates {
    private Context context;
    private CurrencyRepository currencyRepository;
    private SettingsRepository settingsRepository;

    public SyncRatesImpl(Context context, CurrencyRepository currencyRepository, SettingsRepository settingsRepository) {
        this.context = context;
        this.currencyRepository = currencyRepository;
        this.settingsRepository = settingsRepository;
    }

    // Выолняет синхроризацию валютных курсов и показывает notification о статусе задачи
    @Override
    public void runSync() {
        NotifiOnSync notifiOnSync = new NotifiOnSync(context);

        currencyRepository.syncRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<Currency>>() {
                    @Override
                    public void onSuccess(List<Currency> currencies) {
                        notifiOnSync.showNotifiSuccessSyncCurrencyRate();
                    }

                    @Override
                    public void onError(Throwable e) {
                        notifiOnSync.showNotifiErrorSyncCurrencyRate();
                    }
                });
    }

    // Устанавливает задачу по синхроризации валютных курсов
    @Override
    public void setWorkSync() {
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SyncRatesWorker.class)
                .setInitialDelay(getIntervalBeforeNextSync(), TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance()
                .enqueue(request);

        settingsRepository.setIdWorkSyncCurrencyRates(request.getId().toString());
    }

    // Удаляет задачу по синхроризации валютных курсов
    @Override
    public void removeWorkSync() {
        String workId = settingsRepository.getIdWorkSyncCurrencyRates();

        WorkManager.getInstance()
                .cancelWorkById(UUID.fromString(workId));

        settingsRepository.setIdWorkSyncCurrencyRates(""); // Удаляем ID задачи из preferences
    }

    // WorkManager'у для установки задачи требуется интервал в мс, по истечению которого будет запущена здача
    // Метод высчитывает интервал между текущем временем и временем, на которое нужно установить выполнение задачи
    // Валютные курсы банка обновляются ПН-СБ в 14:00 по МСК
    private long getIntervalBeforeNextSync() {
        GregorianCalendar calendarMsk = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow")); // Текущее МСК время
        GregorianCalendar calendarUser = new GregorianCalendar(); // Текущее время пользователя
        long intervalDate = 0; // Интервал между датами

        // Если время у календаря >= 15 часов, то переводим календарь на +1 день
        if (calendarMsk.get(Calendar.HOUR_OF_DAY) >= 15) {
            calendarMsk.roll(Calendar.DAY_OF_MONTH, 1);
        }

        // Если у календаря день недели ВСК, то переводим календарь на +1 день
        if (calendarMsk.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendarMsk.roll(Calendar.DAY_OF_MONTH, 1);
        }

        // Устанавливаем время на 15:00
        // После этого calendarMsk содержит дату и время, в которое нужно получить новые курсы от банка
        calendarMsk.set(Calendar.HOUR_OF_DAY, 15);
        calendarMsk.set(Calendar.MINUTE, 0);

        // Считаем интервал между датой, когда обновится курс валют и текущем временем
        intervalDate = calendarMsk.getTimeInMillis() - calendarUser.getTimeInMillis();

        return intervalDate;
    }
}
