package com.bridge.soom.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Thaher-Majeed on 13-03-2017.
 */

public class SharedPreferencesManager {

        private static SharedPreferences mSharedPref;//mSharedPref for device and login, app settings details
    private static SharedPreferences mSharedPrefOther;//mSharedPref for other for future use
    private static SharedPreferences mSharedPrefMaybe;//mSharedPref for fututee use

    public static void init(Context context)
    {
        if(mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }



}