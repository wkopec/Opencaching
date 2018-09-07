package com.example.opencaching.ui.main;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.opencaching.R;
import com.example.opencaching.app.prefs.SessionManager;
import com.example.opencaching.ui.authorization.LoginActivity;
import com.example.opencaching.ui.main.map.MapFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by Volfram on 15.07.2017.
 */

public class Menu {


    public static boolean isAction(int tag){
        if (tag == R.string.nav_logout){
            return true;
        } else {
            return false;
        }
    }

    public static Fragment getFragment(int tag){
        switch (tag){
            case R.string.nav_map:
                return new MapFragment();

            default:
                return new Fragment();
        }
    }

    public static List<MenuAdapter.MenuItem> getItems (){
        ArrayList<MenuAdapter.MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.HEADER, R.string.app_name, 0));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_news, R.drawable.ic_news));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_map, R.drawable.ic_map_24dp));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_search, R.drawable.ic_search_24dp));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_notifications, R.drawable.ic_notifications_24dp));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.SEPARATOR, 0, 0));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_user_neighbourhood, R.drawable.ic_notifications_24dp));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_user_stats, R.drawable.ic_statistics));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_user_geocaches, R.drawable.ic_notifications_24dp));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_user_profile, R.drawable.ic_profile));
        //menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_user_watched, R.drawable.ic_visibility));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.SEPARATOR, 0, 0));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_settings, R.drawable.ic_settings_24dp));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_logout, R.drawable.ic_logout_arrow_24dp));
        return menuItems;
    }

    public static void performAction(MainActivity mainActivity, int tag) {
        switch (tag){
            case R.string.nav_logout:
                //TODO: use SessionManager
                //setOauthTokenSecret(mainActivity, "");
                //sessionManager.saveOauthTokenSecret("");
                mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
                mainActivity.finish();
                break;
        }
    }

}
