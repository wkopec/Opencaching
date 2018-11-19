package pl.opencaching.android.ui.geocache.new_log;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.hsalf.smilerating.BaseRating;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.GeocacheLogDraw;
import pl.opencaching.android.ui.base.BaseActivity;
import pl.opencaching.android.ui.base.BaseFragment;
import pl.opencaching.android.ui.dialogs.MessageDialog;
import pl.opencaching.android.ui.dialogs.NewLogTypeDialog;
import pl.opencaching.android.utils.GeocacheUtils;
import pl.opencaching.android.utils.StringUtils;
import pl.opencaching.android.utils.events.ChangeLogTypeEvent;
import pl.opencaching.android.utils.views.CustomSmileRating;
import pl.opencaching.android.utils.views.like_button.LikeButtonView;

import static pl.opencaching.android.ui.geocache.GeocacheActivity.GEOCACHE_CODE;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_ATTENDED;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_COMMENT;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_FOUND;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_MAINTENANCE_PERFORMED;
import static pl.opencaching.android.utils.Constants.LOG_TYPE_NEEDS_MAINTENANCE;
import static pl.opencaching.android.utils.GeocacheUtils.getLogType;
import static pl.opencaching.android.utils.StringUtils.getDateString;
import static pl.opencaching.android.utils.StringUtils.getTimeString;

public class NewLogFragment extends BaseFragment implements NewLogContract.View {

    public static final String NEW_LOG_TYPE = "new_log_type:";

    @Inject
    Context context;
    @Inject
    NewLogContract.Presenter presenter;

