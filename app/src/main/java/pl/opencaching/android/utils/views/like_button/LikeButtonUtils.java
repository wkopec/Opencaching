package pl.opencaching.android.utils.views.like_button;

/**
 * Created by Miroslaw Stanek on 20.12.2015.
 * Modified by Wojciech Kopeć on 14.11.2018
 */
class LikeButtonUtils {
    static double mapValueFromRangeToRange(double value, double fromLow, double fromHigh, double toLow, double toHigh) {
        return toLow + ((value - fromLow) / (fromHigh - fromLow) * (toHigh - toLow));
    }

    static double clamp(double value, double low, double high) {
        return Math.min(Math.max(value, low), high);
    }
}
