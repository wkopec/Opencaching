package pl.opencaching.android.ui.geocache.logs;

import dagger.Binds;
import dagger.Module;

@Module
public interface GeocacheLogsModule {

    @Binds
    GeocacheLogsContract.View bindView(GeocacheLogsFragment geocacheLogsFragment);
}
