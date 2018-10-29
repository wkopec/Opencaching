package pl.opencaching.android.ui.geocache.info;

import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.OnClick;
import io.realm.RealmList;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Geocache;
import pl.opencaching.android.data.models.okapi.Image;
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
import static pl.opencaching.android.utils.StringUtils.getFormatedHtmlString;

/**
 * Created by Wojtek on 27.07.2017.
 */

@SuppressLint("SetJavaScriptEnabled")
public class GeocacheInfoFragment extends BaseFragment implements GeocacheInfoContract.View {

    private Unbinder unbinder;

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
    @BindView(R.id.attributeLabel)
    ConstraintLayout attributeLabel;
    @BindView(R.id.hintLabel)
    ConstraintLayout hintLabel;
    @BindView(R.id.attributeList)
    RecyclerView attributeRecycleView;
    @BindView(R.id.galleryLabel)
    ConstraintLayout galleryLabel;
    @BindView(R.id.photoList)
    RecyclerView photoRecycleView;
    @BindView(R.id.descriptionWebView)
    WebView descriptionWebView;

    @Inject
    GeocacheInfoContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geocache_info, null);
        unbinder = ButterKnife.bind(this, view);
        setupView();
        setPresenter(presenter);
        // TODO: get waypoint from args using getInstance
        presenter.getGeocacheInfo(getGeocacheWaypoint());
        return view;
    }

    private void setupView() {
        attributeRecycleView.setFocusable(false);
        ViewCompat.setNestedScrollingEnabled(attributeRecycleView, false);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        attributeRecycleView.setLayoutManager(layoutManager);
        attributeRecycleView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        attributeRecycleView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int viewWidth = attributeRecycleView.getMeasuredWidth();
                        float cardViewWidth = getActivity().getResources().getDimension(R.dimen.item_attribute_height);
                        int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);
                        if(newSpanCount > 0) {
                            layoutManager.setSpanCount(newSpanCount);
                            layoutManager.requestLayout();
                        }
                    }
                });

        photoRecycleView.setFocusable(false);
        ViewCompat.setNestedScrollingEnabled(photoRecycleView, false);
        photoRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        descriptionWebView.setFocusable(false);
        descriptionWebView.getSettings().setJavaScriptEnabled(true);
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

        if(geocache.getAttributeCodes().isEmpty()) {
            attributeLabel.setVisibility(View.GONE);
        } else {
            setAttributeAdapter(geocache.getAttributeCodes());
        }

        if(geocache.getImages().isEmpty()) {
            galleryLabel.setVisibility(View.GONE);
        } else {
            setImageAdapter(geocache.getImages());
        }

        String description = getFormatedHtmlString(geocache.getDescription());
        descriptionWebView.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null);
    }

    private void setAttributeAdapter(RealmList<String> attributeCodes) {
        AttributesAdapter adapter = new AttributesAdapter(attributeRepository.loadAttributesIncludes(attributeCodes.toArray(new String[]{})), (BaseActivity) getActivity());
        attributeRecycleView.setAdapter(adapter);

    }

    private void setImageAdapter(RealmList<Image> images) {
        PhotosAdapter adapter = new PhotosAdapter(images, getActivity());
        photoRecycleView.setAdapter(adapter);
    }

    @OnClick(R.id.hintButton)
    public void onHintClick() {
        presenter.onHintClick();
    }

    @Override
    public void showHint() {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        hintLabel.setLayoutTransition(layoutTransition);

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
        hintButton.setEnabled(false);
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
