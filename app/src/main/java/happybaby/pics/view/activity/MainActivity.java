package happybaby.pics.view.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import happybaby.pics.R;
import happybaby.pics.base.BaseActivity;
import happybaby.pics.view.fragment.PicsFragment;

public class MainActivity extends BaseActivity {
    NavigationView navigationView;
    DrawerLayout drawerLayout;

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
                PicsFragment fragment = new PicsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("tag2", (String) menuItem.getTitle());
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commitAllowingStateLoss();
                return true;
            }
        });
    }

    @Override
    public void IInitData() {

    }
}
