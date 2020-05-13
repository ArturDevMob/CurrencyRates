package com.arturdevmob.currencyrates.presentation.converter.mvp;

import com.arturdevmob.currencyrates.business.converter.ChoiceCurrencyInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class ChoiceCurrencyPresenter {
    private CompositeDisposable compositeDisposable;
    private ChoiceCurrencyInteractor interactor;
    private ChoiceCurrencyView view;

    public ChoiceCurrencyPresenter(ChoiceCurrencyInteractor interactor) {
        this.compositeDisposable = new CompositeDisposable();
        this.interactor = interactor;
    }

    public ChoiceCurrencyView getView() {
        return view;
    }

    public void bind(ChoiceCurrencyView view) {
        this.view = view;
    }

    public void unbind() {
        this.compositeDisposable.dispose();
        this.view = null;
    }

    // Получает список всех валют и просит view показать их в диалоге
    public void loadCurrencyRates() {
        getView().showProgress(true);

        compositeDisposable.add(
                interactor.getAllCurrency()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(currencies -> {
                            getView().showProgress(false);
                            getView().showListCurrency(currencies);
                        }, throwable -> {})
        );
    }
}
