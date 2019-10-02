package com.example.testnoti;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class notificationHealper {

    //display notification will display the notification
    public static void displayNotification(Context context,String title,String body){
        //notification Builder will build notification

        Intent intent = new Intent(context,profileActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        NotificationCompat.Builder mBuilder  = new NotificationCompat.Builder(context,MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // now create notiufication manager which will manage the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        // to display the notification use the notify method and pass notification builder to build the  notification
        notificationManagerCompat.notify(1,mBuilder.build()); //mBuilder.build() will build the message.
    }
}
