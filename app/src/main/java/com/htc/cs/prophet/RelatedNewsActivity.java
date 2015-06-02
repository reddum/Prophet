package com.htc.cs.prophet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class RelatedNewsActivity extends ActionBarActivity {

    private static final String TAG = "[Prophet][" + RelatedNewsActivity.class.getSimpleName() + "]";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_news);

        String aid = getIntent().getStringExtra("aid");

        Fragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(NewsFragment.NEWS_TYPE, NewsFragment.TYPE_RELATED);
        args.putString(NewsFragment.AID, aid);


        fragment.setArguments(args);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, fragment).commit();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Related News");

    }
}
