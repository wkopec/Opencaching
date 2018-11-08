package pl.opencaching.android.app.di;

import pl.opencaching.android.ui.authorization.login.LoginContract;
import pl.opencaching.android.ui.authorization.login.LoginFragmentPresenter;
import pl.opencaching.android.ui.geocache.info.GeocacheInfoContract;
import pl.opencaching.android.ui.geocache.info.GeocacheInfoPresenter;
import pl.opencaching.android.ui.geocache.logs.GeocacheLogsContract;
import pl.opencaching.android.ui.geocache.logs.GeocacheLogsPresenter;
import pl.opencaching.android.ui.geocache.new_log.NewLogContract;
import pl.opencaching.android.ui.geocache.new_log.NewLogPresenter;
import pl.opencaching.android.ui.main.map.MapContract;
import pl.opencaching.android.ui.main.map.MapPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public interface MVPModule {

    @Binds
    LoginContract.Presenter bindLoginPresenter(LoginFragmentPresenter presenter);

    @Binds
    MapContract.Presenter bindMapPresenter(MapPresenter presenter);

    @Binds
    GeocacheInfoContract.Presenter bindGeocacheInfoPresenter(GeocacheInfoPresenter presenter);

    @Binds
    GeocacheLogsContract.Presenter bindGeocacheLogsPresenter(GeocacheLogsPresenter presenter);

    @Binds
    NewLogContract.Presenter bindNewLogPresenter(NewLogPresenter presenter);

}
