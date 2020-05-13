package com.arturdevmob.currencyrates.presentation.converter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import com.arturdevmob.currencyrates.R;
import com.arturdevmob.currencyrates.business.core.models.Currency;
import com.arturdevmob.currencyrates.di.converter.DaggerChoiceCurrencyComponent;
import com.arturdevmob.currencyrates.presentation.SingleActivity;
import com.arturdevmob.currencyrates.presentation.converter.mvp.ChoiceCurrencyPresenter;
import com.arturdevmob.currencyrates.presentation.converter.mvp.ChoiceCurrencyView;
import java.util.List;
import javax.inject.Inject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChoiceCurrencyDialog extends DialogFragment implements ChoiceCurrencyView {
    public static final String TAG = "ChoiceCurrencyDialog";
    @Inject ChoiceCurrencyPresenter presenter;
    @Inject ChoiceCurrencyAdapter choiceCurrencyAdapter;
    private Unbinder unbinder;
    private OnSelectedCurrency onSelectedCurrencyCallback;
    @BindView(R.id.currency_recycler) RecyclerView currencyRecycler;
    @BindView(R.id.progress) ProgressBar progressBar;

    public static ChoiceCurrencyDialog newInstance() {
        return new ChoiceCurrencyDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_choice_currency, null);

        setupDi();

        unbinder = ButterKnife.bind(this, view);

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.chosen_currency)
                .setView(view)
                .setNeutralButton(R.string.cancel, null)
                .create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter.bind(this);

        // Реализация интерфейса, для принятия результата (код выбранной валюты) от адаптера
        choiceCurrencyAdapter.setOnSelectedCurrencyCallback(charCodeCurrency -> {
            onSelectedCurrencyCallback.onSelected(charCodeCurrency);
            dismiss();
        });

        currencyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        currencyRecycler.hasFixedSize();
        currencyRecycler.setAdapter(choiceCurrencyAdapter);

        presenter.loadCurrencyRates();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showProgress(boolean show) {
        if (show) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showListCurrency(List<Currency> currencyList) {
        choiceCurrencyAdapter.setCurrencyList(currencyList);
    }

    private void setupDi() {
        DaggerChoiceCurrencyComponent.builder()
                .appComponent(((SingleActivity) getActivity()).getAppComponent())
                .build()
                .inject(this);
    }

    public void setOnSelectedCurrencyCallback(OnSelectedCurrency callback) {
        this.onSelectedCurrencyCallback = callback;
    }

    // Интерфейс для передачи результата (Выбранной валюты) из DialogFragment в Fragment
    public interface OnSelectedCurrency {
        void onSelected(String charCodeCurrency);
    }
}
