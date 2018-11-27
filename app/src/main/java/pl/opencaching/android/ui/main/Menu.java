package pl.opencaching.android.ui.main;

import android.support.v4.app.Fragment;

import pl.opencaching.android.R;

import java.util.ArrayList;
import java.util.List;

import pl.opencaching.android.ui.main.map.MapFragment;

/**
 * Created by Volfram on 15.07.2017.
 */

class Menu {

    static boolean isAction(int tag) {
        if (tag == R.string.nav_logout) {
            return true;
        } else {
            return false;
        }
    }

    static Fragment getFragment(int tag) {
        switch (tag) {
            case R.string.nav_map:
                return new MapFragment();

            default:
                return new Fragment();
        }
    }

    static List<MenuAdapter.MenuItem> getItems() {
        ArrayList<MenuAdapter.MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.HEADER, R.string.app_name, 0));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_news, R.drawable.ic_news));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_map, R.drawable.ic_map));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_drafts, R.drawable.ic_draft));
        //menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_search, R.drawable.ic_search));
        //menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_notifications, R.drawable.ic_notifications));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.SEPARATOR, 0, 0));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_user_neighbourhood, R.drawable.ic_neighborhood));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_user_stats, R.drawable.ic_statistics));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_user_geocaches, R.drawable.ic_geocache_box));
        //menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_user_profile, R.drawable.ic_profile));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_user_watched, R.drawable.ic_visibility));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.SEPARATOR, 0, 0));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_settings, R.drawable.ic_settings));
        menuItems.add(new MenuAdapter.MenuItem(MenuAdapter.ITEM, R.string.nav_logout, R.drawable.ic_logout_arrow));
        return menuItems;
    }


}
