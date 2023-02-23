package com.teamx.vevae;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.teamx.vevae.activities.MainActivity;



public class NotificationHelper {

    public static void displayNotification(Context context,String title, String description){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.Channel_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000});
        builder.setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,builder.build());


    }

}
