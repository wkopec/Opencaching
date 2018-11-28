package pl.opencaching.android.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import pl.opencaching.android.R;
import pl.opencaching.android.app.prefs.SessionManager;
import pl.opencaching.android.ui.authorization.LoginActivity;
import pl.opencaching.android.ui.base.BaseActivity;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.opencaching.android.ui.main.drafts.DraftsFragment;
import pl.opencaching.android.ui.main.map.MapFragment;
import pl.opencaching.android.utils.events.SearchMapEvent;

public class MainActivity extends BaseActivity implements MenuAdapter.OnMenuItemCheckedListener, DrawerLayout.DrawerListener {

    @Inject
    SessionManager sessionManager;

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
    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    public Fragment currentFragment;
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);

        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(this);
        toggle.syncState();
        configureMenuRecyclerView();
        setSearchView();
        setStatusBarColor(R.color.colorPrimaryDark);
    }

    public void setSearchMenuItem(MenuItem item) {
        if (item != null) {
            searchView.setMenuItem(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
        }

        return false;
    }

    private void configureMenuRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        menuRecyclerView.setLayoutManager(manager);
        menuAdapter = new MenuAdapter(Menu.getItems(), this);
        menuRecyclerView.setAdapter(menuAdapter);
        menuAdapter.setItemSelected(R.string.nav_map);
    }

    private void setSearchView() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                EventBus.getDefault().postSticky(new SearchMapEvent(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (currentFragment instanceof DraftsFragment) {
            if(((DraftsFragment) currentFragment).onBackPressedHandled()) {
                return;
            }
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else if (!(currentFragment instanceof MapFragment)) {
            menuAdapter.setItemSelected(R.string.nav_map);
        } else {
            finish();
        }
    }

    public void hideSearchView() {
        searchView.closeSearch();
    }

    @Override
    public void onMenuItemChecked(int tag) {
        if (Menu.isAction(tag)) {
            switch (tag) {
                case R.string.nav_logout:
                    sessionManager.clearLoggedUserData();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                    break;
            }
        } else {
            Fragment newFragment = Menu.getFragment(tag);
            if (currentFragment == null || !newFragment.getClass().equals(currentFragment.getClass())) {
                replaceFragment(newFragment, false);
                currentFragment = newFragment;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    private boolean isDrawerClosed = true;

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        if (isDrawerClosed) {
            setStatusBarColor(R.color.transparent);
            isDrawerClosed = false;
        }
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        isDrawerClosed = true;
        setStatusBarColor(R.color.colorPrimaryDark);
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    private void setStatusBarColor(@ColorRes int color) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(color));
            }
        } catch (Exception e) {
            //Crashlytics.logException(e);
        }
    }

}
