package com.arturdevmob.currencyrates.data.sources.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "currency")
public class CurrencyEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "char_code")
    private String charCode;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "rate")
    private double rate;

    public CurrencyEntity(long id, String charCode, String name, double rate) {
        this.id = id;
        this.charCode = charCode;
        this.name = name;
        this.rate = rate;
    }

    @Ignore
    public CurrencyEntity(String charCode, String name, double rate) {
        this.charCode = charCode;
        this.name = name;
        this.rate = rate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
