package com.example.imagesearch.di.component;

import com.example.imagesearch.MyApplication;
import com.example.imagesearch.di.module.ActivityBindingModule;
import com.example.imagesearch.di.module.ApiModule;
import com.example.imagesearch.di.module.ApplicationModule;
import com.example.imagesearch.di.module.RxModule;
import com.example.imagesearch.di.module.SharedPrefModule;
import com.example.imagesearch.di.scope.AppScope;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@AppScope
@Component(modules = {ApplicationModule.class,
        AndroidSupportInjectionModule.class,
        ActivityBindingModule.class,
        ApiModule.class,
        SharedPrefModule.class,
        RxModule.class})
public interface ApplicationComponent extends AndroidInjector<MyApplication> {

    void inject(MyApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(MyApplication application);
        ApplicationComponent build();
    }
}