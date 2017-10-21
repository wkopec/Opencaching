package com.example.opencaching.fragments.geocache_info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opencaching.R;
import com.example.opencaching.activities.BaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.opencaching.activities.GeocacheActivity.getGeocacheWaypoint;

/**
 * Created by Wojtek on 27.07.2017.
 */

public class GeocacheInfoFragment extends Fragment implements GeocacheInfoFragmentView {

    private Unbinder unbinder;
    private View view;
    private BaseActivity activity;
    private GeocacheInfoFragmentPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_geocache_info, null);
        unbinder = ButterKnife.bind(this, view);
        activity = (BaseActivity) getActivity();
        presenter = new GeocacheInfoFragmentPresenter(this, activity);
        presenter.getGeocacheInfo(getGeocacheWaypoint());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
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
