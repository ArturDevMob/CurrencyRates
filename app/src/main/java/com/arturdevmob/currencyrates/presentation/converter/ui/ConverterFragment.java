package com.arturdevmob.currencyrates.presentation.converter.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.arturdevmob.currencyrates.R;
import com.arturdevmob.currencyrates.di.converter.DaggerConverterComponent;
import com.arturdevmob.currencyrates.presentation.SingleActivity;
import com.arturdevmob.currencyrates.presentation.converter.mvp.ConverterPresenter;
import com.arturdevmob.currencyrates.presentation.converter.mvp.ConverterView;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import javax.inject.Inject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConverterFragment extends Fragment implements ConverterView {
    public static final String TAG = "ConverterFragment";

    @Inject ConverterPresenter presenter;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.converter_layout) View converterLayout;
    @BindView(R.id.include_error_load_data) View errorLoadDataLayout;
    @BindView(R.id.first_flag_country_image) ImageView firstFlagCountryImage;
    @BindView(R.id.first_char_code_text) TextView firstCharCodeText;
    @BindView(R.id.first_value_edit) EditText firstValueEdit;
    @BindView(R.id.second_flag_country_image) ImageView secondFlagCountryImage;
    @BindView(R.id.second_char_code_text) TextView secondCharCodeText;
    @BindView(R.id.second_value_text) TextView secondValueText;
    @BindView(R.id.invert_image) ImageView invertImage;

    public static ConverterFragment newInstance() {
        return new ConverterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_converter, container, false);

        setupDi();

        ButterKnife.bind(this, view);

        setupToolbar();

        presenter.bind(this);

        presenter.loadConverter(
                firstCharCodeText.getText().toString(),
                secondCharCodeText.getText().toString()
        );

        // Уведомляет презентер об изменение введенного в EditText количества единиц первой валюты
        firstValueEdit.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                presenter.onChangedFirstCurrencyValue();
                return true;
            }

            return false;
        });

        return view;
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        converterLayout.setVisibility(View.GONE);
        errorLoadDataLayout.setVisibility(View.GONE);
    }

    @Override
    public void showConverterLayout() {
        progressBar.setVisibility(View.GONE);
        converterLayout.setVisibility(View.VISIBLE);
        errorLoadDataLayout.setVisibility(View.GONE);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorLoad() {
        progressBar.setVisibility(View.GONE);
        converterLayout.setVisibility(View.GONE);
        errorLoadDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setFirstCurrencyCharCode(String charCode) {
        firstCharCodeText.setText(charCode);
    }

    @Override
    public void setFirstCurrencyFlagCountry(String imageUrl) {
        Glide.with(getContext())
                .load(imageUrl)
                .error(R.drawable.ic_no_flag_country)
                .placeholder(R.drawable.ic_no_flag_country)
                .sizeMultiplier(0.5f)
                .into(firstFlagCountryImage);
    }

    @Override
    public void setSecondCurrencyCharCode(String charCode) {
        secondCharCodeText.setText(charCode);
    }

    @Override
    public void setSecondCurrencyFlagCountry(String imageUrl) {
        Glide.with(getContext())
                .load(imageUrl)
                .error(R.drawable.ic_no_flag_country)
                .placeholder(R.drawable.ic_no_flag_country)
                .sizeMultiplier(0.5f)
                .into(secondFlagCountryImage);
    }

    @Override
    public void setSecondCurrencyValue(String value) {
            secondValueText.setText(value);
    }

    @OnClick({R.id.first_flag_country_image, R.id.first_char_code_text})
    public void onClickFirstFlagCountryImage(View view) {
        presenter.onClickFirstCurrency();
    }

    @OnClick({R.id.second_flag_country_image, R.id.second_char_code_text})
    public void onClickSecondFlagCountryImage(View view) {
        presenter.onClickSecondCurrency();
    }

    @OnClick(R.id.invert_image)
    public void onClickInvertImage(View view) {
        presenter.onClickInvertCurrency();
    }

    @OnClick(R.id.include_error_load_data)
    public void onClickErrorLoadDataLayout(View view) {
        presenter.onClickErrorLoadDataLayout();
    }

    @Override
    public String getFirstCurrencyCharCode() {
        return firstCharCodeText.getText().toString();
    }

    @Override
    public String getSecondCurrencyCharCode() {
        return secondCharCodeText.getText().toString();
    }

    @Override
    public String getFirstCurrencyValue() {
        return firstValueEdit.getText().toString();
    }

    @Override
    public void showDialogChoiceFirstCurrency() {
        this.showDialogChoiceCurrency(charCodeCurrency ->
            presenter.loadConverter(
                    charCodeCurrency,
                    secondCharCodeText.getText().toString(),
                    getFirstCurrencyValue()
            )
        );
    }

    @Override
    public void showDialogChoiceSecondCurrency() {
        this.showDialogChoiceCurrency(charCodeCurrency -> {
            presenter.loadConverter(
                    firstCharCodeText.getText().toString(),
                    charCodeCurrency
            );
        });
    }

    private void showDialogChoiceCurrency(ChoiceCurrencyDialog.OnSelectedCurrency callback) {
        ChoiceCurrencyDialog dialog = ChoiceCurrencyDialog.newInstance();
        dialog.show(getFragmentManager(), ChoiceCurrencyDialog.TAG);
        // Реализация интерфейса, для принятия результата (код выбранной валюты) от DialogFragment
        dialog.setOnSelectedCurrencyCallback(callback);
    }

    private void setupDi() {
        DaggerConverterComponent.builder()
                .appComponent(((SingleActivity) getActivity()).getAppComponent())
                .build()
                .inject(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = ((SingleActivity) getActivity()).getToolbar();
        ((SingleActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.converter);
    }
}
