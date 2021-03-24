package com.example.swe483_assignment1_group1;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationHelper extends Application {
    public static final String channel1ID = "HighImportanceChannel";
    public static final String channel1Name = "ImportantRemindersChannel";
    public static final String channel2ID = "LowImportanceChannel";
    public static final String channel2Name = "LowImportanceRemindersChannel";

    @Override
    public void onCreate(){
        super.onCreate();
        createChannels();
    }

    private void createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            NotificationChannel highImportanceChannel = new NotificationChannel(channel1ID,channel1Name, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(highImportanceChannel);

            NotificationChannel lowImportanceChannel = new NotificationChannel(channel2ID,channel2Name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(lowImportanceChannel);
        }
    }
}


