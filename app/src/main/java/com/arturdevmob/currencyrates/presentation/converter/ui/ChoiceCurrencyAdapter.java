package com.arturdevmob.currencyrates.presentation.converter.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.arturdevmob.currencyrates.R;
import com.arturdevmob.currencyrates.business.core.models.Currency;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoiceCurrencyAdapter extends RecyclerView.Adapter<ChoiceCurrencyAdapter.ViewHolder> {
    private Context context;
    private OnSelectedCurrency onSelectedCurrencyCallback;
    private List<Currency> currencyList;

    public ChoiceCurrencyAdapter() {
        currencyList = new ArrayList<>();
    }

    public void setOnSelectedCurrencyCallback(OnSelectedCurrency callback) {
        this.onSelectedCurrencyCallback = callback;
    }

    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList.clear();
        this.currencyList.addAll(currencyList);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.item_choice_currency, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Currency currency = currencyList.get(position);

        Glide.with(context)
                .load(currency.getImageUrl())
                .error(R.drawable.ic_no_flag_country)
                .placeholder(R.drawable.ic_no_flag_country)
                .centerCrop()
                .into(holder.flagCountryImage);

        holder.charCodeText.setText(currency.getCharCode());
        holder.nameText.setText(currency.getName());
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_layout) View rootLayout;
        @BindView(R.id.flag_country_image) ImageView flagCountryImage;
        @BindView(R.id.char_code_text) TextView charCodeText;
        @BindView(R.id.name_text) TextView nameText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        // Клик по элементу из исписка валюты, через интерфейс возвращаем
        // в DialogFragment код валюты charcode
        @OnClick(R.id.root_layout)
        public void onClickRootLayout(View view) {
            String charCodeCurrency = currencyList.get(getLayoutPosition()).getCharCode();
            onSelectedCurrencyCallback.onSelected(charCodeCurrency);
        }
    }

    // Интерфейс для передачи результата (Выбранной валюты) из адаптера в DialogFragment
    public interface OnSelectedCurrency {
        void onSelected(String charCodeCurrency);
    }
}
