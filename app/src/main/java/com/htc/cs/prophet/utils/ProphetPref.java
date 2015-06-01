package com.htc.cs.prophet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Created by Ct_Huang on 6/1/15.
 */
public class ProphetPref {

    public static void setSerial(Context context, String sn) {
        SharedPreferences settings = context.getSharedPreferences("PREF_DEMO", 0);
        SharedPreferences.Editor PE = settings.edit();
        PE.putString("serial", sn);
        PE.commit();
    }

    public static String getSerial(Context context) {
        SharedPreferences settings = context.getSharedPreferences("PREF_DEMO", 0);
        return settings.getString("serial", Build.SERIAL);
    }
}
