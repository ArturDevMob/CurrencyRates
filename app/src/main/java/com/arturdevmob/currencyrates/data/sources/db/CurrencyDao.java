package com.arturdevmob.currencyrates.data.sources.db;

import com.arturdevmob.currencyrates.data.sources.db.entity.CurrencyEntity;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Single;

@Dao
public interface CurrencyDao {
    @Query("SELECT * FROM currency")
    Single<List<CurrencyEntity>> getAllCurrency();

    @Query("SELECT * FROM currency WHERE char_code = :charCode")
    Single<List<CurrencyEntity>> getCurrency(String charCode);

    @Insert
    void insert(List<CurrencyEntity> currencyEntityList);

    @Query("DELETE FROM currency")
    void clearTable();
}