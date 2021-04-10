package com.nachomp.cinehoy.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.nachomp.cinehoy.app.MyApp;

public class SharedPreferencesManager {
    private SharedPreferencesManager() {
    }

    private static SharedPreferences getSharedPreferences(){
        return MyApp.getContext().getSharedPreferences("", Context.MODE_PRIVATE);
    }

    public static void setSomeStringValue(String dataLabel, String dataValue){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(dataLabel, dataValue);
        editor.commit();
    }

    public static void setSomeBooleanValue(String dataLabel, boolean dataValue){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(dataLabel, dataValue);
        editor.commit();
    }

    public static String getSomeStringValue(String dataLabel){
        return getSharedPreferences().getString(dataLabel, null);
    }

    public static boolean getSomeBooleanValue(String dataLabel){
        return getSharedPreferences().getBoolean(dataLabel, false);
    }
}
