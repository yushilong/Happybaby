package happybaby.pics.view.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.umeng.update.UmengUpdateAgent;

import happybaby.pics.R;
import happybaby.pics.base.AppManager;
import happybaby.pics.base.BaseActivity;
import happybaby.pics.common.AppUtil;
import happybaby.pics.common.PreferenceUtil;
import happybaby.pics.view.fragment.PicsFragment;

public class MainActivity extends BaseActivity {
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    private View first;

    @Override
    public int IGetContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    public void IFindViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                if (menuItem.getGroupId() == R.id.gp_menu)
                    replaceContent((String) menuItem.getTitle());
                else if (menuItem.getGroupId() == R.id.gp_set)
                    AppUtil.jump(IGetActivity(), SetActivity.class);
                return true;
            }
        });
        first = findViewById(R.id.first);
        boolean isFirst = PreferenceUtil.getBoolean(PreferenceUtil.PreferenceKeys.FIRST_OPEN_V1, true);
        first.setVisibility(isFirst ? View.VISIBLE : View.GONE);
    }

    private void replaceContent(String keyword) {
        PicsFragment fragment = new PicsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tag2", keyword);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commitAllowingStateLoss();
    }

    @Override
    public void IInitData() {
        UmengUpdateAgent.update(this);
        if (!"wifi".equalsIgnoreCase(AppUtil.getNetworkType())) {
            View view = View.inflate(IGetContext(), R.layout.dialog_base, null);
            TextView tv_connect = (TextView) view.findViewById(R.id.tv_content);
            tv_connect.setText("系统检测到当前网络非WIFI环境，浏览大量图片会耗费很多的手机流量，确定要继续吗？");
            DialogPlus.newDialog(IGetContext()).setContentHolder(new ViewHolder(view)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(DialogPlus dialogPlus, View view) {
                    dialogPlus.dismiss();
                    switch (view.getId()) {
                        case R.id.bt_commit:
                            AppManager.getAppManager().AppExit(IGetContext());
                            break;
                    }
                }
            }).create().show();
        }
        replaceContent("可爱");
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.KEYCODE_MENU) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                drawerLayout.closeDrawers();
            else drawerLayout.openDrawer(Gravity.LEFT);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_iknow:
                first.setVisibility(View.GONE);
                PreferenceUtil.putBoolean(PreferenceUtil.PreferenceKeys.FIRST_OPEN_V1, false);
                break;
        }
    }
}
