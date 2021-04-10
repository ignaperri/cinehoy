package com.nachomp.cinehoy.firebase.services;

import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nachomp.cinehoy.R;
import com.nachomp.cinehoy.app.MyApp;
import com.nachomp.cinehoy.common.Constantes;
import com.nachomp.cinehoy.common.SharedPreferencesManager;
import com.nachomp.cinehoy.firebase.FirebaseUtils;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "onMessageReceived";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Firebase: From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Firebase: Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            String titulo = data.get("title");
            String mensaje = data.get("message");
            if(titulo != null && titulo.equalsIgnoreCase(Constantes.FIREBASE_TITLE_MESSAGING)) {
                Log.d(TAG, "Firebase:  Message Notification Body: " + mensaje);

                if(mensaje.equalsIgnoreCase(Constantes.TAG_MSG_FIREBASE_ADD_USERS)){
                    FirebaseUtils.addUsersFromPushMessage(getApplicationContext());
                }
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Firebase:  Message Notification Body: " + remoteMessage.getNotification().getBody());
                mostrarNotificacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }

        }
    }

    private void mostrarNotificacion(String title, String msg) {
        try {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyApp.getInstance());
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApp.getInstance(), Constantes.NOTIFICATION_CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.ic_firebase_messaging)
                    .setSound(soundUri)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            notificationManager.notify(0, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        sendRegistrationToServer(s);
    }
    private void sendRegistrationToServer(String token) {
        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_FIREBASE_USUARIO_TOKEN, token);
        Log.i(TAG, "Firebase sendRegistrationToServer newToken: " + token);
    }
}
