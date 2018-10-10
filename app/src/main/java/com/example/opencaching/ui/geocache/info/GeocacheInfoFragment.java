package com.example.opencaching.ui.geocache.info;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.opencaching.R;
import com.example.opencaching.data.models.okapi.Geocache;
import com.example.opencaching.ui.base.BaseActivity;
import com.example.opencaching.ui.base.BaseFragment;
import com.github.lzyzsd.circleprogress.ArcProgress;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.opencaching.ui.geocache.GeocacheActivity.getGeocacheWaypoint;
import static com.example.opencaching.utils.GeocacheUtils.getGeocacheShortSize;
import static com.example.opencaching.utils.GeocacheUtils.getSizeIntValue;
import static com.example.opencaching.utils.StringUtils.getFormatedCoordinates;

/**
 * Created by Wojtek on 27.07.2017.
 */

public class GeocacheInfoFragment extends BaseFragment implements GeocacheInfoContract.View {

    private Unbinder unbinder;
    private View view;
    private BaseActivity activity;

    @BindView(R.id.geocacheTopLabel)
    TextView geocacheTopLabel;
    @BindView(R.id.geocacheName)
    TextView geocacheName;
    @BindView(R.id.geocacheFound)
    TextView geocacheFound;
    @BindView(R.id.geocacheNotFound)
    TextView geocacheNotFound;
    @BindView(R.id.geocacheRecommendation)
    TextView geocacheRecommendation;
    @BindView(R.id.difficulty)
    TextView difficulty;
    @BindView(R.id.terrain)
    TextView terrain;
    @BindView(R.id.size)
    TextView size;
    @BindView(R.id.difficultyProgress)
    ArcProgress difficultyProgress;
    @BindView(R.id.terrainProgress)
    ArcProgress terrainProgress;
    @BindView(R.id.sizeProgress)
    ArcProgress sizeProgress;

    @Inject
    GeocacheInfoContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_geocache_info, null);
        unbinder = ButterKnife.bind(this, view);
        activity = (BaseActivity) getActivity();
        setPresenter(presenter);
        // TODO: get waypoint from args using getInstance
        presenter.getGeocacheInfo(getGeocacheWaypoint());
        return view;
    }

    @Override
    public void setGeocacheData(Geocache geocache) {
        geocacheName.setText(geocache.getName());
        geocacheTopLabel.setText(String.format(getString(R.string.separated_strings), geocache.getCode(), getFormatedCoordinates(geocache.getPosition())));

        geocacheFound.setText(String.valueOf(geocache.getFounds()));
        geocacheNotFound.setText(String.valueOf(geocache.getNotFounds()));
        geocacheRecommendation.setText(String.valueOf(geocache.getRecommendations()));

        difficulty.setText(String.valueOf(geocache.getDifficulty()));
        terrain.setText(String.valueOf(geocache.getTerrain()));
        size.setText(getGeocacheShortSize(geocache.getSize()));

        animateArcProgress(difficultyProgress, (int) (geocache.getDifficulty() * 100));
        animateArcProgress(terrainProgress, (int) (geocache.getTerrain() * 100));
        animateArcProgress(sizeProgress, (int) (getSizeIntValue(geocache.getSize()) * 100));

    }

    private void animateArcProgress(ArcProgress arcProgress, int value) {
        ValueAnimator progressAnimator = ValueAnimator.ofInt(0, value).setDuration(1000);
        progressAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            arcProgress.setProgress(animatedValue);
        });
        progressAnimator.start();
    }

    @Override
    public void showError(Error error) {
        activity.showToast(error.getMessage());
    }

    @Override
    public void showProgress() {
        activity.showProgress();
    }

    @Override
    public void hideProgress() {
        activity.hideProgress();
    }

}
