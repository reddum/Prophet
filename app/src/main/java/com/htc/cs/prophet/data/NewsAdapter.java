package com.htc.cs.prophet.data;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;
        public final NetworkImageView mImageView;
        public final TextView mTitleView;
        public final TextView mSubTitleView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = ((NetworkImageView) mView.findViewById(R.id.coverImage));
            mTitleView = ((TextView) mView.findViewById(R.id.title));
            mSubTitleView = ((TextView) mView.findViewById(R.id.subtitle));
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText();
        }
    }

    private static final String TAG = "[Prophet][" + NewsAdapter.class.getSimpleName() + "]";


    private Context context;
    private List<String> idList;
    private int mBackground;
    private final TypedValue mTypedValue = new TypedValue();

    public NewsAdapter(Context context, List<String> ids) {
        this.context = context;
        this.idList = ids;

        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final String aid = idList.get(position);
        holder.mView.setTag(aid);

        holder.mTitleView.setText("");
        holder.mImageView.setImageBitmap(null);
        holder.mSubTitleView.setText("");

        ArticleRequest.getArticle(context, aid, new ArticleRequest.OnGetArticleMeta() {
            @Override
            public void onSuccess(final NewsMeta meta) {

                if (meta == null) {
                    return;
                }

                if (!meta.getId().equals(holder.mView.getTag())) {
                    return;
                }

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                String dateString = formatter.format(new Date(meta.getTimestamp()));

                ImageLoader loader = RequestQueueInstance.getInstance(context).getImageLoader();
                holder.mTitleView.setText(meta.getTitle());
                holder.mImageView.setImageUrl(meta.getCover(), loader);
                holder.mSubTitleView.setText(dateString + ", " + meta.getProvider());

                holder.mView.setOnClickListener(new View.OnClickListener() {
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

    }

    @Override
    public int getItemCount() {
        return idList.size();
    }

}
