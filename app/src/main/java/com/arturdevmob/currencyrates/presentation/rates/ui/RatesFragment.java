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
import com.arturdevmob.currencyrates.presentation.rates.mvp.RatesPresenterImpl;
import com.arturdevmob.currencyrates.presentation.rates.mvp.RatesView;
import com.arturdevmob.currencyrates.presentation.rates.utils.RxSearch;
import com.google.android.material.snackbar.Snackbar;

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
import io.reactivex.Observable;

public class RatesFragment extends Fragment implements RatesView {
    public static final String TAG = "RatesFragment";

    @Inject
    RatesPresenterImpl presenter;
    @Inject
    RatesAdapter ratesAdapter;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.rates_recycler)
    RecyclerView ratesRecycler;
    @BindView(R.id.include_error_load_data)
    View errorLoadDataLayout;
    private Toolbar toolbar;
    private boolean isVisibleItemMenuUpdateRates;

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

        if (isVisibleItemMenuUpdateRates) menu.findItem(R.id.update_rates_action).setVisible(true);

        // Источник поискового запроса
        Observable<String> searchQueryObservable = RxSearch.from(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(s -> !s.isEmpty())
                .distinctUntilChanged();

        // При клике на поиск, передаем презентеру источник и тот подписывается на него для получения поисковых запросов
        searchView.setOnSearchClickListener(v -> presenter.loadCurrencyBySearch(searchQueryObservable));

        // Обработка клика на закрытие поиска
        searchView.setOnCloseListener(() -> {
            presenter.onClickCloseSearch();
            return false;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_rates_action:
                presenter.onClickUpdateCurrencyRates();
                break;
        }
        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unbind();
    }

    @Override
    public void showProgress(boolean isShow) {
        if (isShow) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorLoad(boolean isShow) {
        if (isShow) errorLoadDataLayout.setVisibility(View.VISIBLE);
        else errorLoadDataLayout.setVisibility(View.GONE);
    }

    @Override
    public void showCurrencyRates(boolean isShow) {
        if (isShow) ratesRecycler.setVisibility(View.VISIBLE);
        else ratesRecycler.setVisibility(View.GONE);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showIconUpdateRates() {
        // Т.к. этот метод вызывается раньше, чем onCreateOptionsMenu
        // нужно заранее установить видимость элемента для меню
        isVisibleItemMenuUpdateRates = true;
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
        toolbar = ((SingleActivity) getActivity()).getToolbar();
        ((SingleActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.app_name);
        setHasOptionsMenu(true);
    }

    private void showView(boolean isShow, View view) {

    }
}
