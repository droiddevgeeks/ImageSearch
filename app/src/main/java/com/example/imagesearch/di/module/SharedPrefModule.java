package com.example.imagesearch.di.module;

import android.content.Context;

import com.example.imagesearch.MyApplication;
import com.example.imagesearch.api.SharedPrefsHelper;
import com.example.imagesearch.di.scope.AppScope;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPrefModule {

    private final String MY_PREF = "FLICKERPREF";

    @AppScope
    @Provides
    SharedPrefsHelper provideSharedPrefHelper(MyApplication application){
        return  new SharedPrefsHelper(application.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE));
    }
}
