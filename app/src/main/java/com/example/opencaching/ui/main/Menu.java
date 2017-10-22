package com.example.opencaching.ui.main;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.opencaching.R;
import com.example.opencaching.ui.auth.LoginActivity;
import com.example.opencaching.ui.main.map.MapFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.opencaching.utils.UserUtils.setUserToken;

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
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.GROUP, R.string.main_menu, 0));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_map, R.drawable.ic_map_24dp));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_search, R.drawable.ic_search_24dp));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_notifications, R.drawable.ic_notifications_24dp));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.GROUP, R.string.user_menu, 0));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_user_stats, R.drawable.ic_notifications_24dp));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_user_geocaches, R.drawable.ic_notifications_24dp));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_user_neighbourhood, R.drawable.ic_notifications_24dp));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.GROUP, R.string.options, 0));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_settings, R.drawable.ic_settings_24dp));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_logout, R.drawable.ic_logout_arrow_24dp));
        return menuItems;
    }

    public static void performAction(MainActivity mainActivity, int tag) {
        switch (tag){
            case R.string.nav_logout:
                setUserToken(mainActivity, "");
                mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
                mainActivity.finish();
                break;
        }
    }

}
