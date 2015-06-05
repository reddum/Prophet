package com.htc.cs.prophet;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.htc.cs.prophet.utils.Utils;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "[Prophet][" + MainActivity.class.getSimpleName() + "]";

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateTitle();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new RecommendationPagerAdapter(getSupportFragmentManager()));
    }

    private void updateTitle() {
        mTitle = Utils.getDeviceSN(this);
        restoreActionBar();
    }

    private void restoreActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.get_device) {
            Intent intent = new Intent(this, ListDevicesActivity.class);
            this.startActivityForResult(intent, 0);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String sn = data.getStringExtra("DSN");

                if (sn.equals("My Device")) {
                    Utils.setDeviceSN(this, Build.SERIAL);
                } else{
                    Utils.setDeviceSN(this, sn);
                }
                mViewPager.setAdapter(new RecommendationPagerAdapter(getSupportFragmentManager()));
                updateTitle();
            }
        }
    }

    public class RecommendationPagerAdapter extends FragmentStatePagerAdapter {

        public RecommendationPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new NewsFragment();
            Bundle args = new Bundle();

            if (position == 0) {
                args.putString(NewsFragment.NEWS_TYPE, NewsFragment.TYPE_HISTORY);
            } else if (position == 1) {
                args.putString(NewsFragment.NEWS_TYPE, NewsFragment.TYPE_CF);
            } else {
                args.putString(NewsFragment.NEWS_TYPE, NewsFragment.TYPE_IFCF);
            }

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Resources res = getResources();
            String[] titles = res.getStringArray(R.array.tab_titles);
            return titles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
