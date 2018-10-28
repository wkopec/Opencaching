package pl.opencaching.android.ui.gallery;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
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

public class GalleryActivity extends BaseActivity {

    public static String KEY_IMAGE_ITEM = "key_image_item:";
    public static String KEY_IMAGE_POSITION = "key_image_position:";
    public static String KEY_IMAGE_ITEMS = "key_image_items:";

    @BindView(R.id.container)
    ConstraintLayout container;
    @BindView(R.id.galleryViewPager)
    ViewPager galleryViewPager;
    @BindView(R.id.description)
    TextView description;
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

        description.setText(image.getCaption());

        ArrayList<Image> images = intent.getParcelableArrayListExtra(KEY_IMAGE_ITEMS);
        configureViewPager(images);

        int imagePosition = intent.getIntExtra(KEY_IMAGE_POSITION, 0);
        galleryViewPager.setCurrentItem(imagePosition);

        galleryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                description.setText(images.get(position).getCaption());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            galleryViewPager.setTransitionName(image.getUuid());
            getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    description.setVisibility(View.VISIBLE);
                    shadow.setVisibility(View.VISIBLE);
                    ObjectAnimator.ofFloat(description, View.ALPHA, 0.0f, 1.0f).setDuration(500).start();
                    ObjectAnimator.ofFloat(shadow, View.ALPHA, 0.0f, 1.0f).setDuration(500).start();
//                    AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.fade_in);
//                    set.setTarget(description);
//                    set.start();
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

    private void configureViewPager(ArrayList<Image> images) {
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
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
