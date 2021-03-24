package com.example.swe483_assignment1_group1;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //called when alarm is fired, show notification
        String primaryKey = intent.getStringExtra("");
        Reminder reminder = retrieveReminderDetails(primaryKey, context);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = getNotification(reminder,context);
        //edit id here
        notificationManager.notify(11, notification);

    }

    private Notification getNotification (Reminder reminder, Context context) {
        NotificationCompat.Builder builder;
        if (reminder.reminderImportance == "high")
            builder = new NotificationCompat.Builder(context, "HighImportanceChannel");
        else
            builder = new NotificationCompat.Builder(context, "LowImportanceChannel");

        builder.setContentIntent(getDetailsActivityPendingIntent(context, reminder));
        builder.setContentTitle(reminder.reminderTitle) ;
        //builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel(true) ;
//        builder.setChannelId("") ;
        return builder.build() ;
    }

    private PendingIntent getDetailsActivityPendingIntent(Context context, Reminder reminder){
        Intent notifyIntent = new Intent(context, infoActivity.class);

        notifyIntent.putExtra("reminderTitle", reminder.reminderTitle);
        notifyIntent.putExtra("reminderDate", reminder.reminderDate);
        notifyIntent.putExtra("reminderTime", reminder.reminderTime);
        notifyIntent.putExtra("reminderImportance", reminder.reminderImportance);

        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Create the PendingIntent
        return PendingIntent.getActivity(
                context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private Reminder retrieveReminderDetails(String primaryKey, Context context){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        String query = "Select * from ReminderDetails where reminderTitle = '" + primaryKey + "'";
        Cursor cursor = databaseHelper.getRemindersCustomQuery(query);

        int reminderTitleIndex = cursor.getColumnIndex("reminderTitle");
        int reminderDateIndex = cursor.getColumnIndex("reminderDate");
        int reminderTimeIndex = cursor.getColumnIndex("reminderTime");
        int reminderImportanceIndex = cursor.getColumnIndex("reminderImportance");

        String reminderTitle = cursor.getString(reminderTitleIndex);
        String reminderDate = cursor.getString(reminderDateIndex);
        String reminderTime = cursor.getString(reminderTimeIndex);
        String reminderImportance = cursor.getString(reminderImportanceIndex);

        return new Reminder(reminderTitle, reminderDate, reminderTime, reminderImportance);
    }

}
