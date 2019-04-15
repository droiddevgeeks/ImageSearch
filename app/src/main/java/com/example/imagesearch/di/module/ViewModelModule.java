package com.example.imagesearch.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.imagesearch.api.CustomViewModelFactory;
import com.example.imagesearch.di.scope.ViewModelKey;
import com.example.imagesearch.ui.viewmodel.SearchViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindSearchViewModel(SearchViewModel searchViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindCustomViewModelFactory(CustomViewModelFactory factory);
}
