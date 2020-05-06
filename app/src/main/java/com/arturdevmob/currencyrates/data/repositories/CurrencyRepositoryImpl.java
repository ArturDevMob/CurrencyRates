package com.arturdevmob.currencyrates.data.repositories;

import com.arturdevmob.currencyrates.business.core.models.Currency;
import com.arturdevmob.currencyrates.data.UtilsData;
import com.arturdevmob.currencyrates.data.sources.db.AppDatabase;
import com.arturdevmob.currencyrates.data.sources.db.entity.CurrencyEntity;
import com.arturdevmob.currencyrates.data.sources.db.entity.CurrencyEntityMapper;
import com.arturdevmob.currencyrates.data.sources.network.CbrRateApi;
import com.arturdevmob.currencyrates.business.core.repositories.CurrencyRepository;
import com.arturdevmob.currencyrates.data.sources.network.response.CurrencyListResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class CurrencyRepositoryImpl implements CurrencyRepository {
    @Inject AppDatabase database;
    @Inject CbrRateApi cbrRateApi;

    public CurrencyRepositoryImpl(AppDatabase database, CbrRateApi cbrRateApi) {
        this.database = database;
        this.cbrRateApi = cbrRateApi;
    }

    // Все валютные курсы
    // Из списка будет удалена валюта RUB (т.к. она не должна отображаться на экране с курсами валют)
    @Override
    public Single<List<Currency>> getListCurrency() {
        return this.getAllCurrencyFromDb()
                .filter(currency -> !currency.getCharCode().equals("RUB"))
                .toList();
    }

    // Все валютные курсы
    // Из списка НЕ будет удалена валюта RUB (т.к. она нужна для экрана с конвертером валют)
    @Override
    public Single<List<Currency>> getListCurrencyWithRus() {
        return this.getAllCurrencyFromDb()
                .toList();
    }

    // Валютные курсы соответствующие поисковому запросу
    // Поиск производится по полю charCode (RUB) и name (Российский рубль)
    @Override
    public Single<List<Currency>> getListCurrencyBySearch(String query) {
        return this.getAllCurrencyFromDb()
                .filter(currency -> {
                    String searchStr = String.format("%s %s", currency.getCharCode(), currency.getName())
                            .toLowerCase();

                    return searchStr.contains(query.toLowerCase());
                })
                .toList();
    }

    // Получит из БД валюту с курсом в Entity по чаркоду валюты (например: charCode = USD)
    // Преобразует в модель для бизнес слоя
    @Override
    public Single<Currency> getCurrencyByCharCode(String charCode) {
        return database.currencyDao().getCurrency(charCode)
                .flatMap((Function<List<CurrencyEntity>, Single<CurrencyEntity>>) currencyEntities -> {
                    if (currencyEntities.size() == 0) {
                        return Single.fromObservable(getAllCurrencyEntityNetworkAndAddFromDb()
                                .filter(entity -> entity.getCharCode().equals(charCode))
                        );
                    }

                    return Single.just(currencyEntities.get(0));
                })
                .map(CurrencyEntityMapper::map);
    }

    // Получит из БД валюту с курсом в Entity
    // Если в БД нет валютных курсов, обратится к методу для работы с API
    // Преобразует в модель для бизнес слоя
    private Observable<Currency> getAllCurrencyFromDb() {
        return database.currencyDao().getAllCurrency()
                .flatMapObservable(currencyEntities -> {
                    // Если получен пустой список, значит в бд нет данных, получаем их по API
                    if (currencyEntities.size() == 0) return getAllCurrencyEntityNetworkAndAddFromDb();

                    return Observable.fromIterable(currencyEntities);
                })
                .map(CurrencyEntityMapper::map);
    }

    // Получит валютные курсы по API
    // Преобразует в модель для бизнес слоя
    @Override
    public Single<List<Currency>> syncRates() {
        return getAllCurrencyEntityNetworkAndAddFromDb()
                .map(CurrencyEntityMapper::map)
                .toList();
    }

    // Получит валютные курсы по API
    // Преобразует в Entity
    // Добавит в БД
    private Observable<CurrencyEntity> getAllCurrencyEntityNetworkAndAddFromDb() {
        // Текущая дата в формате 01.01.2020 для получения курса
        String dateFormat = UtilsData.dateFormatCurrency(new Date().getTime());

        return cbrRateApi.getListCurrencyRateByDateFlo(dateFormat)
                .flatMapIterable((Function<CurrencyListResponse, Iterable<CurrencyListResponse.Currency>>) response ->
                        response.getCurrencies()
                )
                .retry((integer, throwable) -> {
                    // В сключае получения ошибки, ждет 5 секунд
                    TimeUnit.SECONDS.sleep(5);

                    // И повторяет попытку 2 раза
                    return integer >= 2;
                })
                .map(currency -> new CurrencyEntity(
                        currency.getCharCode(),
                        currency.getName(),
                        UtilsData.parseAndFormatRateCurrency(currency.getValue())
                ))
                .toList()
                .map(currencyEntities -> {
                    currencyEntities.add(
                            new CurrencyEntity(
                                    "RUB",
                                    "Российский рубль",
                                    1
                            )
                    );

                    return currencyEntities;
                })
                .flatMapObservable((Function<List<CurrencyEntity>, Observable<CurrencyEntity>>) currencyEntities -> {
                    // Удаляет из БД старые валютные курсы и добавляет новые
                    database.currencyDao().clearTable();
                    database.currencyDao().insert(currencyEntities);

                    return Observable.fromIterable(currencyEntities);
                });
    }
}
