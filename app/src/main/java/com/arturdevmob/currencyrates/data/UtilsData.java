package com.arturdevmob.currencyrates.data;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UtilsData {
    private static double parseDoubleFromString(String rate) {
        return Double.parseDouble(rate.replace(",", "."));
    }

    // Из строки формата 123,45678 создает double формата 12.34
    public static double parseAndFormatRateCurrency(String rateStr) {
        return formatRateCurrency(parseDoubleFromString(rateStr));
    }

    public static double formatRateCurrency(double rate) {
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        String resultStr = decimalFormat.format(rate);

        return parseDoubleFromString(resultStr);
    }

    // Из long миллисекунд в String 01.02.2020
    public static String dateFormatCurrency(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
        return simpleDateFormat.format(new Date(timeMillis));
    }
}
