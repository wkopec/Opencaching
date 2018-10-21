package pl.opencaching.android.ui.geocache.info;

import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import butterknife.OnClick;
import io.realm.RealmList;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Attribute;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.repository.AttributeRepository;
import pl.opencaching.android.ui.base.BaseActivity;
import pl.opencaching.android.ui.base.BaseFragment;
import com.github.lzyzsd.circleprogress.ArcProgress;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import pl.opencaching.android.utils.GeocacheUtils;
import pl.opencaching.android.utils.StringUtils;
import pl.opencaching.android.utils.views.SecretTextView;

import static pl.opencaching.android.ui.geocache.GeocacheActivity.getGeocacheWaypoint;

/**
 * Created by Wojtek on 27.07.2017.
 */

public class GeocacheInfoFragment extends BaseFragment implements GeocacheInfoContract.View {

    private Unbinder unbinder;
    private View view;

    @Inject
    AttributeRepository attributeRepository;

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
    @BindView(R.id.hint)
    SecretTextView hint;
    @BindView(R.id.hintButton)
    TextView hintButton;
    @BindView(R.id.hintLabel)
    ConstraintLayout hintLabel;
    @BindView(R.id.attributeList)
    RecyclerView attributeRecycleView;

    @Inject
    GeocacheInfoContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_geocache_info, null);
        unbinder = ButterKnife.bind(this, view);
        setupView();
        setPresenter(presenter);
        // TODO: get waypoint from args using getInstance
        presenter.getGeocacheInfo(getGeocacheWaypoint());
        return view;
    }

    private void setupView() {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        hintLabel.setLayoutTransition(layoutTransition);
    }

    @Override
    public void setGeocacheData(Geocache geocache) {
        geocacheName.setText(geocache.getName());
        geocacheTopLabel.setText(String.format(getString(R.string.separated_strings), geocache.getCode(), StringUtils.getFormatedCoordinates(geocache.getPosition())));

        geocacheFound.setText(String.valueOf(geocache.getFounds()));
        geocacheNotFound.setText(String.valueOf(geocache.getNotFounds()));
        geocacheRecommendation.setText(String.valueOf(geocache.getRecommendations()));

        difficulty.setText(String.valueOf(geocache.getDifficulty()));
        terrain.setText(String.valueOf(geocache.getTerrain()));
        size.setText(GeocacheUtils.getGeocacheShortSize(geocache.getSize()));

        animateArcProgress(difficultyProgress, (int) (geocache.getDifficulty() * 100));
        animateArcProgress(terrainProgress, (int) (geocache.getTerrain() * 100));
        animateArcProgress(sizeProgress, (int) (GeocacheUtils.getSizeIntValue(geocache.getSize()) * 100));

        if(!geocache.getHint().isEmpty()) {
            hint.setText(geocache.getHint());
        } else {
            hintButton.setTextColor(getResources().getColor(R.color.gray));
        }

        setAttributeAdapter(geocache.getAttributeCodes());

    }

    private void setAttributeAdapter(RealmList<String> attributeCodes) {
        AttributesAdapter adapter = new AttributesAdapter(attributeRepository.loadAttributesIncludes(attributeCodes.toArray(new String[]{})), (BaseActivity) getActivity());
        attributeRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        attributeRecycleView.setAdapter(adapter);
        //attributeRecycleView.setLayoutManager( new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    @OnClick(R.id.hintButton)
    public void onHintClick() {
        presenter.onHintClick();
    }

    @Override
    public void showHint() {
        final Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(800);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                hintButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        hintButton.setAnimation(fadeOut);
        hint.setVisibility(View.VISIBLE);
        hint.show();
    }

    private void animateArcProgress(ArcProgress arcProgress, int value) {
        ValueAnimator progressAnimator = ValueAnimator.ofInt(0, value).setDuration(1000);
        progressAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            arcProgress.setProgress(animatedValue);
        });
        progressAnimator.start();
    }

}
