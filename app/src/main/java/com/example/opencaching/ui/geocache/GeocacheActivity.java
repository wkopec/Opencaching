package com.example.opencaching.ui.geocache;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.opencaching.R;
import com.example.opencaching.ui.base.BaseActivity;
import com.example.opencaching.ui.base.SectionsPagerAdapter;
import com.example.opencaching.ui.geocache.geocache_info.GeocacheInfoFragment;
import com.example.opencaching.ui.geocache.geocache_logs.GeocacheLogsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wojtek on 26.07.2017.
 */

public class GeocacheActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    public static final String GEOCACHE_WAYPOINT = "geocache_waypoint";

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private static String geocacheWaypoint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ButterKnife.bind(this);
        geocacheWaypoint = getIntent().getExtras().getString(GEOCACHE_WAYPOINT);
        setupActionBar();
        configureTabLayout();
    }

    private void configureTabLayout() {
        ArrayList<SectionsPagerAdapter.Item> items = new ArrayList<>();
        items.add(new SectionsPagerAdapter.Item(getString(R.string.info), new GeocacheInfoFragment()));
        items.add(new SectionsPagerAdapter.Item(getString(R.string.logs), new GeocacheLogsFragment()));
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), items);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
            actionBar.setTitle(geocacheWaypoint);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static String getGeocacheWaypoint() {
        return geocacheWaypoint;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        hideKeyboard();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
