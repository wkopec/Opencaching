package com.example.opencaching.app.di;

import com.example.opencaching.ui.authorization.login.LoginFragment;
import com.example.opencaching.ui.dialogs.MapFilterDialog;
import com.example.opencaching.ui.geocache.info.GeocacheInfoFragment;
import com.example.opencaching.ui.geocache.logs.GeocacheLogsFragment;
import com.example.opencaching.ui.geocache.logs.GeocacheLogsModule;
import com.example.opencaching.ui.main.map.MapFragment;
import com.example.opencaching.ui.main.map.MapScreenModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface AndroidBindingModule {

    //Fragments

    @ContributesAndroidInjector(modules = MapScreenModule.class)
    MapFragment mapFragment();

    @ContributesAndroidInjector()
    GeocacheInfoFragment geocacheInfoFragment();

    @ContributesAndroidInjector(modules = GeocacheLogsModule.class)
    GeocacheLogsFragment geocacheLogsFragment();

    @ContributesAndroidInjector()
    LoginFragment loginFragment();

    //Dialogs

    @ContributesAndroidInjector()
    MapFilterDialog mapFilterDialog();

}
