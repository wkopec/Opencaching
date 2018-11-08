package pl.opencaching.android.app.di;

import pl.opencaching.android.ui.authorization.LoginActivity;
import pl.opencaching.android.ui.authorization.login.LoginFragment;
import pl.opencaching.android.ui.authorization.login.LoginModule;
import pl.opencaching.android.ui.dialogs.MapFilterDialog;
import pl.opencaching.android.ui.dialogs.MapTypeDialog;
import pl.opencaching.android.ui.dialogs.MessageDialog;
import pl.opencaching.android.ui.gallery.GalleryActivity;
import pl.opencaching.android.ui.geocache.GeocacheActivity;
import pl.opencaching.android.ui.geocache.info.GeocacheInfoFragment;
import pl.opencaching.android.ui.geocache.info.GeocacheInfoModule;
import pl.opencaching.android.ui.geocache.logs.GeocacheLogsFragment;
import pl.opencaching.android.ui.geocache.logs.GeocacheLogsModule;
import pl.opencaching.android.ui.geocache.new_log.NewLogFragment;
import pl.opencaching.android.ui.geocache.new_log.NewLogModule;
import pl.opencaching.android.ui.main.MainActivity;
import pl.opencaching.android.ui.main.map.MapFragment;
import pl.opencaching.android.ui.main.map.MapScreenModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface AndroidBindingModule {

    //Fragments

    @ContributesAndroidInjector(modules = MapScreenModule.class)
    MapFragment mapFragment();

    @ContributesAndroidInjector(modules = GeocacheInfoModule.class)
    GeocacheInfoFragment geocacheInfoFragment();

    @ContributesAndroidInjector(modules = GeocacheLogsModule.class)
    GeocacheLogsFragment geocacheLogsFragment();

    @ContributesAndroidInjector(modules = NewLogModule.class)
    NewLogFragment newLogFragment();

    @ContributesAndroidInjector(modules = LoginModule.class)
    LoginFragment loginFragment();

    //Dialogs

    @ContributesAndroidInjector()
    MapFilterDialog mapFilterDialog();

    @ContributesAndroidInjector()
    MessageDialog messageDialog();

    @ContributesAndroidInjector()
    MapTypeDialog mapTypeDialog();

    //Activities

    @ContributesAndroidInjector()
    LoginActivity loginActivity();

    @ContributesAndroidInjector()
    MainActivity mainActivity();

    @ContributesAndroidInjector()
    GeocacheActivity geocacheActivity();

    @ContributesAndroidInjector()
    GalleryActivity galleryActivity();

}
