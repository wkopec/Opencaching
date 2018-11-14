package pl.opencaching.android.ui.geocache;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import butterknife.OnClick;
import pl.opencaching.android.R;

import pl.opencaching.android.app.prefs.SessionManager;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.ui.base.BaseActivity;
import pl.opencaching.android.ui.base.SectionsPagerAdapter;
import pl.opencaching.android.ui.dialogs.NewLogTypeDialog;
import pl.opencaching.android.ui.geocache.info.GeocacheInfoFragment;
import pl.opencaching.android.ui.geocache.logs.GeocacheLogsFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.opencaching.android.utils.GeocacheUtils;

/**
 * Created by Wojtek on 26.07.2017.
 */

public class GeocacheActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener,  OnMapReadyCallback {

    @Inject
    SessionManager sessionManager;

    public static final String GEOCACHE = "geocache";
    public static final String GEOCACHE_CODE = "geocache_code:";

    private static final float START_MAP_ZOOM = (float) 15;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.mapFrame)
    FrameLayout mapFrame;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.newLogButton)
    FloatingActionButton newLogButton;

    private static Geocache geocache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocache);
        ButterKnife.bind(this);
        geocache = getIntent().getExtras().getParcelable(GEOCACHE);
        configureTabLayout();
        newLogButton.hide();
        setSupportActionBar(toolbar);
        setupActionBar();
        setTranslucentStatusBar(getWindow());
        setupCollapsingToolbar();
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
        viewPager.addOnPageChangeListener(this);
    }

    private void setupCollapsingToolbar() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mapFrame.getLayoutParams();
        params.setMargins(0, 0, 0, getStatusBarHeight() * -1);
        mapFrame.setLayoutParams(params);
    }

    public static void setTranslucentStatusBar(Window window) {
        if (window == null) return;
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window);
        } else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatusBarKiKat(window);
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setTranslucentStatusBarLollipop(Window window) {
        window.setStatusBarColor(
                window.getContext()
                        .getResources()
                        .getColor(R.color.translucent));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setTranslucentStatusBarKiKat(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static String getGeocacheWaypoint() {
        return geocache.getCode();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.setMapType(sessionManager.getMapType());
        LatLng latLng = geocache.getPosition();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), START_MAP_ZOOM));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(GeocacheUtils.getGeocacheIcon(geocache.getType())));
        googleMap.addMarker(markerOptions);

    }

    @OnClick(R.id.newLogButton)
    public void onNewLogClick() {
        NewLogTypeDialog messageDialog = NewLogTypeDialog.newInstance(geocache.getCode(), NewLogTypeDialog.NEW_LOG);
        messageDialog.show(getSupportFragmentManager(), NewLogTypeDialog.class.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.geocache_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            finish();
//        }
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(position == 1) {
            newLogButton.show();
        } else {
            newLogButton.hide();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
