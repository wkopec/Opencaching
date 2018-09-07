package com.example.opencaching.app.di;

import com.example.opencaching.ui.authorization.login.LoginContract;
import com.example.opencaching.ui.authorization.login.LoginFragmentPresenter;
import com.example.opencaching.ui.geocache.logs.GeocacheLogsContract;
import com.example.opencaching.ui.geocache.logs.GeocacheLogsPresenter;
import com.example.opencaching.ui.main.map.MapContract;
import com.example.opencaching.ui.main.map.MapPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public interface MVPModule {
    @Binds
    MapContract.Presenter bindMapPresenter(MapPresenter presenter);

    @Binds
    GeocacheLogsContract.Presenter bindGeocacheLogsPresenter(GeocacheLogsPresenter presenter);

    @Binds
    LoginContract.Presenter bindLoginPresenter(LoginFragmentPresenter presenter);

}
