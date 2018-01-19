package com.example.flavia.fg_user_app.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.ArrayMap;
import android.util.Log;

import com.example.flavia.fg_user_app.MainActivity;
import com.example.flavia.fg_user_app.R;






import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Notification Service";

    private Map<String, String> convert;

    public MyFirebaseMessagingService() {
        super();

        convert = new ArrayMap<>();
        convert.put("HS", "notif_hs");
        convert.put("LOL", "notif_lol");
        convert.put("CSGO", "notif_csgo");
        convert.put("OW", "notif_ow");
        convert.put("Anim", "notif_anim");
        convert.put("Food", "notif_meal");
        convert.put("Ceremony", "notif_price");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        // Take back information for the notification.
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        Map<String, String> data = remoteMessage.getData();
        String channel;
        String type;

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(convert.get(entry.getKey()), false)){
                channel = entry.getKey();
                type = entry.getValue();
                sendNotification(channel, type, title, message);
                break;
            }
        }

        // do nothing if the user doesnt subscribe to this channel of notification.

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String channel, String type, String title, String message) {

        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel, "FestiApp - Notification System", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("FestiApp - " + channel);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channel);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setTicker(title)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(message)
                .setContentInfo(message)
                .setChannelId(channel)
                .setCategory(type);

        notificationManager.notify(1, notificationBuilder.build());
    }
}
