package com.dabinu.apps.skeda.broadcasts;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.activities.ConnectivityOnClick;


public class MyBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent){

        Bundle bundle = intent.getExtras();

        String name = bundle.getString("NAME");
        boolean task = bundle.getBoolean("TASK");
        new ConnectivityOnClick().settingSetter(context, name, task);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.notif_icon).setContentIntent(PendingIntent.getActivity(context, 0, new Intent(), 0)).setAutoCancel(true).setContentTitle(new ConnectivityOnClick().returnDoneString(name, task)).setContentText("");

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

    }
}
