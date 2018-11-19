package pl.opencaching.android.ui.geocache.logs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.opencaching.android.R;
import pl.opencaching.android.ui.base.BaseActivity;
import pl.opencaching.android.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static pl.opencaching.android.ui.geocache.GeocacheActivity.getGeocacheWaypoint;

/**
 * Created by Volfram on 27.07.2017.
 */

public class GeocacheLogsFragment extends BaseFragment implements GeocacheLogsContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private LogListAdapter adapter;
    private BaseActivity activity;

    @Inject
    GeocacheLogsContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geocache_logs, null);
        unbinder = ButterKnife.bind(this, view);
        activity = (BaseActivity) requireActivity();
        setPresenter(presenter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getGeocacheLogs(getGeocacheWaypoint());
    }

    @Override
    public void setLogs(ArrayList<GeocacheLogInterface> geocacheLogs) {
        Collections.sort(geocacheLogs);
        configureRecyclerView(geocacheLogs);
    }

    private void configureRecyclerView(ArrayList<GeocacheLogInterface> geocacheLogs) {
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
