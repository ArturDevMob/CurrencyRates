package com.arturdevmob.currencyrates.business.converter;

import com.arturdevmob.currencyrates.business.core.models.Converter;
import com.arturdevmob.currencyrates.business.core.models.Currency;
import com.arturdevmob.currencyrates.business.core.repositories.CurrencyRepository;
import com.arturdevmob.currencyrates.data.UtilsData;
import io.reactivex.Single;

public class ConverterInteractor {
    private CurrencyRepository currencyRepository;

    public ConverterInteractor(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    // Принимает charCode первой, второй валюты и количество единиц первой валюты
    // Получает две модели валют по их charCode'am и считает кросс-курс между двумя выбранными валютами
    // Создает модель. Если в метод передали RUB, USD, 1000, (т.е. метод считает, сколько за 1000 RUB мы получим USD по текущему курсу)
    // то на выходе получается Converter модель с валютой RUB, валютой USD, и количеством USD за 1000 RUB
    public Single<Converter> startConverter(String firstCurrencyCharCode, String secondCurrencyCharCode, double countFirstCurrencyValue) {
        Single<Currency> firstCurrencySingle = currencyRepository.getCurrencyByCharCode(firstCurrencyCharCode);
        Single<Currency> secondCurrencySingle = currencyRepository.getCurrencyByCharCode(secondCurrencyCharCode);

        return Single.zip(firstCurrencySingle, secondCurrencySingle, (firstCurrency, secondCurrency) -> {
            double crossRate = firstCurrency.getRate() / secondCurrency.getRate();

            return new Converter(
                    firstCurrency,
                    secondCurrency,
                    UtilsData.formatRateCurrency(countFirstCurrencyValue * crossRate)
            );
        });
    }
}
