package com.example.imagesearch.api;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class SharedPrefsHelper {

    private SharedPreferences preferences;

    @Inject
    public SharedPrefsHelper(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    private final String QUERY_KEY = "queryKey";

    public void setQuery(String query){
        preferences.edit().putString(QUERY_KEY,query).apply();
    }

    public String getQuery(){
        return  preferences.getString(QUERY_KEY, "");
    }
}
