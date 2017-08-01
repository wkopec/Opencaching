package com.example.opencaching.fragments.geocache_logs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opencaching.R;
import com.example.opencaching.activities.BaseActivity;
import com.example.opencaching.models.okapi.Log;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Volfram on 27.07.2017.
 */

public class GeocacheLogsFragment extends Fragment implements GeocacheLogsFragmentView{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private View view;
    private LogsListAdapter adapter;
    private BaseActivity activity;
    private GeocacheLogsFragmentPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_geocache_logs, null);
        unbinder = ButterKnife.bind(this, view);
        activity = (BaseActivity) getActivity();
        presenter = new GeocacheLogsFragmentPresenter(this, activity);
        presenter.getGeocacheLogs("OP8MDT");
        return view;
    }

    @Override
    public void setLogs(ArrayList<Log> logs) {
        configureRecyclerView(logs);
    }

    private void configureRecyclerView(ArrayList<Log> logs) {
        adapter = new LogsListAdapter(logs, activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
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
