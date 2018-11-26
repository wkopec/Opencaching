package pl.opencaching.android.ui.dialogs;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import pl.opencaching.android.R;
import pl.opencaching.android.app.prefs.SessionManager;
import pl.opencaching.android.ui.base.BaseDialog;
import pl.opencaching.android.utils.events.MapTypeChangeEvent;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class MapTypeDialog extends BaseDialog {

    @Inject
    SessionManager sessionManager;

    @BindView(R.id.mapTypeDefaultBorder)
    View mapTypeDefaultBorder;
    @BindView(R.id.mapTypeDefault)
    TextView mapTypeDefault;

    @BindView(R.id.mapTypeSatelliteBorder)
    View mapTypeSatelliteBorder;
    @BindView(R.id.mapTypeSatellite)
    TextView mapTypeSatellite;

    @BindView(R.id.mapTypeTerrainBorder)
    View mapTypeTerrainBorder;
    @BindView(R.id.mapTypeTerrain)
    TextView mapTypeTerrain;

    private TextView selectedTextView;
    private View selectedBorder;

    public static MapTypeDialog newInstance() {
        return new MapTypeDialog();
    }

    @Override
    protected int getChildId() {
        return R.layout.dialog_map_type;
    }

    @Override
    protected void setupView() {
        setSelectedMapType(sessionManager.getMapType());
        setIcon(R.drawable.ic_layers);
    }

    private void setSelectedMapType(int mapType) {

        switch (mapType) {
            case MAP_TYPE_NORMAL:
                selectedTextView = mapTypeDefault;
                selectedBorder = mapTypeDefaultBorder;
                break;
            case MAP_TYPE_SATELLITE:
                selectedTextView = mapTypeSatellite;
                selectedBorder = mapTypeSatelliteBorder;
                break;
            case MAP_TYPE_TERRAIN:
                selectedTextView = mapTypeTerrain;
                selectedBorder = mapTypeTerrainBorder;
                break;
            default:
                selectedTextView = mapTypeDefault;
                selectedBorder = mapTypeDefaultBorder;
        }

        if(selectedTextView != null) {
            selectedTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            selectedTextView.setTypeface(null, Typeface.BOLD);
            selectedBorder.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.mapTypeDefaultIcon, R.id.mapTypeDefault})
    public void onDefaultTypeClick() {
        changeMapType(MAP_TYPE_NORMAL);
    }

    @OnClick({R.id.mapTypeSatelliteIcon, R.id.mapTypeSatellite})
    public void onSatelliteTypeClick() {
        changeMapType(MAP_TYPE_SATELLITE);
    }

    @OnClick({R.id.mapTypeTerrainIcon, R.id.mapTypeTerrain})
    public void onTerrainTypeClick() {
        changeMapType(MAP_TYPE_TERRAIN);
    }

    private void changeMapType(int mapType) {
        setSelectedMapType(mapType);
        sessionManager.saveMapType(mapType);
        EventBus.getDefault().postSticky(new MapTypeChangeEvent(mapType));
        dismiss();
    }

}
