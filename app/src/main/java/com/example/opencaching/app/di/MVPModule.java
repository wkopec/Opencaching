package com.example.opencaching.app.di;

import com.example.opencaching.ui.geocache.logs.GeocacheLogsContract;
import com.example.opencaching.ui.geocache.logs.GeocacheLogsPresenter;
import com.example.opencaching.ui.main.map.MapContract;
import com.example.opencaching.ui.main.map.MapFragmentPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public interface MVPModule {
    @Binds
    MapContract.Presenter bindMapPresenter(MapFragmentPresenter presenter);

    @Binds
    GeocacheLogsContract.Presenter bindGeocacheLogsPresenter(GeocacheLogsPresenter presenter);

}
