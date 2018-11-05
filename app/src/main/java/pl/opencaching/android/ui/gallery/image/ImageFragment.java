package pl.opencaching.android.ui.gallery.image;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.opencaching.android.R;
import pl.opencaching.android.data.models.okapi.Image;

import static android.view.View.VISIBLE;
import static pl.opencaching.android.ui.gallery.GalleryActivity.KEY_IMAGE_ITEM;

public class ImageFragment extends Fragment {

    @BindView(R.id.photo)
    PhotoView photo;
    @BindView(R.id.spoiler)
    ImageView spoiler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, null);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            Image image = getArguments().getParcelable(KEY_IMAGE_ITEM);
            if (image != null) {
                if (image.isSpoiler()) {
                    //TODO: change spoiler drawable
                    spoiler.setVisibility(VISIBLE);
                    spoiler.setOnClickListener(v -> {
                        photo.setScale(1);
                        ObjectAnimator animator = ObjectAnimator.ofFloat(spoiler, View.ALPHA, 1.0f, 0.0f).setDuration(500);
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                spoiler.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        animator.start();

                        spoiler.setOnClickListener(null);

                    });

                }
                Picasso.with(getActivity())
                        .load(image.getImageUrl())
                        .placeholder(getResources().getDrawable(R.drawable.ic_image_placeholder))
                        .into(photo);
            }
        }

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && photo != null) {
            photo.setScale(1);
        }
    }


}
