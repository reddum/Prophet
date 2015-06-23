package com.htc.cs.prophet.data;

import android.text.TextUtils;

/**
 * Created by Ct_Huang on 5/20/15.
 */
public class NewsMeta {

    private String id;
    private String title;
    private String cover;
    private String url;
    private String tid;
    private String eid;
    private String provider;
    private long timestamp;

    public NewsMeta(String id, String title, String cover, String url, String provider, long timestamp) {
        this.id = id;
        this.title = title;
        this.cover = cover;
        this.url = url;
        this.provider = !TextUtils.isEmpty(provider) ? provider : "";
        this.timestamp = timestamp;

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCover() {
        return cover;
    }

    public String getUrl() {
        return url;
    }

    public String getProvider() { return provider; }

    public long getTimestamp() { return timestamp; }

    public void setTid(String tid) { this.tid = tid; }

    public String getTid() { return tid; }

    public void getEid(String eid) { this.eid = eid; }

    public String getEid() { return eid; }

}
