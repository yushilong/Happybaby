package happybaby.pics.http;

import android.os.Looper;

import happybaby.pics.base.BaseApplication;
import happybaby.pics.common.AppUtil;

/**
 * Created by yushilong on 2015/5/4.
 */
public class HttpCallback {
    public void success(Object object) {

    }

    public void failure(int status, String errorMsg) {
        if (status != 0 && Looper.myLooper() == Looper.getMainLooper()) {
            AppUtil.toast(BaseApplication.getInstance(), errorMsg);
        }
    }
}
