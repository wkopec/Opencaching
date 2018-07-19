package com.example.opencaching.ui.geocache;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.opencaching.R;
import com.example.opencaching.network.models.okapi.Geocache;
import com.example.opencaching.ui.base.BaseActivity;
import com.example.opencaching.ui.base.SectionsPagerAdapter;
import com.example.opencaching.ui.geocache.info.GeocacheInfoFragment;
import com.example.opencaching.ui.geocache.logs.GeocacheLogsFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.opencaching.utils.ResourceUtils.getGeocacheIcon;

/**
 * Created by Wojtek on 26.07.2017.
 */

public class GeocacheActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, OnMapReadyCallback {

    public static final String GEOCACHE = "geocache";
    private static final float START_MAP_ZOOM = (float) 15;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static Geocache geocache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocache);
        ButterKnife.bind(this);
        geocache = getIntent().getExtras().getParcelable(GEOCACHE);
        configureTabLayout();
        setSupportActionBar(toolbar);
        setupActionBar();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
            actionBar.setTitle(geocache.getCode());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static String getGeocacheWaypoint() {
        return geocache.getCode();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng latLng = geocache.getPosition();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), START_MAP_ZOOM));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(getGeocacheIcon(geocache.getType())));
        googleMap.addMarker(markerOptions);

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
