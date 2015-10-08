package happybaby.pics.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.sso.UMSsoHandler;

import happybaby.pics.R;
import happybaby.pics.common.AppUtil;
import happybaby.pics.common.share.ShareUtil;


/**
 * Created by yushilong on 2015/5/4.
 */
public abstract class BaseActivity extends ActionBarActivity implements IActivityHelper {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ProgressDialog progressDialog;
    private View toolbarClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        progressDialog = AppUtil.getProgressDialog(this, "加载中...", true, true);
        if (IGetContentView() != null)
            setContentView(IGetContentView());
        else if (IGetContentViewResId() != 0) {
            setContentView(IGetContentViewResId());
        }
        View view = findViewById(R.id.toolbar);
        View title = findViewById(R.id.toolbarTitle);
        View close = findViewById(R.id.toolbarClose);
        if (view != null) {
            toolbar = (Toolbar) view;
            toolbar.setNavigationIcon(R.mipmap.ic_action_previous_item);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            if (getMenuResId() != 0) {
                getToolbar().inflateMenu(getMenuResId());
                if (getOnMenuItemClickListener() != null)
                    getToolbar().setOnMenuItemClickListener(getOnMenuItemClickListener());
            }
        }
        if (title != null) {
            toolbarTitle = (TextView) title;
            toolbarTitle.setText(getTitle());
        }
        if (close != null) {
            toolbarClose = close;
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        IFindViews();
        IInitData();
        IRequest();
    }

    public Toolbar.OnMenuItemClickListener getOnMenuItemClickListener() {
        return null;
    }

    public int getMenuResId() {
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public Context IGetContext() {
        return this;
    }

    @Override
    public Activity IGetActivity() {
        return this;
    }

    @Override
    public abstract int IGetContentViewResId();

    @Override
    public View IGetContentView() {
        return null;
    }

    @Override
    public abstract void IFindViews();

    @Override
    public void IFindViews(View view) {

    }

    @Override
    public abstract void IInitData();

    @Override
    public void IRequest() {

    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public TextView getToolbarTitle() {
        return toolbarTitle;
    }

    public View getToolbarClose() {
        return toolbarClose;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 根据requestCode获取对应的SsoHandler
        UMSsoHandler ssoHandler = ShareUtil.mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}