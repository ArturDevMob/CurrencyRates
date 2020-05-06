package com.arturdevmob.currencyrates.data.sources.db.entity;

import com.arturdevmob.currencyrates.business.core.models.Currency;

public class CurrencyEntityMapper {
    public static final String PATH_CURRENCY_ICON = "file:///android_asset/countryflag/%s.png";

    public static Currency map(CurrencyEntity currencyEntity) {
        return new Currency(
                currencyEntity.getCharCode(),
                currencyEntity.getName(),
                currencyEntity.getRate(),
                String.format(PATH_CURRENCY_ICON, currencyEntity.getCharCode())
        );
    }
}
