package com.example.opencaching.ui.geocache.logs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opencaching.R;
import com.example.opencaching.ui.base.BaseActivity;
import com.example.opencaching.data.models.okapi.GeocacheLog;
import com.example.opencaching.ui.base.BaseFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.opencaching.ui.geocache.GeocacheActivity.getGeocacheWaypoint;

/**
 * Created by Volfram on 27.07.2017.
 */

public class GeocacheLogsFragment extends BaseFragment implements GeocacheLogsContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private View view;
    private LogListAdapter adapter;
    private BaseActivity activity;

    @Inject
    GeocacheLogsContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_geocache_logs, null);
        unbinder = ButterKnife.bind(this, view);
        activity = (BaseActivity) getActivity();
        setPresenter(presenter);
        presenter.getGeocacheLogs(getGeocacheWaypoint());
        return view;
    }

    @Override
    public void setLogs(ArrayList<GeocacheLog> geocacheLogs) {
        configureRecyclerView(geocacheLogs);
    }

    private void configureRecyclerView(ArrayList<GeocacheLog> geocacheLogs) {
        adapter = new LogListAdapter(geocacheLogs, activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    public void showError(Error error) {
        activity.showToast(error.getMessage());
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
