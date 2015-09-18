package happybaby.pics.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import happybaby.pics.base.BaseApplication;
import happybaby.pics.view.activity.MainActivity;

/**
 * Created by yushilong on 2015/5/4.
 */
public class AppUtil {
    public static String getVersionName() {
        try {
            return BaseApplication.getInstance().getPackageManager().getPackageInfo(BaseApplication.getInstance().getPackageName(), PackageManager.PERMISSION_GRANTED).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取渠道名称
     *
     * @return
     */
//    public static String getChannel() {
//        String channel = null;
//        try {
//            ApplicationInfo info = BaseApplication.getInstance().getPackageManager().getApplicationInfo(
//                    BaseApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
//            channel = info.metaData.getString("UMENG_CHANNEL");
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return channel;
//    }

    /**
     * 获取设备ID
     *
     * @return
     */
    public static String getDeviceId() {
        String deviceId;
        TelephonyManager tel = (TelephonyManager) BaseApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        // String sim = tel.getSimSerialNumber();
        deviceId = tel.getDeviceId();
        if (deviceId == null) {
            deviceId = android.provider.Settings.System.getString(BaseApplication.getInstance().getContentResolver(), android.provider.Settings.System.ANDROID_ID);
        }
        if (deviceId == null)
            deviceId = UUID.randomUUID().toString();
        return deviceId;
    }

    public static String getPlatform() {
        return "android";
    }

    public static void jump(Activity activity, Class<? extends Activity> targetActivity, Bundle bundle) {
        Intent intent = new Intent(activity, targetActivity);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public static void jump(Activity activity, Class<? extends Activity> targetActivity) {
        jump(activity, targetActivity, null);
    }

    /**
     * 综合scheme和普通url的打开方式
     *
     * @param activity
     * @param url
     */
//    public static void jump(Activity activity, String url) {
//        if (StringUtil.isEmpty(url))
//            return;
//        if (url.startsWith(Constant.SCHEME_PREFIX))
//            jumpScheme(activity, url);
//        else {
//            Bundle bundle = new Bundle();
//            bundle.putString("url", url);
//            jump(activity, WebViewTwoActivity.class, bundle);
//        }
//    }

    /**
     * 支持Scheme跳转
     *
     * @param activity
     * @param schemeUrl
     */
    public static void jumpScheme(Activity activity, String schemeUrl) {
        if (!StringUtil.isEmpty(schemeUrl)) {
            Uri uri = Uri.parse(schemeUrl);
            Intent intent = new Intent();
            intent.setData(uri);
            activity.startActivity(intent);
        }
    }

    public static void toast(final Context context, final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 回到首页
     *
     * @param activity
     */
    public static void goHome(Activity activity) {
        Intent toHome = new Intent(activity, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("to", "main");
        toHome.putExtras(bundle);
        toHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(toHome);
    }

    /**
     * 隐藏键盘
     *
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    /**
     * 显示键盘
     *
     * @param activity
     */
    public static void showSoftInput(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    public static boolean isNetConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) (BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivity != null) {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public static String getNetworkType() {
        ConnectivityManager connectivity = (ConnectivityManager) (BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivity != null) {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.getTypeName();
            }
        }
        return null;
    }

    /**
     * 获取Uri参数名集合
     *
     * @param uri
     * @return
     */
    public static Set<String> getQueryParameterNames(Uri uri) {
        if (uri.isOpaque()) {
            throw new UnsupportedOperationException("This isn't a hierarchical URI.");
        }
        String query = uri.getEncodedQuery();
        if (query == null) {
            return Collections.emptySet();
        }
        Set<String> names = new LinkedHashSet<String>();
        int start = 0;
        do {
            int next = query.indexOf('&', start);
            int end = (next == -1) ? query.length() : next;

            int separator = query.indexOf('=', start);
            if (separator > end || separator == -1) {
                separator = end;
            }

            String name = query.substring(start, separator);
            names.add(uri.decode(name));

            // Move start to end of name.
            start = end + 1;
        } while (start < query.length());
        return Collections.unmodifiableSet(names);
    }

    public static ProgressDialog getProgressDialog(Context context, String message, boolean isCanceledOnTouchOutside, boolean isCancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(isCancelable);
        progressDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        return progressDialog;
    }

    /**
     * @param activity
     * @param isPaiZhao 是否是启动拍照程序
     * @param isCrop    图片是否需要裁剪
     */
//    public static void toPicImage(Activity activity, boolean isPaiZhao, boolean isCrop) {
//        Intent intent = new Intent(activity, BasePicImageActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putBoolean("isPaiZhao", isPaiZhao);
//        bundle.putBoolean("isCrop", isCrop);
//        intent.putExtras(bundle);
//        activity.startActivity(intent);
//    }

    /**
     * 复制字符串
     *
     * @param str
     */
    public static void copy(String str) {
        ClipboardManager cmb = (ClipboardManager) BaseApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(str);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                AppUtil.toast(BaseApplication.getInstance(), "复制成功!");
            }
        });
    }
}
