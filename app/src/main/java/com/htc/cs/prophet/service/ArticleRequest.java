package com.htc.cs.prophet.service;

import android.content.Context;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.htc.cs.prophet.data.NewsMeta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ct_Huang on 5/21/15.
 */
public class ArticleRequest {

    private static final String TAG = "[Prophet][" + ArticleRequest.class.getSimpleName() + "]";

    private static final String HOST = "https://geo-prism.htctouch.com/";
    private static final String FeedITEM_API = "feedItem/item/";

    private static LruCache<String, JSONObject> requestCache = new LruCache<String, JSONObject>(5 * 1024 * 1024);

    public interface OnGetArticleMeta {
        void onSuccess(NewsMeta meta) ;
        void onError();
    }

    public static void getArticle(Context context, String aid, final OnGetArticleMeta l) {

        final String url = HOST + FeedITEM_API + aid;

        JSONObject articleMeta = requestCache.get(url);

        if (articleMeta != null) {
            l.onSuccess(getNewsMeta(articleMeta));
        }

        JsonObjectRequest request =  new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        requestCache.put(url, response);
                        l.onSuccess(getNewsMeta(response));
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage(), error);
                        l.onError();
                    }
                });

        RequestQueueInstance.getInstance(context).addToRequestQueue(request);

    }


    public static NewsMeta getNewsMeta(JSONObject object) {

        try {
            JSONObject res = object.getJSONObject("res");
            JSONObject item = res.getJSONObject("item");
            JSONObject meta = item.getJSONObject("meta");


            String title = meta.getString("tl");
            String url = meta.getString("u");
            String provider = meta.getString("src");
            String id = meta.getString("id");
            long timestamp = meta.getLong("ts");
            String coverUrl = "";
            try {
                JSONObject th = meta.getJSONObject("th");
                String coverId = th.getString("id");
                JSONArray rs = th.getJSONArray("rs");
                int size = rs.getInt(0);
                for (int i = 1; i < res.length(); i++) {
                    if (size > rs.getInt(i)) {
                        size = rs.getInt(i);
                    }
                }
                coverUrl = getCoverUrl(coverId, size);
            } catch (JSONException e) {}

            return new NewsMeta(id, title, coverUrl, url, provider, timestamp);

        } catch (JSONException e) {
            Log.e(TAG, "Wrong JSON::" + object.toString());
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }


    private static String getCoverUrl(String id, int resolution) {

        String url = "https://geo-prism.htctouch.com/thumbnail?id=";
        url += id;
        url += "&w=";
        url += resolution;

        return url;
    }
}
