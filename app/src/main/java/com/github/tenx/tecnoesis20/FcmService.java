package com.github.tenx.tecnoesis20;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;

import com.github.tenx.tecnoesis20.ui.main.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import timber.log.Timber;

public class FcmService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        try {
            String msg = data.get("message");
            String title = data.get("title");
            createNotification(title, msg);
        }catch (NullPointerException e){
            e.printStackTrace();
        }




    }
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Timber.d("new token : "+s);

//        send token to server


    }



    public void createNotification(String title, String message)
    {
        Context mContext = getApplicationContext();
        Intent resultIntent = new Intent(mContext , MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 , resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        android.app.Notification.Builder mBuilder = new android.app.Notification.Builder(mContext);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setSmallIcon(R.drawable.icon)
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("1201", "CHANNEL1", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100});
            assert mNotificationManager != null;
            mBuilder.setChannelId("1201");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 , mBuilder.build());
    }
}