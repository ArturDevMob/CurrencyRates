package com.arturdevmob.currencyrates.presentation.rates.mvp;

import com.arturdevmob.currencyrates.business.core.models.Currency;
import com.arturdevmob.currencyrates.business.rates.RatesInteractor;
import java.util.List;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

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

    // Получает от интерактора лист с моделями валют
    // если валютные курсы неудалось получить, выводит лейаут с ошибкой
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

    // Поиск валют. Неправильная резализация! Переписать!
    public Single<List<Currency>> loadCurrencyBySearch(String query) {
        return interactor.getAllCurrencyBySearch(query);
    }

    // Обработка клика по лейаута с ошибкой об отстутствие валютных курсов
    // Повторная попытка загрузить валютные курсы
    public void onClickErrorLoadDataMessage() {
        this.loadCurrencyRates();
    }
}
