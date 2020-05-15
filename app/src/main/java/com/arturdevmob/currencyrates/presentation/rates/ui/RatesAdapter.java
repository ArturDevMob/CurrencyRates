package com.arturdevmob.currencyrates.presentation.rates.ui;

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

public class RatesAdapter extends RecyclerView.Adapter<RatesAdapter.ViewHolder> {
    private Context context;
    private List<Currency> currencyList;

    public RatesAdapter() {
        this.currencyList = new ArrayList<>();
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_rate, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Currency currency = currencyList.get(position);

        Glide.with(context)
                .load(currency.getImageUrl())
                .error(R.drawable.ic_no_flag_country)
                .placeholder(R.drawable.ic_no_flag_country)
                .into(holder.countryFlagImage);

        holder.nameText.setText(currency.getName());
        holder.charCodeText.setText(currency.getCharCode());
        holder.valueText.setText(String.valueOf(currency.getRate()));
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.country_flag_image) ImageView countryFlagImage;
        @BindView(R.id.name_text) TextView nameText;
        @BindView(R.id.first_char_code_text) TextView charCodeText;
        @BindView(R.id.value_text) TextView valueText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
