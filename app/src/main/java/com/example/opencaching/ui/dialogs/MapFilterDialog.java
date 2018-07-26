package com.example.opencaching.ui.dialogs;

import com.example.opencaching.R;
import com.example.opencaching.ui.base.BaseDialog;

public class MapFilterDialog extends BaseDialog {

    public static MapFilterDialog newInstance() {
        return new MapFilterDialog();
    }

    @Override
    protected void setupView() {
        setIcon(R.drawable.ic_filter);
    }

    @Override
    protected int getChildId() {
        return R.layout.dialog_map_filter;
    }
}
