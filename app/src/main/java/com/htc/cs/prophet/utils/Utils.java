package com.htc.cs.prophet.utils;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by Ct_Huang on 5/21/15.
 */
public class Utils {

    private static String dsn;

    public static final String getDeviceSN(Context context) {
        return TextUtils.isEmpty(dsn) ?  ProphetPref.getSerial(context) : dsn;
    }

    public static final void setDeviceSN(Context context, String sn) {
        dsn = sn;
        ProphetPref.setSerial(context, sn);
    }

}