    @BindView(R.id.buttonViewSwitcher)
    ViewSwitcher buttonViewSwitcher;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.geocacheName)
    TextView geocacheName;
    @BindView(R.id.logType)
    TextView logTypeTv;
    @BindView(R.id.logTypeIcon)
    ImageView logTypeIcon;
    @BindView(R.id.logDateTextView)
    TextView logDateTextView;
    @BindView(R.id.logTimeTextView)
    TextView logTimeTextView;
    @BindView(R.id.rateLabel)
    ConstraintLayout rateLabel;
    @BindView(R.id.smileRating)
    CustomSmileRating smileRating;
    @BindView(R.id.recommendationButton)
    LikeButtonView recommendationButton;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.passwordLabel)
    ConstraintLayout passwordLabel;
    @BindView(R.id.password)
    EditText password;

    private Geocache geocache;
    private GeocacheLogDraw geocacheLogDraw;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_log, null);
        ButterKnife.bind(this, view);
        geocacheLogDraw = new GeocacheLogDraw(getArguments().getString(GEOCACHE_CODE));
        geocacheLogDraw.setType(getArguments().getString(NEW_LOG_TYPE));

        setupView();
        setPresenter(presenter);
        presenter.start(geocacheLogDraw.getGeocacheCode());
        return view;
    }

    @Override
    public void setupGeocache(Geocache geocache) {
        this.geocache = geocache;
        geocacheName.setText(geocache.getName());
        setupPasswordLabel(geocache.isPasswordRequired());
    }

    @Override
    public void finish() {
        //Toast.makeText(requireActivity(), R.string.geocache_submited, Toast.LENGTH_SHORT).show();
        requireActivity().finish();
    }

    private void setupView() {
        setupActionBar();
        setupSmileRating();
        setupLogType(geocacheLogDraw.getType());
        setupLogDate(geocacheLogDraw.getDateTime());
    }

    private void setupActionBar() {
        ActionBar actionBar = ((BaseActivity) requireActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.new_log));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_log_menu, menu);

        Switch draftSwitch = menu.findItem(R.id.switchItem).getActionView().findViewById(R.id.draftSwitch);
        draftSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                buttonViewSwitcher.showNext();
            } else {
                buttonViewSwitcher.showPrevious();
            }

        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupSmileRating() {
        smileRating.setNameForSmile(BaseRating.GREAT, R.string.rate_perfect);
        smileRating.setNameForSmile(BaseRating.GOOD, R.string.rate_good);
        smileRating.setNameForSmile(BaseRating.OKAY, R.string.rate_normal);
        smileRating.setNameForSmile(BaseRating.BAD, R.string.rate_below_average_two_lines);
        smileRating.setNameForSmile(BaseRating.TERRIBLE, R.string.rate_bad);
    }

    private void setupLogDate(DateTime logDate) {
        logDateTextView.setText(getDateString(logDate, context));
        logTimeTextView.setText(getTimeString(logDate.getHourOfDay(), logDate.getMinuteOfHour(), context));
    }

    private void setupLogType(String newLogType) {
        logTypeTv.setText(getLogType(newLogType));
        logTypeIcon.setImageResource(GeocacheUtils.getLogIcon(newLogType));
        logTypeIcon.setColorFilter(ContextCompat.getColor(context, GeocacheUtils.getLogIconColor(newLogType)));
        if (newLogType.equals(LOG_TYPE_FOUND)) {
            rateLabel.setVisibility(View.VISIBLE);
        } else {
            rateLabel.setVisibility(View.GONE);
        }
    }

    private void setupPasswordLabel(boolean isPasswordRequires) {
        passwordLabel.setVisibility(isPasswordRequires && geocacheLogDraw.getType().equals(LOG_TYPE_FOUND) ? View.VISIBLE : View.GONE);
    }

    private GeocacheLogDraw getGeocacheLogDraw() {
        geocacheLogDraw.setComment(comment.getText().toString());
        if (geocache.isPasswordRequired()) {
            geocacheLogDraw.setPassword(password.getText().toString());
        }
        switch (geocacheLogDraw.getType()) {
            case LOG_TYPE_NEEDS_MAINTENANCE:
                geocacheLogDraw.setType(LOG_TYPE_COMMENT);
                geocacheLogDraw.setNeedMaintenance(true);
                break;
            case LOG_TYPE_MAINTENANCE_PERFORMED:
                geocacheLogDraw.setType(LOG_TYPE_COMMENT);
                geocacheLogDraw.setNeedMaintenance(false);
                break;
            default:
                geocacheLogDraw.setNeedMaintenance(null);
                break;
        }
        if(geocacheLogDraw.getType().equals(LOG_TYPE_FOUND) || geocacheLogDraw.getType().equals(LOG_TYPE_ATTENDED)) {
            if(smileRating.getRating() > 0 && smileRating.getRating() < 6) {
                geocacheLogDraw.setRate(smileRating.getRating());
            }
            geocacheLogDraw.setRecommended(recommendationButton.isChecked());
        }
        return geocacheLogDraw;
    }

    @OnClick(R.id.submitButton)
    public void onNewLogClick() {
        presenter.submitNewLog(getGeocacheLogDraw());
    }

    @OnClick(R.id.createDraftButton)
    public void onCreateDraftClick() {
        presenter.createDraft(getGeocacheLogDraw());
    }

    @OnClick({R.id.logType, R.id.logTypeIcon})
    public void onChangeLogTypeClick() {
        NewLogTypeDialog messageDialog = NewLogTypeDialog.newInstance(geocacheLogDraw.getGeocacheCode(), NewLogTypeDialog.CHANGE_LOG_TYPE);
        messageDialog.show(requireActivity().getSupportFragmentManager(), NewLogTypeDialog.class.getName());
    }

    @OnClick(R.id.usePattern)
    public void onUsePatternClick() {

    }

    @OnClick({R.id.logDateTextView, R.id.logTimeTextView})
    public void onChangeDateClick() {
        DateTime date = geocacheLogDraw.getDateTime();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    geocacheLogDraw.setDate(geocacheLogDraw.getDateTime()
                            .withYear(year)
                            .withMonthOfYear(monthOfYear)
                            .withDayOfMonth(dayOfMonth).toDate());
                    setupLogDate(geocacheLogDraw.getDateTime());

                    TimePickerDialog timePickerDialog = new TimePickerDialog(requireActivity(),
                            (view1, hourOfDay, minute) -> {
                                geocacheLogDraw.setDate(geocacheLogDraw.getDateTime()
                                        .withHourOfDay(hourOfDay)
                                        .withMinuteOfHour(minute).toDate());
                                setupLogDate(geocacheLogDraw.getDateTime());
                            }, date.getHourOfDay(), date.getMinuteOfHour(), true);
                    timePickerDialog.show();

                }, date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onChangeLogType(ChangeLogTypeEvent event) {
        geocacheLogDraw.setType(event.getLogType());
        setupLogType(geocacheLogDraw.getType());

        setupPasswordLabel(geocache.isPasswordRequired());
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void showMessage(String title, String message) {
        MessageDialog messageDialog = MessageDialog.newInstance(title, message);
        messageDialog.setOnOkClickListener(v -> requireActivity().finish());
        messageDialog.setCancelable(false);
        messageDialog.show(requireActivity().getSupportFragmentManager(), MessageDialog.class.getName());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
