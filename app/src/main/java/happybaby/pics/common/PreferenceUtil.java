package happybaby.pics.common;

import android.content.SharedPreferences;

import happybaby.pics.base.BaseApplication;

/**
 * Created by yushilong on 2015/5/21.
 */
public class PreferenceUtil {
    public static final String NAME = "preference_happybaby";
    private static SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences(NAME, 0);

    public static void putString(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    public static String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    public static class PreferenceKeys {
        public static final String FIRST_OPEN_V1 = "FIRST_OPEN_V1.0";
    }
}
