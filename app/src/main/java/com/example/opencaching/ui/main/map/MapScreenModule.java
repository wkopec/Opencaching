package com.example.opencaching.ui.main.map;

import dagger.Binds;
import dagger.Module;

@Module
public interface MapScreenModule {
    @Binds
    MapContract.View bindView(MapFragment mapFragment);
}
