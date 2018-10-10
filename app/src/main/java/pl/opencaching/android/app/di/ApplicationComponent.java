package pl.opencaching.android.app.di;

import pl.opencaching.android.app.App;
import pl.opencaching.android.app.ApplicationModule;
import pl.opencaching.android.api.NetworkModule;

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
