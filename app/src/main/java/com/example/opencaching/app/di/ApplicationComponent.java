package com.example.opencaching.app.di;

import com.example.opencaching.app.App;
import com.example.opencaching.app.di.modules.ApplicationModule;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@ApplicationScope
@Component(modules = {AndroidSupportInjectionModule.class,
        AndroidBindingModule.class,
        ApplicationModule.class,
        MVPModule.class})
public interface ApplicationComponent extends AndroidInjector<App>{

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App>{}
}
