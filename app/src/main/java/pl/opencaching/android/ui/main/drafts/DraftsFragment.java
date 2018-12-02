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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraft;
import pl.opencaching.android.data.repository.GeocacheRepository;
import pl.opencaching.android.ui.base.BaseFragment;
import pl.opencaching.android.ui.main.MainActivity;

public class DraftsFragment extends BaseFragment implements DraftsContract.View, DraftsAdapretEventListener {

    @Inject
    GeocacheRepository geocacheRepository;
    @Inject
    Realm realm;
    @Inject
    DraftsContract.Presenter presenter;

    @BindView(R.id.draftsRecycleView)
    RecyclerView draftsRecycleView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private DraftsAdapter adapter;
    private Menu menu;
    private boolean isMultipleChoiceMode;

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

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void setupView() {
        setHasOptionsMenu(true);
        setupActionBar();
        configureRecyclerView();
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
            actionBar.setTitle(getString(R.string.nav_drafts));
        }
    }

    private void configureRecyclerView() {
        adapter = new DraftsAdapter(requireActivity(), geocacheRepository, realm, this);
        draftsRecycleView.setAdapter(adapter);
        draftsRecycleView.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        requireActivity().getMenuInflater().inflate(R.menu.drafts_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_multiple_choice:
                setMultipleChoiceMode(true);
                return true;
//            case R.id.action_set_rate:
//
//                return true;
//            case R.id.action_set_comment:
//
//                return true;
            case R.id.action_post:
                presenter.postDrafts(adapter.getSelectedGeocacheDraws());
                return true;
            default:
                return false;
        }
    }

    @Override
    public void setMultipleChoiceMode(boolean isMultipleChoiceMode) {
        this.isMultipleChoiceMode = isMultipleChoiceMode;
        adapter.setMultipleChoiceMode(isMultipleChoiceMode);
        if (!isMultipleChoiceMode) {
            adapter.setAllItemsChecked(false);
        }
        menu.findItem(R.id.action_multiple_choice).setVisible(!isMultipleChoiceMode);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            if (requireActivity() instanceof MainActivity) {
                ((MainActivity) requireActivity()).displayHomeAsUpEnabled(isMultipleChoiceMode);
            }
        }
    }

    private void showMultipleChoiceMenu(boolean isMultipleChoiceMenuVisible) {
        menu.findItem(R.id.action_post).setVisible(isMultipleChoiceMenuVisible);
//        menu.findItem(R.id.action_set_rate).setVisible(isMultipleChoiceMenuVisible);
//        menu.findItem(R.id.action_set_comment).setVisible(isMultipleChoiceMenuVisible);
    }

    public boolean onBackPressedHandled() {
        if (isMultipleChoiceMode) {
            setMultipleChoiceMode(false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setGeocacheDraws(List<GeocacheLogDraft> geocacheLogDrafts) {
        adapter.setGeocacheLogDraftList(geocacheLogDrafts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateData() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress(boolean isShown) {
        progressBar.setVisibility(isShown ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onDraftSelectionChange() {
        if (adapter.getSelectedGeocacheDraws().isEmpty()) {
            showMultipleChoiceMenu(false);
        } else {
            showMultipleChoiceMenu(true);
        }
    }

    @Override
    public void onLongItemClick() {
        if (!isMultipleChoiceMode) {
            setMultipleChoiceMode(true);
        }
    }

    @Override
    public void onPostDraft(GeocacheLogDraft logDraft) {
        presenter.postDraft(logDraft);
    }
}
