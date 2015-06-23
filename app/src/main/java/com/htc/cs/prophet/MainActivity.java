package com.htc.cs.prophet;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.htc.cs.prophet.utils.Utils;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "[Prophet][" + MainActivity.class.getSimpleName() + "]";

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private ViewPager mViewPager;
    private RecommendationPagerAdapter mPagerAdapter;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        updateTitle();

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2);
        mPagerAdapter = new RecommendationPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);


//        if (mToolbar != null) {
//            setSupportActionBar(mToolbar);
//        }

        mTabLayout.setupWithViewPager(mViewPager);


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
                mPagerAdapter = new RecommendationPagerAdapter(getSupportFragmentManager());
                mViewPager.setAdapter(mPagerAdapter);
                updateTitle();
            }
        }
    }

    public void showHotNews() {

        mPagerAdapter.showHotNews();
        mPagerAdapter.notifyDataSetChanged();

    }

    public class RecommendationPagerAdapter extends FragmentStatePagerAdapter {

        private String[] titles = null;
        private boolean isHotNews = false;

        public RecommendationPagerAdapter(FragmentManager fm) {
            super(fm);
            Resources res = getResources();
            titles = res.getStringArray(R.array.tab_titles);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new NewsFragment();
            Bundle args = new Bundle();

            if (position == 1) {
                args.putString(NewsFragment.NEWS_TYPE, NewsFragment.TYPE_HISTORY);
            } else if (position == 0) {
                args.putString(NewsFragment.NEWS_TYPE, isHotNews? NewsFragment.TYPE_HOT_NEWS :NewsFragment.TYPE_CF );
//            } else {
//                args.putString(NewsFragment.NEWS_TYPE, NewsFragment.TYPE_IFCF);
            }

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return titles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void showHotNews() {
            Log.d(TAG, "showHotNews");
            titles[1] = "Hot News";
            isHotNews = true;
        }
    }
}
