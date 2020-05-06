package com.arturdevmob.currencyrates.presentation.converter.mvp;

public interface ConverterView {
    void showProgress();
    void showSnackBar(String message);
    void showErrorLoad();
    void showConverterLayout();

    void setFirstCurrencyCharCode(String charCode);
    void setFirstCurrencyFlagCountry(String imageUrl);
    void setSecondCurrencyCharCode(String charCode);
    void setSecondCurrencyFlagCountry(String imageUrl);
    void setSecondCurrencyValue(String value);
    String getFirstCurrencyCharCode();
    String getSecondCurrencyCharCode();
    String getFirstCurrencyValue();

    void showDialogChoiceFirstCurrency();
    void showDialogChoiceSecondCurrency();
}
