package com.arturdevmob.currencyrates.presentation.rates.utils;

import androidx.appcompat.widget.SearchView;
import io.reactivex.Observable;

public class RxSearch {
    public static Observable<String> from(SearchView searchView) {
        return Observable.create(emitter -> searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                emitter.onNext(newText);
                return false;
            }
        }));
    }
}
