package pl.opencaching.android.ui.geocache.info;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Volfram on 08.09.2018.
 */

@Module
public interface GeocacheInfoModule {

    @Binds
    GeocacheInfoContract.View bindView(GeocacheInfoFragment geocacheInfoFragment);

}
