package com.sultan.lasttest;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sultan.lasttest.student.Actrequests;
import com.sultan.lasttest.student.MainPage;
import com.sultan.lasttest.teacher.actReceivedRequest;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingServce";
    public static final String NOTIFICATION_CHANNEL_ID = "personal_notification";
    public  static  final  int NOTIFICATION_ID =001;
    Intent intent2;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String notificationTitle = null, notificationBody = null;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();
        }

        if(notificationBody.equals("لديك طلب جديد")){
             intent2 = new Intent(this, actReceivedRequest.class);
        }else {
            intent2 = new Intent(this , MainActivity.class);

        }


        sendNotification(notificationTitle, notificationBody  , intent2);

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }


    private void sendNotification(String notificationTitle, String notificationBody , Intent intent2) {


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent2,
                PendingIntent.FLAG_UPDATE_CURRENT);


        createNotificationChannel();

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this ,NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)   //Automatically delete the notification
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_subject) //Notification icon
                .setContentIntent(pendingIntent)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationManager.IMPORTANCE_HIGH);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
    private void createNotificationChannel(){


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Personal Notification";
            String description  = "...";
            int importance = NotificationManager.IMPORTANCE_HIGH;


            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID , name ,importance);


            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}

