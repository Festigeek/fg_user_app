package com.example.flavia.fg_user_app.notification;

import android.util.Log;
import android.widget.Toast;

import com.example.flavia.fg_user_app.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FIREBASE Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        // Toast.makeText(this, "From: " + remoteMessage.getFrom() + " - " + remoteMessage.getNotification().getBody(), Toast.LENGTH_LONG ).show();
    }
}
