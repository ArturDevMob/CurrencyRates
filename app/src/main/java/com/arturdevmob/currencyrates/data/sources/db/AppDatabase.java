package com.arturdevmob.currencyrates.data.sources.db;

import com.arturdevmob.currencyrates.data.sources.db.entity.CurrencyEntity;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CurrencyEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CurrencyDao currencyDao();
}