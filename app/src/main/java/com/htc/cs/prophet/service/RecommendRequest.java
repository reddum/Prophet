package com.htc.cs.prophet.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ct_Huang on 5/21/15.
 */
public class RecommendRequest {

    private static final String TAG = "[Prophet][" + RecommendRequest.class.getSimpleName() + "]";

    private static final String HOST = "http://bi-proto-prism-1990381229.us-west-2.elb.amazonaws.com/";
    private static final String RECOMMEND_API = "recommend/";
    private static final String RECOMMEND_IF_API = "recommend_w_if/";
    private static final String HISTORY_API = "history/";
    private static final String USER_API = "users/";
    private static final String RELATED_API = "itemsim/";


    public interface OnGetNewRecommendListener {
        void onSuccess(List<String> list) ;
        void onError();
    }

    public interface OnGetUserListListener {
        void onSuccess(List<String> list) ;
        void onError();
    }

    public static void getUserList(Context context, final OnGetUserListListener l) {
        String url = HOST + USER_API;
        Log.d(TAG, url);

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, response.toString());

                        try {
                            JSONArray users = response.getJSONArray("data");
                            List<String> list = new ArrayList<String>();
                            for (int i = 0; i < users.length(); i++) {
                                list.add(users.getString(i));
                            }
                            l.onSuccess(list);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage(), error);
                        l.onError();
                    }
                });

        request.setShouldCache(false);
        RequestQueueInstance.getInstance(context).addToRequestQueue(request);
    }

    public static void getNewsRecommendations(Context context, String deviceSN, int recType, final OnGetNewRecommendListener l) {

        String url = HOST;
        if (recType == 0) {
            url += RECOMMEND_API + deviceSN;
        } else {
            url += RECOMMEND_IF_API + deviceSN;
        }
        Log.d(TAG, url);


        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, response.toString());

                        try {
                            JSONArray recommends = response.getJSONArray("recommends");

                            List<JSONObject> recommendsList = new ArrayList<JSONObject>();
                            for (int i = 0; i < recommends.length(); i++)
                                recommendsList.add(recommends.getJSONObject(i));

                            Collections.sort(recommendsList, new Comparator<JSONObject>() {
                                @Override
                                public int compare(JSONObject a, JSONObject b) {
                                    double valA, valB;

                                    try {
                                        valA = a.getDouble("score");
                                        valB = b.getDouble("score");
                                    }
                                    catch (JSONException e) {
                                        return 0;
                                    }

                                    if( valA >= valB)
                                        return -1;
                                    else
                                        return 1;
                                }
                            });

                            List<String> list = new ArrayList<String>();
                            for (int i=0; i < recommendsList.size(); i++) {
                                Log.d(TAG, "score::" + recommendsList.get(i).getDouble("score"));
                                list.add(recommendsList.get(i).getString("aid"));
                            }
                            l.onSuccess(list);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage(), error);
                        l.onError();
                    }
                });

        request.setShouldCache(false);
        RequestQueueInstance.getInstance(context).addToRequestQueue(request);
    }

    public static void getReadingHistory(Context context, String deviceSN, final OnGetNewRecommendListener l) {

        String url = HOST + HISTORY_API + deviceSN;
        Log.d(TAG, url);

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, response.toString());

                        try {
                            JSONArray history = response.getJSONArray("history");

                            List<JSONObject> historyList = new ArrayList<JSONObject>();
                            for (int i = 0; i < history.length(); i++)
                                historyList.add(history.getJSONObject(i));

                            Collections.sort(historyList, new Comparator<JSONObject>() {
                                @Override
                                public int compare(JSONObject a, JSONObject b) {
                                    String valA = new String();
                                    String valB = new String();

                                    try {
                                        valA = (String) a.get("timestamp");
                                        valB = (String) b.get("timestamp");
                                    }
                                    catch (JSONException e) {
                                        Log.e(TAG, "JSONException in combineJSONArrays sort section", e);
                                    }

                                    int comp = valA.compareTo(valB);

                                    if(comp < 0)
                                        return 1;
                                    if(comp > 0)
                                        return -1;
                                    return 0;
                                }
                            });

                            List<String> list = new ArrayList<String>();
                            for (int i=0; i < historyList.size(); i++) {
                                list.add(historyList.get(i).getString("aid"));
                            }
                            l.onSuccess(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage(), error);
                        l.onError();
                    }
                });

        request.setShouldCache(false);
        RequestQueueInstance.getInstance(context).addToRequestQueue(request);
    }

    public static void getRelatedList(Context context, String aid, final OnGetNewRecommendListener l) {
        String url = HOST + RELATED_API + aid;
        Log.d(TAG, url);

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, response.toString());

                        try {
                            JSONArray news = response.getJSONArray("similars");

                            List<JSONObject> newsList = new ArrayList<JSONObject>();
                            for (int i = 0; i < news.length(); i++)
                                newsList.add(news.getJSONObject(i));

                            Collections.sort(newsList, new Comparator<JSONObject>() {
                                @Override
                                public int compare(JSONObject a, JSONObject b) {
                                    String valA = new String();
                                    String valB = new String();

                                    try {
                                        valA = (String) a.get("strength");
                                        valB = (String) b.get("strength");
                                    }
                                    catch (JSONException e) {
                                        Log.e(TAG, "JSONException in combineJSONArrays sort section", e);
                                    }

                                    int comp = valA.compareTo(valB);

                                    if(comp > 0)
                                        return 1;
                                    if(comp < 0)
                                        return -1;
                                    return 0;
                                }
                            });

                            List<String> list = new ArrayList<String>();
                            for (int i=0; i < newsList.size(); i++) {
                                list.add(newsList.get(i).getString("aid"));
                            }
                            l.onSuccess(list);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage(), error);
                        l.onError();
                    }
                });

        request.setShouldCache(false);
        RequestQueueInstance.getInstance(context).addToRequestQueue(request);
    }


}
