package pro200.smile.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import pro200.smile.MainActivity;
import pro200.smile.R;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "smile-notifier";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeatingIntent = new Intent(context, MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(context, CHANNEL_ID);
        nb.setContentIntent(pendingIntent);
        nb.setSmallIcon(R.mipmap.ic_launcher);
        nb.setContentTitle("Smile!");
        nb.setContentText("Don't forget to smile!");
        nb.setAutoCancel(true);

        notificationManager.notify(100, nb.build());
    }
}
