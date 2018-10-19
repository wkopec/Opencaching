package pl.opencaching.android.ui.dialogs;

import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import pl.opencaching.android.R;
import pl.opencaching.android.ui.base.BaseDialog;

public class MessageDialog extends BaseDialog {

    @BindView(R.id.message)
    TextView message;

    public static MessageDialog newInstance(int icon, int message) {
        return new MessageDialog();
    }

    @Override
    protected int getChildId() {
        return R.layout.dialog_message;
    }

    @Override
    protected void setupView() {
        setIcon(R.drawable.ic_info);
        message.setText(R.string.geocache_hint_unavailable_message);
    }

    @OnClick(R.id.ok)
    public void onOkClick() {
        dismiss();
    }
}
