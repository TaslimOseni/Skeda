package com.dabinu.apps.skeda;


import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Inner extends AppCompatActivity{


    TextView head, turner, stateText;
    ImageButton back;
    Button process, timeSelector;
    Switch stateSwitch;
    CountDownTimer ticker;
    String notificationTitle = "";
    String userTerminatedText = "";
    String normalTerminatedText = "";
    boolean terminator;
    boolean happyEnding = true;



    public long convertTimeStringsToTime(String timeFormattedString){
        long result = 0;

        char rawFormOfTime[] = timeFormattedString.trim().toCharArray();

        if(rawFormOfTime[6] == 'A' || rawFormOfTime[6] == 'a'){
            if(rawFormOfTime[0] == '1' && rawFormOfTime[1] == '2'){
                result = (Integer.parseInt(Character.toString(rawFormOfTime[3]).concat(Character.toString(rawFormOfTime[4]))) * 60);
            }
            else{
                result = (Integer.parseInt(Character.toString(rawFormOfTime[0]).concat(Character.toString(rawFormOfTime[1]))) * 3600) + (Integer.parseInt(Character.toString(rawFormOfTime[3]).concat(Character.toString(rawFormOfTime[4]))) * 60);
            }
        }



        else if(rawFormOfTime[6] == 'P' || rawFormOfTime[6] == 'p'){
            if(rawFormOfTime[0] == '1' && rawFormOfTime[1] == '2'){
                result = (12 * 3600) + (Integer.parseInt(Character.toString(rawFormOfTime[3]).concat(Character.toString(rawFormOfTime[4]))) * 60);
            }
            else{
                result = ((Integer.parseInt(Character.toString(rawFormOfTime[0]).concat(Character.toString(rawFormOfTime[1]))) + 12) * 3600) + (Integer.parseInt(Character.toString(rawFormOfTime[3]).concat(Character.toString(rawFormOfTime[4]))) * 60);
            }

        }

        return result;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);

        overridePendingTransition(0, 0);


        final WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



        head = (TextView) findViewById(R.id.nameOfCarrierIntent);
        turner = (TextView) findViewById(R.id.turner);
        stateText = (TextView) findViewById(R.id.stateText);
        back = (ImageButton) findViewById(R.id.back);
        stateSwitch = (Switch) findViewById(R.id.state);
        process = (Button) findViewById(R.id.process);
        timeSelector = (Button) findViewById(R.id.timeSelector);



        String leadString = getIntent().getStringExtra("NAME");


        switch(leadString){
            case "WiFi":
                head.setText(leadString);
                if(wifiManager.getWifiState() == 1){
                    stateText.setText(R.string.offString);
                    turner.setText(R.string.turnOnString);
                    stateSwitch.setChecked(false);
                }
                else{
                    stateText.setText(R.string.onString);
                    turner.setText(R.string.turnOffString);
                    stateSwitch.setChecked(true);
                }
                break;


            case "Bluetooth":
                head.setText(leadString);
                if(bluetoothAdapter.isEnabled()){
                    stateText.setText(R.string.onString);
                    turner.setText(R.string.turnOffString);
                    stateSwitch.setChecked(true);
                }
                else{
                    stateText.setText(R.string.offString);
                    turner.setText(R.string.turnOnString);
                    stateSwitch.setChecked(false);
                }
                break;


            case "Flight mode":
                head.setText(leadString);
                if(Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1) {
                    stateText.setText(R.string.onString);
                    turner.setText(R.string.turnOffString);
                    stateSwitch.setChecked(true);
                }
                else{
                    stateText.setText(R.string.offString);
                    turner.setText(R.string.turnOnString);
                    stateSwitch.setChecked(false);
                }
                break;


            case "Hotspot":
                head.setText(leadString);
                try{
                    if((Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager) == 13) {
                        stateText.setText(R.string.onString);
                        turner.setText(R.string.turnOffString);
                        stateSwitch.setChecked(true);
                    }
                    else{
                        stateText.setText(R.string.offString);
                        turner.setText(R.string.turnOnString);
                        stateSwitch.setChecked(false);
                    }
                }
                catch(Exception e){

                }
                break;



            case "Data Conn.":
                head.setText(leadString);

                if (((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
                    stateText.setText(R.string.onString);
                    turner.setText(R.string.turnOffString);
                    stateSwitch.setChecked(true);
                }
                else{
                    stateText.setText(R.string.offString);
                    turner.setText(R.string.turnOnString);
                    stateSwitch.setChecked(false);
                }
        }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(getApplicationContext(), Display.class);
                startActivity(goBack);
            }
        });


        process.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String currentTime = new SimpleDateFormat("hh:mm a").format(new Date());
                long diff = convertTimeStringsToTime(returnStringFromTextView()) - convertTimeStringsToTime(currentTime);

                if(convertTimeStringsToTime(returnStringFromTextView()) < convertTimeStringsToTime(currentTime)){
                    Toast.makeText(getApplicationContext(), "Negative time "+ diff, Toast.LENGTH_LONG).show();
                }
                else if(convertTimeStringsToTime(returnStringFromTextView()) == convertTimeStringsToTime(currentTime)){
                    Toast.makeText(getApplication(), "Equal", Toast.LENGTH_LONG).show();
                }
                else{
                    switch(head.getText().toString().trim()){


                        //WiFi......................................................................


                        case "WiFi":
                            if(wifiManager.getWifiState() == 1){
                                notificationTitle = "Turning WiFi on by "+returnStringFromTextView();
                                userTerminatedText = "Terminated. WiFi has been turned on by user";
                                normalTerminatedText = "WiFi has been turned on";
                                terminator = false;
                            }
                            else{
                                notificationTitle = "Turning WiFi off by "+returnStringFromTextView();
                                userTerminatedText = "Terminated. WiFi has been turned off by user";
                                normalTerminatedText = "WiFi has been turned off";
                                terminator = true;
                            }
                            ticker = new CountDownTimer(diff * 1000, 1000){
                                @Override
                                public void onTick(long millisUntilFinished){

                                    final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(notificationTitle).setContentText("").setAutoCancel(false).build();
                                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mng.notify(0, notification);

                                    if(terminator){
                                        if(!(terminator && wifiManager.getWifiState() != 1)){
                                            happyEnding = false;
                                            ticker.cancel();
                                            mng.cancel(0);
                                            ticker.onFinish();
                                        }
                                    }
                                    else{
                                        if(!(!terminator && wifiManager.getWifiState() == 1)){
                                            happyEnding = false;
                                            ticker.cancel();
                                            mng.cancel(0);
                                            ticker.onFinish();
                                        }
                                    }
                                }

                                @Override
                                public void onFinish(){
                                    if(happyEnding){
                                        final Notification finalNotif = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(normalTerminatedText).setContentText("").setAutoCancel(true).build();
                                        NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        mng.notify(0, finalNotif);
                                        if(wifiManager.getWifiState() != 1){
                                            wifiManager.setWifiEnabled(false);
                                        }
                                        else{
                                            wifiManager.setWifiEnabled(true);
                                        }
                                    }
                                    else{
                                        final Notification finalNotiff = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(userTerminatedText).setContentText("").setAutoCancel(true).build();
                                        NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        mng.notify(0, finalNotiff);
                                    }
                                }
                            }.start();
                            break;


                        //Bluetooth.................................................................


                        case "Bluetooth":
                            if(bluetoothAdapter.isEnabled()){
                                notificationTitle = "Turning Bluetooth off by "+returnStringFromTextView();
                                userTerminatedText = "Terminated. Bluetooth has been turned off by user";
                                normalTerminatedText = "Bluetooth has been turned off";
                                terminator = true;
                            }
                            else{
                                notificationTitle = "Turning Bluetooth on by "+returnStringFromTextView();
                                userTerminatedText = "Terminated. Bluetooth has been turned on by user";
                                normalTerminatedText = "Bluetooth has been turned on";
                                terminator = false;
                            }
                            ticker = new CountDownTimer(diff * 1000, 1000){
                                @Override
                                public void onTick(long millisUntilFinished){

                                    final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(notificationTitle).setContentText("").setAutoCancel(false).build();
                                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mng.notify(0, notification);

                                    if(terminator){
                                        if(!(terminator && bluetoothAdapter.isEnabled())){
                                            happyEnding = false;
                                            ticker.cancel();
                                            mng.cancel(0);
                                            ticker.onFinish();
                                        }
                                    }
                                    else{
                                        if(!(!terminator && !bluetoothAdapter.isEnabled())){
                                            happyEnding = false;
                                            ticker.cancel();
                                            mng.cancel(0);
                                            ticker.onFinish();
                                        }
                                    }
                                }

                                @Override
                                public void onFinish() {
                                    if(happyEnding){
                                        final Notification finalNotif = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(normalTerminatedText).setContentText("").setAutoCancel(true).build();
                                        NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        mng.notify(0, finalNotif);
                                        if(bluetoothAdapter.isEnabled()){
                                            bluetoothAdapter.disable();
                                        }
                                        else{
                                            bluetoothAdapter.enable();
                                        }
                                    }
                                    else{
                                        final Notification finalNotiff = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(userTerminatedText).setContentText("").setAutoCancel(true).build();
                                        NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        mng.notify(0, finalNotiff);
                                    }
                                }
                            }.start();
                            break;


                        //Flight mode...............................................................


                        case "Flight mode":
                            if(Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1) {
                                notificationTitle = "Turning Flight mode off by "+returnStringFromTextView();
                                userTerminatedText = "Terminated. Flight mode has been turned off by user";
                                normalTerminatedText = "Flight mode has been turned off";
                                terminator = true;
                            }
                            else{
                                notificationTitle = "Turning Flight mode on by "+returnStringFromTextView();
                                userTerminatedText = "Terminated. Flight mode has been turned on by user";
                                normalTerminatedText = "Flight mode has been turned on";
                                terminator = false;
                            }
                            ticker = new CountDownTimer(diff * 1000, 1000){
                                @Override
                                public void onTick(long millisUntilFinished){

                                    final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(notificationTitle).setContentText("").setAutoCancel(false).build();
                                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mng.notify(0, notification);

                                    if(terminator){
                                        if(!(terminator && Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1)){
                                            happyEnding = false;
                                            ticker.cancel();
                                            mng.cancel(0);
                                            ticker.onFinish();
                                        }
                                    }
                                    else{
                                        if(!(!terminator && !(Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1))){
                                            happyEnding = false;
                                            ticker.cancel();
                                            mng.cancel(0);
                                            ticker.onFinish();
                                        }
                                    }
                                }

                                @Override
                                public void onFinish(){
                                    if(happyEnding){
                                        final Notification finalNotif = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(normalTerminatedText).setContentText("").setAutoCancel(true).build();
                                        NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        mng.notify(0, finalNotif);
                                        if(Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1){
                                            //PUT CODE HERE
                                        }
                                        else{
                                            //PUT CODE HERE
                                        }
                                    }
                                    else{
                                        final Notification finalNotiff = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(userTerminatedText).setContentText("").setAutoCancel(true).build();
                                        NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        mng.notify(0, finalNotiff);
                                    }
                                }
                            }.start();
                            break;


                        //Hotspot.......................................................................


                        case "Hotspot":
                            try{
                                if((Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager) == 13) {
                                    notificationTitle = "Turning Hotspot off by "+returnStringFromTextView();
                                    userTerminatedText = "Terminated. Hotspot has been turned off by user";
                                    normalTerminatedText = "Hotspot has been turned off";
                                    terminator = true;
                                }
                                else{
                                    notificationTitle = "Turning Hotspot on by "+returnStringFromTextView();
                                    userTerminatedText = "Terminated. Hotspot has been turned on by user";
                                    normalTerminatedText = "Hotspot has been turned on";
                                    terminator = false;
                                }
                            }
                            catch(Exception e){

                            }
                            ticker = new CountDownTimer(diff * 1000, 1000){
                                @Override
                                public void onTick(long millisUntilFinished){

                                    final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(notificationTitle).setContentText("").setAutoCancel(false).build();
                                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mng.notify(0, notification);

                                    try{
                                        if(terminator){
                                            if(!(terminator && (Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager) == 13)){
                                                happyEnding = false;
                                                ticker.cancel();
                                                mng.cancel(0);
                                                ticker.onFinish();
                                            }
                                        }
                                        else{
                                            if(!(!terminator && !((Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager) == 13))){
                                                happyEnding = false;
                                                ticker.cancel();
                                                mng.cancel(0);
                                                ticker.onFinish();
                                            }
                                        }
                                    }
                                    catch(Exception e){

                                    }
                                }

                                @Override
                                public void onFinish() {
                                    if(happyEnding){
                                        final Notification finalNotif = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(normalTerminatedText).setContentText("").setAutoCancel(true).build();
                                        NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        mng.notify(0, finalNotif);

                                    }
                                    else{
                                        final Notification finalNotiff = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(userTerminatedText).setContentText("").setAutoCancel(true).build();
                                        NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        mng.notify(0, finalNotiff);
                                    }
                                }
                            }.start();
                            break;


                        //Data Conn.................................................................


                        case "Data Conn.":
                            if(((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
                                notificationTitle = "Turning Data Conn. off by "+returnStringFromTextView();
                                userTerminatedText = "Terminated. Data Connection has been turned off by user";
                                normalTerminatedText = "Data Conn. has been turned off";
                                terminator = true;
                            }
                            else{
                                notificationTitle = "Turning Data Conn. on by "+returnStringFromTextView();
                                userTerminatedText = "Terminated. Data Conn. has been turned on by user";
                                normalTerminatedText = "Data Conn. has been turned on";
                                terminator = false;
                            }
                            ticker = new CountDownTimer(diff * 1000, 1000){
                                @Override
                                public void onTick(long millisUntilFinished){

                                    final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(notificationTitle).setContentText("").setAutoCancel(false).build();
                                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mng.notify(0, notification);

                                    if(terminator){
                                        if(!(terminator && ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected())){
                                            happyEnding = false;
                                            ticker.cancel();
                                            mng.cancel(0);
                                            ticker.onFinish();
                                        }
                                    }
                                    else{
                                        if(!(!terminator && !(((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()))){
                                            happyEnding = false;
                                            ticker.cancel();
                                            mng.cancel(0);
                                            ticker.onFinish();
                                        }
                                    }
                                }

                                @Override
                                public void onFinish() {
                                    if(happyEnding){
                                        final Notification finalNotif = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(normalTerminatedText).setContentText("").setAutoCancel(true).build();
                                        NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        mng.notify(0, finalNotif);
                                        if(((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
                                            try{
                                                ConnectivityManager dataManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                                                Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
                                                dataMtd.setAccessible(true);
                                                dataMtd.invoke(dataManager, false);
                                            }
                                            catch(Exception e){
                                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else{
                                            try{
                                                ConnectivityManager dataManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                                                Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
                                                dataMtd.setAccessible(true);
                                                dataMtd.invoke(dataManager, true);
                                            }
                                            catch(Exception e){
                                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                            }
                                        }
//
                                    }
                                    else{
                                        final Notification finalNotiff = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(userTerminatedText).setContentText("").setAutoCancel(true).build();
                                        NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        mng.notify(0, finalNotiff);
                                    }
                                }
                            }.start();
                            break;

                    }

                    Toast.makeText(getApplicationContext(), "Successful!!!", Toast.LENGTH_LONG).show();

                    Intent kissTent = new Intent(getApplicationContext(), Display.class);
                    startActivity(kissTent);
                }



            }
        });


        timeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });



    }




    @Override
    public void onBackPressed() {
        back.performClick();
    }





    public void showTimePickerDialog(View v) {
        DialogFragment timeFragment = new TimePicker();
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }




    public String returnStringFromTextView(){
        char[] rawData = process.getText().toString().trim().toCharArray();
        String ourStandardTime = "";

        if(rawData[6] == 'f'){
            for(int i = 12; i < rawData.length; i++){
                ourStandardTime += (Character.toString(rawData[i]));
            }
        }


        else if(rawData[6] == 'n'){
            for(int i = 11; i < rawData.length; i++){
                ourStandardTime += (Character.toString(rawData[i]));
            }
        }

        return ourStandardTime.trim();
    }




}