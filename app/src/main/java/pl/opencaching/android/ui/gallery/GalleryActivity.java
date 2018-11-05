package pl.opencaching.android.ui.gallery;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Image;
import pl.opencaching.android.ui.base.BaseActivity;
import pl.opencaching.android.ui.base.SectionsPagerAdapter;
import pl.opencaching.android.ui.gallery.image.ImageFragment;
import pl.opencaching.android.utils.views.GalleryViewPager;

public class GalleryActivity extends BaseActivity {

    public static String KEY_IMAGE_ITEM = "key_image_item:";
    public static String KEY_IMAGE_POSITION = "key_image_position:";
    public static String KEY_IMAGE_ITEMS = "key_image_items:";

    @BindView(R.id.container)
    ConstraintLayout container;
    @BindView(R.id.galleryViewPager)
    GalleryViewPager galleryViewPager;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.galleryIndicator)
    TextView galleryIndicator;
    @BindView(R.id.shadow)
    View shadow;

    private Context context;

    public static void launchGallery(Activity context, View photoView, Image transitionImage, int imagePosition, ArrayList<Image> images) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra(KEY_IMAGE_ITEM, transitionImage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context,
                    photoView,
                    transitionImage.getUuid());

            intent.putParcelableArrayListExtra(KEY_IMAGE_ITEMS, new ArrayList<>(images));
            intent.putExtra(KEY_IMAGE_POSITION, imagePosition);
            context.startActivity(intent, options.toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        context = this;
        Intent intent = getIntent();

        intent.setExtrasClassLoader(Image.class.getClassLoader());
        Image image = intent.getParcelableExtra(KEY_IMAGE_ITEM);

        ArrayList<Image> images = intent.getParcelableArrayListExtra(KEY_IMAGE_ITEMS);
        int imagePosition = intent.getIntExtra(KEY_IMAGE_POSITION, 0);

        description.setText(image.getCaption());
        galleryIndicator.setText(String.format(getString(R.string.separated_indicators), imagePosition, images.size()));

        configureViewPager(images, imagePosition);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            galleryViewPager.setTransitionName(image.getUuid());
            getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    galleryIndicator.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    shadow.setVisibility(View.VISIBLE);
                    ObjectAnimator.ofFloat(galleryIndicator, View.ALPHA, 0.0f, 1.0f).setDuration(500).start();
                    ObjectAnimator.ofFloat(description, View.ALPHA, 0.0f, 1.0f).setDuration(500).start();
                    ObjectAnimator.ofFloat(shadow, View.ALPHA, 0.0f, 1.0f).setDuration(500).start();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }
    }

    private void configureViewPager(ArrayList<Image> images, int position) {
        ArrayList<SectionsPagerAdapter.Item> items = new ArrayList<>();
        for(Image image : images) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(KEY_IMAGE_ITEM, image);
            ImageFragment fragment = new ImageFragment();
            fragment.setArguments(bundle);
            items.add(new SectionsPagerAdapter.Item(getString(R.string.info), fragment));
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), items);
        galleryViewPager.setAdapter(sectionsPagerAdapter);

        galleryViewPager.setCurrentItem(position);

        galleryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(positionOffset < 0.5) {
                    description.setText(images.get(position).getCaption());
                    galleryIndicator.setText(String.format(getString(R.string.separated_indicators), position + 1, images.size()));
                } else {
                    description.setText(images.get(position + 1).getCaption());
                    galleryIndicator.setText(String.format(getString(R.string.separated_indicators), position + 2, images.size()));
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
