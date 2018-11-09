package pl.opencaching.android.ui.geocache.new_log;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.ui.base.BaseActivity;
import pl.opencaching.android.ui.base.BaseFragment;

import static pl.opencaching.android.ui.geocache.GeocacheActivity.GEOCACHE_CODE;

public class NewLogFragment extends BaseFragment implements NewLogContract.View {

    public static final String NEW_LOG_TYPE = "new_log_type:";

    public static final int TYPE_FOUND = 1;
    public static final int TYPE_NOT_FOUND = 2;

    private Unbinder unbinder;

    @Inject
    NewLogContract.Presenter presenter;

    @BindView(R.id.geocacheName)
    TextView geocacheName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_log, null);
        unbinder = ButterKnife.bind(this, view);
        setupActionBar();
        setPresenter(presenter);
        presenter.start(getArguments().getString(GEOCACHE_CODE));
        return view;
    }

    private void setupActionBar() {
        BaseActivity activity = (BaseActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.new_log));
        }
    }

    @Override
    public void setupView(Geocache geocache) {
        geocacheName.setText(geocache.getName());
    }
}
