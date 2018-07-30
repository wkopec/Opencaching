package com.example.opencaching.ui.dialogs;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatCheckBox;

import com.example.opencaching.R;
import com.example.opencaching.ui.base.BaseDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class MapFilterDialog extends BaseDialog {

    @BindView(R.id.foundFilter)
    AppCompatCheckBox foundFilter;
    @BindView(R.id.notFoundFilter)
    AppCompatCheckBox notFoundFilter;
    @BindView(R.id.ownFilter)
    AppCompatCheckBox ownFilter;
    @BindView(R.id.ignoredFilter)
    AppCompatCheckBox ignoredFilter;
    @BindView(R.id.trackableFilter)
    AppCompatCheckBox trackableFilter;
    @BindView(R.id.availableFilter)
    AppCompatCheckBox availableFilter;
    @BindView(R.id.temporaryUnavailableFilter)
    AppCompatCheckBox tempUnavailableFilter;
    @BindView(R.id.archivedFilter)
    AppCompatCheckBox archivedFilter;
    @BindView(R.id.ftfFilter)
    AppCompatCheckBox ftfFilter;
    @BindView(R.id.powerTrailFilter)
    AppCompatCheckBox powerTrailFilter;

    @BindView(R.id.traditionalFilter)
    AppCompatCheckBox traditionalFilter;
    @BindView(R.id.multicacheFilter)
    AppCompatCheckBox multicacheFilter;
    @BindView(R.id.quizFilter)
    AppCompatCheckBox quizFilter;
    @BindView(R.id.unknownFilter)
    AppCompatCheckBox unknownFilter;
    @BindView(R.id.virtualFilter)
    AppCompatCheckBox virtualFilter;
    @BindView(R.id.eventFilter)
    AppCompatCheckBox eventFilter;
    @BindView(R.id.owncacheFilter)
    AppCompatCheckBox owncacheFilter;
    @BindView(R.id.movingFilter)
    AppCompatCheckBox movingFilter;
    @BindView(R.id.webcamFilter)
    AppCompatCheckBox webcamFilter;

    public static MapFilterDialog newInstance() {
        return new MapFilterDialog();
    }

    @Override
    protected int getChildId() {
        return R.layout.dialog_map_filter;
    }

    @Override
    protected void setupView() {
        setIcon(R.drawable.ic_filter);
        setFilters();
        setListeners();
    }

    @OnClick(R.id.filterGeocaches)
    public void onFilterButtonClick() {
        //TODO: save filters
        dismiss();
    }

    private void setFilters() {
        foundFilter.setChecked(false);
        notFoundFilter.setChecked(true);
        ownFilter.setChecked(true);
        ignoredFilter.setChecked(false);
        trackableFilter.setChecked(false);
        availableFilter.setChecked(true);
        tempUnavailableFilter.setChecked(false);
        archivedFilter.setChecked(false);
        ftfFilter.setChecked(false);
        powerTrailFilter.setChecked(false);

        setGeocacheFilter(traditionalFilter, true);
        setGeocacheFilter(multicacheFilter, true);
        setGeocacheFilter(quizFilter, true);
        setGeocacheFilter(unknownFilter, true);
        setGeocacheFilter(virtualFilter, false);
        setGeocacheFilter(eventFilter, true);
        setGeocacheFilter(owncacheFilter, true);
        setGeocacheFilter(movingFilter, false);
        setGeocacheFilter(webcamFilter, false);

    }

    private void setGeocacheFilter(AppCompatCheckBox checkBox, boolean isChecked){
        checkBox.setChecked(isChecked);
        animateDrawable(checkBox.getBackground(), isChecked);
    }

    private void setListeners() {
        traditionalFilter.setOnCheckedChangeListener((buttonView, isChecked) -> animateDrawable(buttonView.getBackground(), isChecked));
        multicacheFilter.setOnCheckedChangeListener((buttonView, isChecked) -> animateDrawable(buttonView.getBackground(), isChecked));
        quizFilter.setOnCheckedChangeListener((buttonView, isChecked) -> animateDrawable(buttonView.getBackground(), isChecked));
        unknownFilter.setOnCheckedChangeListener((buttonView, isChecked) -> animateDrawable(buttonView.getBackground(), isChecked));
        virtualFilter.setOnCheckedChangeListener((buttonView, isChecked) -> animateDrawable(buttonView.getBackground(), isChecked));
        eventFilter.setOnCheckedChangeListener((buttonView, isChecked) -> animateDrawable(buttonView.getBackground(), isChecked));
        owncacheFilter.setOnCheckedChangeListener((buttonView, isChecked) -> animateDrawable(buttonView.getBackground(), isChecked));
        movingFilter.setOnCheckedChangeListener((buttonView, isChecked) -> animateDrawable(buttonView.getBackground(), isChecked));
        webcamFilter.setOnCheckedChangeListener((buttonView, isChecked) -> animateDrawable(buttonView.getBackground(), isChecked));

        foundFilter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!isChecked && !notFoundFilter.isChecked()) {
                notFoundFilter.setChecked(true);
            }
        });
        notFoundFilter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!isChecked && !foundFilter.isChecked()) {
                foundFilter.setChecked(true);
            }
        });

        availableFilter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!isChecked && !tempUnavailableFilter.isChecked() && !archivedFilter.isChecked()) {
                tempUnavailableFilter.setChecked(true);
            }
        });
        tempUnavailableFilter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!isChecked && !availableFilter.isChecked() && !archivedFilter.isChecked()) {
                availableFilter.setChecked(true);
            }
        });
        archivedFilter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!isChecked && !availableFilter.isChecked() && !tempUnavailableFilter.isChecked()) {
                availableFilter.setChecked(true);
            }
        });
    }

    public void animateDrawable(final Drawable drawable, boolean isSelected) {
        ValueAnimator colorAnim;
        if(isSelected) {
            colorAnim = ObjectAnimator.ofFloat(0.8f, 0f);
        } else {
            colorAnim = ObjectAnimator.ofFloat(0f, 0.8f);
        }

        colorAnim.addUpdateListener(animation -> {
            float mul = (Float) animation.getAnimatedValue();
            int alphaGray = adjustAlpha(getResources().getColor(R.color.accent_gray), mul);
            drawable.setColorFilter(alphaGray, PorterDuff.Mode.SRC_ATOP);
            if (mul == 0.0) {
                drawable.setColorFilter(null);
            }
        });

        colorAnim.setDuration(300);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();

    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

}
