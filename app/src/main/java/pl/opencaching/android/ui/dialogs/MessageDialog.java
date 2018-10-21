package pl.opencaching.android.ui.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import pl.opencaching.android.R;
import pl.opencaching.android.ui.base.BaseDialog;

public class MessageDialog extends BaseDialog {

    public static final String KEY_ICON = "icon";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";

    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.message)
    TextView tvMessage;

    private int icon;
    private String title;
    private String message;

    public static MessageDialog newInstance(String message) {
        return newInstance( -1, null, message);
    }

    public static MessageDialog newInstance(int icon, String message) {
        return newInstance(icon, null, message);
    }

    public static MessageDialog newInstance(int icon, String title, String message) {
        Bundle args = new Bundle();
        args.putInt(MessageDialog.KEY_ICON, icon);
        args.putString(MessageDialog.KEY_TITLE, title);
        args.putString(MessageDialog.KEY_MESSAGE, message);
        MessageDialog dialog = new MessageDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            icon = getArguments().getInt(KEY_ICON);
            title = getArguments().getString(KEY_TITLE);
            message = getArguments().getString(KEY_MESSAGE);
        }
    }

    @Override
    protected int getChildId() {
        return R.layout.dialog_message;
    }

    @Override
    protected void setupView() {
        if(title != null) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }
        if(icon != -1) {
            setIcon(icon);
        } else {
            setIcon(R.drawable.ic_info);
        }
        tvMessage.setText(message);
    }

    @OnClick(R.id.ok)
    public void onOkClick() {
        dismiss();
    }
}
