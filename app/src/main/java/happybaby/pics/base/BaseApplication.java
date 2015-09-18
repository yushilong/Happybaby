package happybaby.pics.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by yushilong on 2015/9/18.
 */
public class BaseApplication extends Application {
    private static BaseApplication _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        Fresco.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static BaseApplication getInstance() {
        return _instance;
    }
}
