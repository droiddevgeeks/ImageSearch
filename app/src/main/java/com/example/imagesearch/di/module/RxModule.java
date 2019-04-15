package com.example.imagesearch.di.module;

import com.example.imagesearch.api.RxSingleSchedulers;
import com.example.imagesearch.di.scope.AppScope;

import dagger.Module;
import dagger.Provides;

@Module
public class RxModule {
    @AppScope
    @Provides
    public RxSingleSchedulers providesScheduler() {
        return RxSingleSchedulers.DEFAULT;
    }
}
