package happybaby.pics.common;

import android.content.Context;
import android.content.res.Configuration;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import happybaby.pics.base.BaseApplication;

/**
 * Created by yushilong on 2015/5/13.
 */
public class DeviceUtil {
    public static Display getDefaultDisplay() {
        return ((WindowManager) BaseApplication.getInstance().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, BaseApplication.getInstance().getResources().getDisplayMetrics());
    }

    public static boolean isTablet(Context c) {
        return (c.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
