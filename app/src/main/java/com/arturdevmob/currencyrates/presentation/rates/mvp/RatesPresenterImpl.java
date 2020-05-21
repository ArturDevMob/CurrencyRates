package com.arturdevmob.currencyrates.presentation.rates.mvp;

import com.arturdevmob.currencyrates.business.core.models.Currency;
import com.arturdevmob.currencyrates.business.core.presenters.RatesPresenter;
import com.arturdevmob.currencyrates.business.rates.RatesInteractor;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

public class RatesPresenterImpl implements RatesPresenter {
    private CompositeDisposable compositeDisposable;
    private RatesInteractor interactor;
    private RatesView view;

    public RatesPresenterImpl(RatesInteractor interactor) {
        this.compositeDisposable = new CompositeDisposable();
        this.interactor = interactor;
    }

    public void bind(RatesView view) {
        this.view = view;
        this.interactor.setPresenter(this);
    }

    public void unbind() {
        this.compositeDisposable.dispose();
        this.view = null;
    }

    public RatesView getView() {
        return this.view;
    }

    @Override
    public void notifyAboutOutdatedRates() {
        getView().showSnackBar("Текущие валютные курсы устарели!");
    }

    @Override
    public void allowUpdateManuallyRates() {
        getView().showIconUpdateRates();
    }

    // Получает лист с моделями валют и просит вью показать их
    public void loadCurrencyRates() {
        getView().showProgress(true);
        getView().showCurrencyRates(false);
        getView().showErrorLoad(false);

        this.compositeDisposable.add(
                interactor.getAllCurrency()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                currencies -> {
                                    getView().showProgress(false);
                                    getView().showCurrencyRates(true);
                                    getView().showCurrencyRates(currencies);
                                },
                                throwable -> {
                                    getView().showProgress(false);
                                    getView().showCurrencyRates(false);
                                    getView().showErrorLoad(true);
                                }
                        )
        );
    }

    // Принимает от вью поисковые запросы. Получает валютные курсы на основании запроса, просит вью показать их
    public void loadCurrencyBySearch(Observable<String> searchQueryObservable) {
        getView().showProgress(true);

        compositeDisposable.add(
                searchQueryObservable
                        .flatMapSingle((Function<String, Single<List<Currency>>>) s -> interactor.getAllCurrencyBySearch(s))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                currencies -> {
                                    getView().showProgress(false);
                                    getView().showCurrencyRates(true);
                                    getView().showCurrencyRates(currencies);
                                },
                                throwable -> {
                                    getView().showProgress(false);
                                    getView().showCurrencyRates(false);
                                    getView().showErrorLoad(true);
                                }
                        )
        );
    }

    // Обработка клика по лейаута с ошибкой об отстутствие валютных курсов
    // Повторная попытка загрузить валютные курсы
    public void onClickErrorLoadDataMessage() {
        this.loadCurrencyRates();
    }

    // Обработка клика на "закрыть поиск" в тулбаре
    // Загрузить валютные курсы и отправить вьюхе
    public void onClickCloseSearch() {
        this.loadCurrencyRates();
    }

    // Обработка клика на "обновить курсы" в тулбаре
    public void onClickUpdateCurrencyRates() {
        this.getNewCurrencyRates();
    }

    // Получить по API актуальные валютные курсы
    // Если получены новые валютные курсы, то отобразить их
    // Иначе просто вывести уведомление об ошибке, т.к. ранее уже были загружены старые курсы
    private void getNewCurrencyRates() {
        getView().showProgress(true);

        compositeDisposable.add(
                interactor.getNewCurrencyRates()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                currencies -> {
                                    getView().showProgress(false);
                                    getView().showCurrencyRates(true);
                                    getView().showCurrencyRates(currencies);
                                    getView().showSnackBar("Валютные курсы обновлены!");
                                },
                                throwable -> {
                                    getView().showProgress(false);
                                    getView().showCurrencyRates(true);
                                    getView().showSnackBar("Произошла ошибка при обновлении!");
                                }
                        )
        );
    }
}
