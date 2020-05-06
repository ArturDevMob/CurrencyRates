package com.arturdevmob.currencyrates.data.sources.network;

import com.arturdevmob.currencyrates.data.sources.network.response.CurrencyListResponse;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

// CbrRateApi - api центрального банка России
public interface CbrRateApi {
    @GET("XML_daily.asp")
    Flowable<CurrencyListResponse> getListCurrencyRateByDateFlo(@Query("date_req") String date);
}
