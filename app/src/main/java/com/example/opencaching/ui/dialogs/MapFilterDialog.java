package com.example.opencaching.ui.dialogs;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;

import com.example.opencaching.R;
import com.example.opencaching.ui.base.BaseDialog;

import butterknife.BindView;

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
    }

    private void setListeners() {
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


}
