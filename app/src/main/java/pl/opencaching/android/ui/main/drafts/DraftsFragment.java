package pl.opencaching.android.ui.main.drafts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import pl.opencaching.android.R;
import pl.opencaching.android.ui.base.BaseFragment;

public class DraftsFragment extends BaseFragment implements DraftsContract.View{

    @Inject
    DraftsContract.Presenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drafts, null);
        ButterKnife.bind(this, view);
        setupView();
        setPresenter(presenter);
        return view;
    }

    private void setupView() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.drafts_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
