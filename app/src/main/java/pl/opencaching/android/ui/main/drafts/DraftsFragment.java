package pl.opencaching.android.ui.main.drafts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraw;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.data.repository.LogDrawRepository;
import pl.opencaching.android.ui.base.BaseFragment;

public class DraftsFragment extends BaseFragment implements DraftsContract.View {

    @Inject
    GeocacheRepository geocacheRepository;
    @Inject
    DraftsContract.Presenter presenter;

    @BindView(R.id.draftsRecycleView)
    RecyclerView draftsRecycleView;

    private DraftsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drafts, null);
        ButterKnife.bind(this, view);
        setupView();
        setPresenter(presenter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onStart();
    }

    private void setupView() {
        setHasOptionsMenu(true);
        setupActionBar();
        configureRecyclerView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.drafts_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
            actionBar.setTitle(getString(R.string.nav_drafts));
        }
    }

    private void configureRecyclerView() {
        adapter = new DraftsAdapter(requireActivity(), geocacheRepository);
        draftsRecycleView.setAdapter(adapter);
        draftsRecycleView.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }

    @Override
    public void setGeocacheDraws(List<GeocacheLogDraw> geocacheLogDraws) {
        adapter.setGeocacheLogDrawList(geocacheLogDraws);
        adapter.notifyDataSetChanged();
    }
}
