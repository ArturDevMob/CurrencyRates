package com.arturdevmob.currencyrates.presentation.rates.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.arturdevmob.currencyrates.R;
import com.arturdevmob.currencyrates.di.rates.DaggerRatesComponent;
import com.arturdevmob.currencyrates.di.rates.RatesModule;
import com.arturdevmob.currencyrates.presentation.SingleActivity;
import com.arturdevmob.currencyrates.business.core.models.Currency;
import com.arturdevmob.currencyrates.presentation.rates.mvp.RatesPresenter;
import com.arturdevmob.currencyrates.presentation.rates.mvp.RatesView;
import com.arturdevmob.currencyrates.presentation.rates.utils.RxSearch;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RatesFragment extends Fragment implements RatesView {
    public static final String TAG = "RatesFragment";

    @Inject
    RatesPresenter presenter;
    @Inject
    RatesAdapter ratesAdapter;
    private CompositeDisposable compositeDisposable;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.rates_recycler) RecyclerView ratesRecycler;
    @BindView(R.id.include_error_load_data) View errorLoadDataLayout;

    public static RatesFragment newInstance() {
        return new RatesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_rate, container, false);

        setupDi();

        ButterKnife.bind(this, view);

        setupToolbar();

        compositeDisposable = new CompositeDisposable();

        presenter.bind(this);

        ratesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ratesRecycler.setHasFixedSize(true);
        ratesRecycler.setAdapter(ratesAdapter);

        presenter.loadCurrencyRates();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_search, menu);

        MenuItem menuItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) menuItem.getActionView();

        // Поиск валют
        // Неправильная резализация! Переписать!
        compositeDisposable.add(
                RxSearch.from(searchView)
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .filter(s -> !s.isEmpty())
                        .distinctUntilChanged()
                        .switchMapSingle(s -> presenter.loadCurrencyBySearch(s))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(currencies -> {
                            showCurrencyRates(currencies);
                        })
        );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unbind();
        compositeDisposable.dispose();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        errorLoadDataLayout.setVisibility(View.GONE);
        ratesRecycler.setVisibility(View.GONE);
    }

    @Override
    public void showErrorLoad() {
        progressBar.setVisibility(View.GONE);
        errorLoadDataLayout.setVisibility(View.VISIBLE);
        ratesRecycler.setVisibility(View.GONE);
    }

    @Override
    public void showCurrencyRates(List<Currency> currencyList) {
        progressBar.setVisibility(View.GONE);
        errorLoadDataLayout.setVisibility(View.GONE);
        ratesRecycler.setVisibility(View.VISIBLE);

        ratesAdapter.setCurrencyList(currencyList);
    }

    @OnClick(R.id.include_error_load_data)
    protected void onClickErrorLoadDataLayout(View view) {
        presenter.onClickErrorLoadDataMessage();
    }

    private void setupDi() {
        DaggerRatesComponent.builder()
                .appComponent(((SingleActivity) getActivity()).getAppComponent())
                .ratesModule(new RatesModule())
                .build()
                .inject(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = ((SingleActivity) getActivity()).getToolbar();
        ((SingleActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.rates);
        setHasOptionsMenu(true);
    }
}
