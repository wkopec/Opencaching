package com.example.opencaching.app.di;

import com.example.opencaching.app.App;
import com.example.opencaching.app.ApplicationModule;
import com.example.opencaching.api.NetworkModule;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@ApplicationScope
@Component(modules = {AndroidSupportInjectionModule.class,
        AndroidBindingModule.class,
        ApplicationModule.class,
        NetworkModule.class,
        MVPModule.class})
public interface ApplicationComponent extends AndroidInjector<App>{

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App>{}
}