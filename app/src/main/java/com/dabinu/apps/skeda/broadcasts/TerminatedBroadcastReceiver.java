package com.dabinu.apps.skeda.broadcasts;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TerminatedBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "adfwegfwegwe", Toast.LENGTH_LONG).show();
    }
}
