package happybaby.pics.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.IconPagerAdapter;

import happybaby.pics.R;
import happybaby.pics.base.BaseActivity;
import happybaby.pics.view.HBTabPageIndicator;
import happybaby.pics.view.fragment.PicsFragment;

public class MainActivity extends BaseActivity {
    private static final String[] CONTENT = new String[]{"美图", "分类", "我的", "设置"};
    private static final int[] ICONS = new int[]{
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
    };

    @Override
    public int IGetContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    public void IFindViews() {
        FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        HBTabPageIndicator indicator = (HBTabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }

    @Override
    public void IInitData() {

    }

    class GoogleMusicAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PicsFragment fragment = new PicsFragment();
            fragment.setArguments(new Bundle());
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getIconResId(int index) {
            return ICONS[index];
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }
}
