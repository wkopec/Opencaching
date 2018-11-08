package pl.opencaching.android.ui.geocache.new_log;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.opencaching.android.R;
import pl.opencaching.android.ui.base.BaseFragment;

public class NewLogFragment extends BaseFragment implements NewLogContract.View {

    private Unbinder unbinder;

    @Inject
    NewLogContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_log, null);
        unbinder = ButterKnife.bind(this, view);
        setPresenter(presenter);

        //presenter.start();
        return view;
    }
}
