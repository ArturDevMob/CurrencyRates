package com.arturdevmob.currencyrates.data.sources.local;

import android.content.SharedPreferences;

public class LocalStorage {
    private SharedPreferences preferences;

    public LocalStorage(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void putString(String key, String value) {
        preferences.edit()
                .putString(key, value)
                .apply();
    }

    public String getString(String key, String value) {
        return preferences.getString(key, value);
    }

    public boolean getBoolean(String key, boolean value) {
        return preferences.getBoolean(key, value);
    }

    public long getLong(String key, long value) {
        return preferences.getLong(key, value);
    }

    public void putLong(String key, long value) {
        preferences.edit()
                .putLong(key, value)
                .apply();
    }
}
