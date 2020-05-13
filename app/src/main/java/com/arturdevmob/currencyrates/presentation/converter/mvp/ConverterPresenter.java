package com.arturdevmob.currencyrates.presentation.converter.mvp;

import com.arturdevmob.currencyrates.business.converter.ConverterInteractor;
import com.arturdevmob.currencyrates.business.core.models.Converter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ConverterPresenter {
    private CompositeDisposable compositeDisposable;
    private ConverterInteractor interactor;
    private ConverterView view;

    public ConverterPresenter(ConverterInteractor interactor) {
        this.compositeDisposable = new CompositeDisposable();
        this.interactor = interactor;
    }

    public void bind(ConverterView view) {
        this.view = view;
    }

    public void onbind() {
        this.compositeDisposable.dispose();
        this.view = null;
    }

    public ConverterView getView() {
        return view;
    }

    public void loadConverter(String firstCurrencyCharCode, String secondCurrencyCharCode) {
        this.loadConverter(firstCurrencyCharCode, secondCurrencyCharCode, "1");
    }

    // Принимает charCode двух валют и количество единиц для первой валюты
    public void loadConverter(String firstCurrencyCharCode, String secondCurrencyCharCode, String strCountFirstValue) {
        getView().showProgress();

        try {
            double countFirstValue = Double.parseDouble(strCountFirstValue);

            compositeDisposable.add(
                    interactor.startConverter(firstCurrencyCharCode, secondCurrencyCharCode, countFirstValue)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(converter -> {
                                updateConverterUi(converter);
                                getView().showConverterLayout();
                            }, throwable -> getView().showErrorLoad())
            );
        } catch (NumberFormatException ex) {
            getView().showSnackBar("Введено неверное количество первой валюты!");
        }
    }

    // Обновляет UI согласно полученной модели
    private void updateConverterUi(Converter converter) {
        // Устанавливает в UI для первой валюты charCode и иконку
        getView().setFirstCurrencyCharCode(converter.getFirstCurrency().getCharCode());
        getView().setFirstCurrencyFlagCountry(converter.getFirstCurrency().getImageUrl());

        // Устанавливает в UI для второй валюты charCode, иконку и количество единиц
        getView().setSecondCurrencyCharCode(converter.getSecondCurrency().getCharCode());
        getView().setSecondCurrencyFlagCountry(converter.getSecondCurrency().getImageUrl());
        getView().setSecondCurrencyValue(String.valueOf(converter.getRate()));
    }

    // При клике на первую валюту, показывает диалог установки новой валюты для конвертации
    public void onClickFirstCurrency() {
        getView().showDialogChoiceFirstCurrency();
    }

    // При клике на вторую валюту, показывает диалог установки новой валюты для конвертации
    public void onClickSecondCurrency() {
        getView().showDialogChoiceSecondCurrency();
    }

    // Меняет местами валюты
    public void onClickInvertCurrency() {
        this.loadConverter(
                getView().getSecondCurrencyCharCode(),
                getView().getFirstCurrencyCharCode(),
                getView().getFirstCurrencyValue()
        );
    }

    // Загружает первую валюту и её количество
    public void onChangedFirstCurrencyValue() {
        this.loadConverter(
                getView().getFirstCurrencyCharCode(),
                getView().getSecondCurrencyCharCode(),
                getView().getFirstCurrencyValue()
        );
    }

    // Загружает вторую валюту
    public void onClickErrorLoadDataLayout() {
        this.loadConverter(
                getView().getFirstCurrencyCharCode(),
                getView().getSecondCurrencyCharCode()
        );
    }
}
