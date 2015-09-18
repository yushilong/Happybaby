package happybaby.pics.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by yushilong on 2015/5/5.
 */
public interface IActivityHelper {
    Context IGetContext();

    Activity IGetActivity();

    int IGetContentViewResId();

    View IGetContentView();

    /**
     * 初始化View
     */
    void IFindViews();

    /**
     * 初始化View 主要用于Fragment
     *
     * @param view
     */
    void IFindViews(View view);//for fragment

    /**
     * 初始化数据
     */
    void IInitData();

    /**
     * 请求数据
     */
    void IRequest();
}
