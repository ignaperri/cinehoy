package com.nachomp.cinehoy.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.nachomp.cinehoy.common.Constantes;
import com.nachomp.cinehoy.common.SharedPreferencesManager;
import com.nachomp.cinehoy.firebase.FirebaseUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance(){
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        checkSubscribeFirebaseAllTopic();
        FirebaseUtils.configurarFirebaseRemoteConfig();
    }

    private void checkSubscribeFirebaseAllTopic(){
        boolean isSubscribeAllTopic = SharedPreferencesManager.getSomeBooleanValue(Constantes.PREF_FIREBASE_SUBSCRIBE_ALL_TOPIC);
        if(!isSubscribeAllTopic){
            FirebaseUtils.subscribeToTopic(Constantes.FIREBASE_SUBSCRIBE_TOPIC_ALL);
            SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_FIREBASE_SUBSCRIBE_ALL_TOPIC, true);
        }
    }

    public String getCurrentLanguage(){
        String lan = Locale.getDefault().getDisplayLanguage();
        String lan_tag = Locale.getDefault().toLanguageTag();
        return lan_tag;
    }

    public String getVersionName(){
        String versionCode = "";
        try {
            PackageInfo pInfo = instance.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private void setInstalationDate(){
        try{
            String pattern = "dd-MM-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));
            SharedPreferencesManager.setSomeStringValue(Constantes.PREF_INSTALATION_DATE, date);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getInstalationDate(){
        String date = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_INSTALATION_DATE);
        if(date == null || date.isEmpty()){
            setInstalationDate();
            return SharedPreferencesManager.getSomeStringValue(Constantes.PREF_INSTALATION_DATE);
        }else{
            return date;
        }
    }
}
