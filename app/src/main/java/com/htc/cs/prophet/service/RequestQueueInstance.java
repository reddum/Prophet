package com.htc.cs.prophet.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.htc.cs.prophet.utils.SimpleImageCache;


/**
 * Created by Ct_Huang on 5/21/15.
 */
public class RequestQueueInstance {

    private static RequestQueueInstance mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;


    private RequestQueueInstance(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new SimpleImageCache(context));
    }

    public static synchronized RequestQueueInstance getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestQueueInstance(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}

