package com.htc.cs.prophet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import android.support.v4.app.Fragment;

import com.htc.cs.prophet.data.NewsAdapter;
import com.htc.cs.prophet.service.RecommendRequest;
import com.htc.cs.prophet.service.RecommendRequest.OnGetNewRecommendListener;
import com.htc.cs.prophet.utils.Utils;

import java.util.List;

/**
 * Created by Ct_Huang on 5/26/15.
 */
public class NewsFragment extends Fragment {

    private static final String TAG = "[Prophet][" + NewsFragment.class.getSimpleName() + "]";


    public static final String NEWS_TYPE = "news_type";
    public static final String AID = "aid";
    public static final String TYPE_HISTORY = "history";
    public static final String TYPE_CF = "cf";
    public static final String TYPE_IFCF = "ifcf";
    public static final String TYPE_HOT_NEWS = "hot_news";
    public static final String TYPE_RELATED = "related";

    private RecyclerView mNewsListView;
    private NewsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsListView = (RecyclerView) inflater.inflate(R.layout.fragment_main, container, false);
        mNewsListView.setLayoutManager(new LinearLayoutManager(mNewsListView.getContext()));
        return mNewsListView;
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        final Context context = activity;

        String type = getArguments().getString(NEWS_TYPE);

        if (type.equals(TYPE_CF)) {

            RecommendRequest.getNewsRecommendations(context, Utils.getDeviceSN(context), 0, new OnGetNewRecommendListener() {
                @Override
                public void onSuccess(List<String> list) {
                    adapter = new NewsAdapter(context, list, NewsAdapter.TYPE_RECOMMEND);
                    mNewsListView.setAdapter(adapter);
                }

                @Override
                public void onError() {
                    Log.e(TAG, "getNewsRecommendations error");
                    ((MainActivity) activity).showHotNews();
                }
            });
        } else if (type.equals(TYPE_HISTORY)) {

            RecommendRequest.getReadingHistory(context, Utils.getDeviceSN(context), new OnGetNewRecommendListener() {
                @Override
                public void onSuccess(List<String> list) {
                    adapter = new NewsAdapter(context, list, NewsAdapter.TYPE_HISTORY);
                    mNewsListView.setAdapter(adapter);
                }

                @Override
                public void onError() {
                    Log.e(TAG, "getNewsRecommendations error");
                }
            });

        } else if (type.equals(TYPE_IFCF)){

            RecommendRequest.getNewsRecommendations(context, Utils.getDeviceSN(context), 1, new OnGetNewRecommendListener() {
                @Override
                public void onSuccess(List<String> list) {
                    adapter = new NewsAdapter(context, list, NewsAdapter.TYPE_RECOMMEND);
                    mNewsListView.setAdapter(adapter);
                }

                @Override
                public void onError() {
                    Log.e(TAG, "getNewsRecommendations error");
                }
            });
        } else if (type.equals(TYPE_RELATED)){

            String aid = getArguments().getString(AID);
            RecommendRequest.getRelatedList(context, aid, new OnGetNewRecommendListener() {
                @Override
                public void onSuccess(List<String> list) {
                    adapter = new NewsAdapter(context, list, NewsAdapter.TYPE_RECOMMEND);
                    mNewsListView.setAdapter(adapter);
                }

                @Override
                public void onError() {
                    Log.e(TAG, "getNewsRecommendations error");
                }
            });
        } else if (type.equals(TYPE_HOT_NEWS)) {
            RecommendRequest.geHotNewsList(context,  new OnGetNewRecommendListener() {
                @Override
                public void onSuccess(List<String> list) {
                    adapter = new NewsAdapter(context, list, NewsAdapter.TYPE_RECOMMEND);
                    mNewsListView.setAdapter(adapter);
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
