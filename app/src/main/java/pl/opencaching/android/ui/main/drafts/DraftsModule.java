package pl.opencaching.android.ui.main.drafts;

import dagger.Binds;
import dagger.Module;

@Module
public interface DraftsModule {
    @Binds
    DraftsContract.View bindView(DraftsFragment mapFragment);
}
