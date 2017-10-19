package com.dabinu.apps.skeda;


import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
                        case "WiFi":
                            ticker = new CountDownTimer(diff * 1000, 1000){
                                @Override
                                public void onTick(long millisUntilFinished){
                                    if(wifiManager.getWifiState() == 1){
                                        notificationTitle = "Turning WiFi on by "+returnStringFromTextView();
                                        userTerminatedText = "Terminated. WiFi has been turned on by user";
                                        normalTerminatedText = "WiFi has been turned on";
                                    }
                                    else{
                                        notificationTitle = "Turning WiFi off by "+returnStringFromTextView();
                                        userTerminatedText = "Terminated. WiFi has been turned off by user";
                                        normalTerminatedText = "WiFi has been turned off";
                                    }
                                    final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Bleep").setContentText(Long.toString(millisUntilFinished / 1000)).setAutoCancel(false).build();
                                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mng.notify(0, notification);
                                }

                                @Override
                                public void onFinish() {
                                    Toast.makeText(getApplicationContext(), "Whatever, boss", Toast.LENGTH_LONG).show();
                                }
                            }.start();
                            break;

                        case "Bluetooth":
                            ticker = new CountDownTimer(diff * 1000, 1000){
                                @Override
                                public void onTick(long millisUntilFinished){
                                    if(bluetoothAdapter.isEnabled()){
                                        notificationTitle = "Turning Bluetooth on by "+returnStringFromTextView();
                                        userTerminatedText = "Terminated. Bluetooth has been turned on by user";
                                        normalTerminatedText = "Bluetooth has been turned on";
                                    }
                                    else{
                                        notificationTitle = "Turning Bluetooth off by "+returnStringFromTextView();
                                        userTerminatedText = "Terminated. Bluetooth has been turned off by user";
                                        normalTerminatedText = "Bluetooth has been turned off";
                                    }


                                    final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Bleep").setContentText(Long.toString(millisUntilFinished / 1000)).setAutoCancel(false).build();
                                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mng.notify(0, notification);
                                }

                                @Override
                                public void onFinish() {
                                    Toast.makeText(getApplicationContext(), "Whatever, boss", Toast.LENGTH_LONG).show();
                                }
                            }.start();
                            break;

                        case "Flight mode":
                            ticker = new CountDownTimer(diff * 1000, 1000){
                                @Override
                                public void onTick(long millisUntilFinished){
                                    if(Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1) {
                                        notificationTitle = "Turning Flight mode off by "+returnStringFromTextView();
                                        userTerminatedText = "Terminated. Flight mode has been turned off by user";
                                        normalTerminatedText = "Flight mode has been turned off";
                                    }
                                    else{
                                        notificationTitle = "Turning Flight mode on by "+returnStringFromTextView();
                                        userTerminatedText = "Terminated. Flight mode has been turned on by user";
                                        normalTerminatedText = "Flight mode has been turned on";
                                    }
                                    final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Bleep").setContentText(Long.toString(millisUntilFinished / 1000)).setAutoCancel(false).build();
                                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mng.notify(0, notification);
                                }

                                @Override
                                public void onFinish() {
                                    Toast.makeText(getApplicationContext(), "Whatever, boss", Toast.LENGTH_LONG).show();
                                }
                            }.start();
                            break;

                        case "Hotspot":
                            ticker = new CountDownTimer(diff * 1000, 1000){
                                @Override
                                public void onTick(long millisUntilFinished){
                                    try{
                                        if((Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager) == 13) {
                                            notificationTitle = "Turning Hotspot off by "+returnStringFromTextView();
                                            userTerminatedText = "Terminated. Hotspot has been turned off by user";
                                            normalTerminatedText = "Hotspot has been turned off";
                                        }
                                        else{
                                            notificationTitle = "Turning Hotspot on by "+returnStringFromTextView();
                                            userTerminatedText = "Terminated. Hotspot has been turned on by user";
                                            normalTerminatedText = "Hotspot has been turned on";
                                        }
                                    }
                                    catch(Exception e){

                                    }
                                    final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Bleep").setContentText(Long.toString(millisUntilFinished / 1000)).setAutoCancel(false).build();
                                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mng.notify(0, notification);
                                }

                                @Override
                                public void onFinish() {
                                    Toast.makeText(getApplicationContext(), "Whatever, boss", Toast.LENGTH_LONG).show();
                                }
                            }.start();
                            break;

                        case "Data Conn.":
                            ticker = new CountDownTimer(diff * 1000, 1000){
                                @Override
                                public void onTick(long millisUntilFinished){
                                    if(((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
                                        notificationTitle = "Turning Data Connection off by "+returnStringFromTextView();
                                        userTerminatedText = "Terminated. Data Connection has been turned off by user";
                                        normalTerminatedText = "Data Connection has been turned off";
                                    }
                                    else{
                                        notificationTitle = "Turning Data Connection on by "+returnStringFromTextView();
                                        userTerminatedText = "Terminated. Data Connection has been turned on by user";
                                        normalTerminatedText = "Data Connection has been turned on";
                                    }
                                    final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Bleep").setContentText(Long.toString(millisUntilFinished / 1000)).setAutoCancel(false).build();
                                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mng.notify(0, notification);
                                }

                                @Override
                                public void onFinish() {
                                    Toast.makeText(getApplicationContext(), "Whatever, boss", Toast.LENGTH_LONG).show();
                                }
                            }.start();
                            break;

                    }

                    Toast.makeText(getApplicationContext(), "Successful!!!", Toast.LENGTH_LONG).show();

                    
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