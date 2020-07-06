package com.inpt.coronapp;

import android.app.NotificationManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Myfirebaseservice extends FirebaseMessagingService {

    private static final String CANAL = "MyNotifCanal";
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        String myMessage = remoteMessage.getNotification().getBody();
        Log.d("FirebaseMessage","Vous venez de recevoir une notification");

        //creer une notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CANAL);
        notificationBuilder.setContentTitle("Reminder");
        notificationBuilder.setContentTitle(myMessage);



        //envoyer la notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }
}
