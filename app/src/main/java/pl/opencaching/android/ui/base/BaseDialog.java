package pl.opencaching.android.ui.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import pl.opencaching.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatDialogFragment;

public abstract class BaseDialog extends DaggerAppCompatDialogFragment {

    protected abstract int getChildId();

    protected abstract void setupView();

    @BindView(R.id.dialogIcon)
    ImageView dialogIcon;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View content = inflater.inflate(R.layout.dialog_base, container, false);
        RelativeLayout rl = content.findViewById(R.id.dialogContainer);
        rl.addView(inflater.inflate(getChildId(), container, false) );
        content.requestLayout();
        ButterKnife.bind(this, content);
        return content;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    protected void setIcon(int icon) {
        dialogIcon.setImageResource(icon);
    }
}
