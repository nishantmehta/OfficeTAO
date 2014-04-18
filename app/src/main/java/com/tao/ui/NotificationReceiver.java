package com.tao.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.tao.R;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String DEBUG_TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAO", "Recurring alar; requesting download service.");
        // start the download
        NotificationManager myNotificationManager= (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        CharSequence NotificationTicket = "USB Connected!";
        CharSequence NotificationTitle = "TAO";
        CharSequence NotificationContent = "Tap to fill your logs";

        Notification notification = new Notification(R.drawable.ic_launcher, NotificationTicket, 0);
        Intent notificationIntent = new Intent(context, TAOMainScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, NotificationTitle, NotificationContent, contentIntent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        myNotificationManager.notify(0100101, notification);

    }

}