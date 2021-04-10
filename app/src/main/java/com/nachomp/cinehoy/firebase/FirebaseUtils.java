package com.nachomp.cinehoy.firebase;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.BuildConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.nachomp.cinehoy.R;
import com.nachomp.cinehoy.app.MyApp;
import com.nachomp.cinehoy.common.Constantes;
import com.nachomp.cinehoy.common.SharedPreferencesManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FirebaseUtils {

    private static final String TAG = "FirebaseUtils";


    public static void configurarFirebaseRemoteConfig(){
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
    }

    public static void registrarUserPropertiesEnAnalytics(Context ctx) {
//        String empresa = MyApp.getInstance().cacheController.getEmpresa();
//        String vend = MyApp.getInstance().cacheController.getVendedorActualCodigo();
//        String version = MyApp.getInstance().cacheController.getVersion();
//        if(empresa==null || empresa.isEmpty()) return;
//        if(vend==null || vend.isEmpty()) return;
//
//        FirebaseAnalytics mFirebaseAnalytics;
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(ctx);
//
//        version = (version!=null && !version.isEmpty()) ? version : "Unknown";
//
//        String usuario = empresa + ":" + vend;
//        mFirebaseAnalytics.setUserProperty("pic_tenant", empresa);
//        mFirebaseAnalytics.setUserProperty("pic_usuario", usuario);
//        mFirebaseAnalytics.setUserProperty("pic_version", version);
//
//        Log.d(Constant.TAG, "Firebase: registrarUserPropertiesEnAnalytics()");
    }

    public static void subscribeToTopic(final String topic){
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "Subscripto con éxito a: " + topic;
                if (!task.isSuccessful()) {
                    msg = "Subscripcion fallida a: " + topic;
                }
                Log.d(TAG, "Firebase: " + msg);
            }
        });
    }
    public static void unsubscribeFromTopic(String topic){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }

    public static void addUsersFromPushMessage(Context ctx) {
        addOrUpdateUserInRealtimeDatabase(ctx);
    }

    public static void addOrUpdateUserInRealtimeDatabase(final Context ctx){
        try{
            String key = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_FIREBASE_USUARIO_KEY);
            String language = MyApp.getInstance().getCurrentLanguage();
            String versionName = MyApp.getInstance().getVersionName();
            String instalationDate = MyApp.getInstance().getInstalationDate();

            String token = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_FIREBASE_USUARIO_TOKEN);

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference(Constantes.F_PATH_USUARIO);
            //this code for keep posts even app offline until the app online again
            ref.keepSynced(true);

            key = key==null || key.length()==0 ? ref.push().getKey() : key;

            UsuarioFirebase user = new UsuarioFirebase(token, language, versionName, instalationDate);

            ref.child(key).setValue(user);

            SharedPreferencesManager.setSomeStringValue(Constantes.PREF_FIREBASE_USUARIO_KEY, key);
            Log.i(TAG, "Firebase: Usuario Firebase creado. key: " + key);

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Keep
    @IgnoreExtraProperties
    private static class UsuarioFirebase {
        public String appToken;
        public String language;
        public String versionName;
        public String instalationDate;

        public UsuarioFirebase(){
            // Default constructor required for calls to DataSnapshot.getValue(Usuario.class)
        }

        public UsuarioFirebase(String token, String language, String versionName, String instalationDate){
            this.appToken = token;
            this.language = language;
            this.versionName = versionName;
            this.instalationDate = instalationDate;
        }
    }
}
