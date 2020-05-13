package com.arturdevmob.currencyrates.presentation.rates.mvp;

import com.arturdevmob.currencyrates.business.core.models.Currency;
import com.arturdevmob.currencyrates.business.rates.RatesInteractor;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

public class RatesPresenter {
    private CompositeDisposable compositeDisposable;
    private RatesInteractor interactor;
    private RatesView view;

    public RatesPresenter(RatesInteractor interactor) {
        this.compositeDisposable = new CompositeDisposable();
        this.interactor = interactor;

        // Синхронизация БД валютных курсов с API
        this.interactor.setDataSyncPeriodicalTime();
    }

    public void bind(RatesView view) {
        this.view = view;
    }

    public void unbind() {
        this.compositeDisposable.dispose();
        this.view = null;
    }

    public RatesView getView() {
        return this.view;
    }

    // Получает лист с моделями валют и просит вью показать их
    public void loadCurrencyRates() {
        getView().showProgress();

        this.compositeDisposable.add(
                interactor.getAllCurrency()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                currencies -> getView().showCurrencyRates(currencies),
                                throwable -> getView().showErrorLoad()
                        )
        );
    }

    // Принимает от вью поисковые запросы. Получает валютные курсы на основании запроса, просит вью показать их
    public void loadCurrencyBySearch(Observable<String> searchQueryObservable) {
        compositeDisposable.add(
                searchQueryObservable
                        .flatMapSingle((Function<String, Single<List<Currency>>>) s -> interactor.getAllCurrencyBySearch(s))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                currencies -> getView().showCurrencyRates(currencies),
                                throwable -> getView().showErrorLoad()
                        )
        );
    }

    // Обработка клика по лейаута с ошибкой об отстутствие валютных курсов
    // Повторная попытка загрузить валютные курсы
    public void onClickErrorLoadDataMessage() {
        this.loadCurrencyRates();
    }

    // Обработка клика на закрытие поиска в тулбаре
    // Загрузить валютные курсы и отправить вьюхе
    public void onClickCloseSearch() {
        this.loadCurrencyRates();
    }
}
