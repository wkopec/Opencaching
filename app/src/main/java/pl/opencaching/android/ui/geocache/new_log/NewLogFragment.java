package pl.opencaching.android.ui.geocache.new_log;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hsalf.smilerating.BaseRating;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.ui.base.BaseActivity;
import pl.opencaching.android.ui.base.BaseFragment;
import pl.opencaching.android.utils.GeocacheUtils;
import pl.opencaching.android.utils.views.CustomSmileRating;

import static pl.opencaching.android.ui.dialogs.NewLogTypeDialog.GEOCACHE_CODE;
import static pl.opencaching.android.utils.GeocacheUtils.getLogType;

public class NewLogFragment extends BaseFragment implements NewLogContract.View {

    public static final String NEW_LOG_TYPE = "new_log_type:";

    public static final int TYPE_FOUND = 1;
    public static final int TYPE_NOT_FOUND = 2;

    private Unbinder unbinder;

    @Inject
    Context context;
    @Inject
    NewLogContract.Presenter presenter;

    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.geocacheName)
    TextView geocacheName;
    @BindView(R.id.logType)
    TextView logType;
    @BindView(R.id.logTypeIcon)
    ImageView logTypeIcon;
    @BindView(R.id.logDate)
    TextView logDate;
    @BindView(R.id.smileRating)
    CustomSmileRating smileRating;
    @BindView(R.id.comment)
    EditText comment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_log, null);
        unbinder = ButterKnife.bind(this, view);
        setupView();
        setPresenter(presenter);
        presenter.start(getArguments().getString(GEOCACHE_CODE));
        return view;
    }

    @Override
    public void setupGeocache(Geocache geocache) {
        geocacheName.setText(geocache.getName());
    }

    private void setupView() {
        setupActionBar();
        setupSmileRating();
        logType.setText(getLogType("Found it"));
        logTypeIcon.setImageResource(GeocacheUtils.getLogIcon("Found it"));
        logTypeIcon.setColorFilter(ContextCompat.getColor(context, GeocacheUtils.getLogIconColor("Found it")));
        logDate.setText("21/11/2018 21:21");
    }

    private void setupActionBar() {
        BaseActivity activity = (BaseActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.new_log));
        }
    }

    private void setupSmileRating() {
        smileRating.setNameForSmile(BaseRating.GREAT, R.string.rate_perfect);
        smileRating.setNameForSmile(BaseRating.GOOD, R.string.rate_good);
        smileRating.setNameForSmile(BaseRating.OKAY, R.string.rate_normal);
        smileRating.setNameForSmile(BaseRating.BAD, R.string.rate_below_average_two_lines);
        smileRating.setNameForSmile(BaseRating.TERRIBLE, R.string.rate_bad);
    }
}
