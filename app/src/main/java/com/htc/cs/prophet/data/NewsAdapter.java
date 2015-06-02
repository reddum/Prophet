package com.htc.cs.prophet.data;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.htc.cs.prophet.NewsActivity;
import com.htc.cs.prophet.R;
import com.htc.cs.prophet.service.ArticleRequest;
import com.htc.cs.prophet.service.RequestQueueInstance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ct_Huang on 5/20/15.
 */
public class NewsAdapter extends BaseAdapter {

    private static final String TAG = "[Prophet][" + NewsAdapter.class.getSimpleName() + "]";


    private Context context;
    private List<String> idList;

    public NewsAdapter(Context context, List<String> ids) {
        this.context = context;
        this.idList = ids;
    }

    @Override
    public int getCount() {
        return idList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final String aid = idList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        }

        final View view = convertView;
        view.setTag(aid);
        ((NetworkImageView) view.findViewById(R.id.coverImage)).setImageBitmap(null);
        ((TextView) view.findViewById(R.id.title)).setText("");
        ((TextView) view.findViewById(R.id.subtitle)).setText("");

        ArticleRequest.getArticle(context, aid, new ArticleRequest.OnGetArticleMeta() {
            @Override
            public void onSuccess(final NewsMeta meta) {

                if (meta == null) {
                    return;
                }

                if (!meta.getId().equals(view.getTag())) {
                    return;
                }

                ImageLoader loader = RequestQueueInstance.getInstance(context).getImageLoader();
                ((TextView) view.findViewById(R.id.title)).setText(meta.getTitle());

                ((NetworkImageView) view.findViewById(R.id.coverImage)).setImageUrl(meta.getCover(), loader);

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                String dateString = formatter.format(new Date(meta.getTimestamp()));
                ((TextView) view.findViewById(R.id.subtitle)).setText(dateString + ", " + meta.getProvider());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent myIntent = new Intent(context, NewsActivity.class);
                            myIntent.putExtra("url", meta.getUrl());
                            myIntent.putExtra("title", meta.getTitle());
                            myIntent.putExtra("aid", meta.getId());
                            context.startActivity(myIntent);
                        } catch (ActivityNotFoundException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                });
            }

            @Override
            public void onError() {

            }
        });



        return convertView;
    }

}
