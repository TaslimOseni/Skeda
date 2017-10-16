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
import android.util.ArraySet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class Inner extends AppCompatActivity{


    TextView head, turner, stateText;
    ImageButton back;
    Button process, timeSelector;
    Switch stateSwitch;



    public long convertTimeStringsToTime(String timeFormattedString){
        long result = 0;

        char rawFormOfTime[] = timeFormattedString.trim().toCharArray();

        if(rawFormOfTime[6] == 'A' || rawFormOfTime[6] == 'a'){
            result = (Integer.parseInt(Character.toString(rawFormOfTime[0]).concat(Character.toString(rawFormOfTime[1]))) * 3600) + (Integer.parseInt(Character.toString(rawFormOfTime[3]).concat(Character.toString(rawFormOfTime[4]))) * 60);
        }
        else if(rawFormOfTime[6] == 'P' || rawFormOfTime[6] == 'p'){
            result = ((Integer.parseInt(Character.toString(rawFormOfTime[0]).concat(Character.toString(rawFormOfTime[1]))) + 12) * 3600) + (Integer.parseInt(Character.toString(rawFormOfTime[3]).concat(Character.toString(rawFormOfTime[4]))) * 60);
        }

        return result;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);

        overridePendingTransition(0, 0);


        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



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
                    final int hotspotState = (Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager);

                    if(hotspotState == 13) {
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
                ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

                if (isConnected){
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
                    Toast.makeText(getApplicationContext(), "Right time: "+ diff, Toast.LENGTH_LONG).show();
                }

//                CountDownTimer ticker = new CountDownTimer(5000, 1000){
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                        final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Bleep").setContentText(Long.toString(millisUntilFinished / 1000)).setAutoCancel(false).build();
//                        NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                        mng.notify(0, notification);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        Toast.makeText(getApplicationContext(), "Whatever, boss", Toast.LENGTH_LONG).show();
//                    }
//                }.start();
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