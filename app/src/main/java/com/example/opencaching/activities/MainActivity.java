package com.example.opencaching.activities;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.example.opencaching.Menu;
import com.example.opencaching.R;
import com.example.opencaching.adapters.MenuAdapter;
import com.example.opencaching.fragments.map.MapFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MenuAdapter.OnMenuItemCheckedListener, DrawerLayout.DrawerListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.fragmentContainer)
    FrameLayout fragmentContainer;
    @BindView(R.id.menuRecyclerView)
    RecyclerView menuRecyclerView;

    public Fragment currentFragment;
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, 0, 0);

        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(this);
        toggle.syncState();
        configureMenuRecyclerView();

    }

    private void configureMenuRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        menuRecyclerView.setLayoutManager(manager);
        menuAdapter = new MenuAdapter(Menu.getItems(), this);
        menuRecyclerView.setAdapter(menuAdapter);
        menuAdapter.setItemSelected(R.string.nav_map);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!(currentFragment instanceof MapFragment)) {
            menuAdapter.setItemSelected(R.string.nav_map);
        } else {
            finish();
        }
    }

    @Override
    public void onMenuItemChecked(int tag) {
        if (Menu.isAction(tag)){
            Menu.performAction(this, tag);
        } else {
            Fragment newFragment = Menu.getFragment(tag);
            if (currentFragment == null || !newFragment.getClass().equals(currentFragment.getClass())) {
                replaceFragment(newFragment, false);
                currentFragment = newFragment;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

}
