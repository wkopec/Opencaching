package pl.opencaching.android.ui.geocache.new_log;

import dagger.Binds;
import dagger.Module;

@Module
public interface NewLogModule {

    @Binds
    NewLogContract.View bindView(NewLogFragment newLogFragment);
}
