package com.dabinu.apps.skeda;


import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


public class InnerNetwork extends AppCompatActivity{


    TextView head, turner;
    ImageButton cancel;
    FloatingActionButton ahead;
    Button process;
    Switch stateSwitch;
    CountDownTimer ticker;
    String notificationTitle = "", userTerminatedText = "", normalTerminatedText = "";
    boolean terminator, happyEnding = true;
    Spinner today;
    ArrayAdapter tod;
    Vibrator vibrator;




    public long convertTimeStringsToTime(String timeFormattedString){
        long result = 0;
        char rawFormOfTime[] = timeFormattedString.trim().toCharArray();
        char[] newRaw = new char[11];

        if(timeFormattedString.length() == 8){
            for(int i = 0; i < 5; i++){
                newRaw[i] = rawFormOfTime[i];
            }
            newRaw[5] = ':';
            newRaw[6] = '0';
            newRaw[7] = '0';

            for(int j = 5; j < 8; j++){
                newRaw[j + 3] = rawFormOfTime[j];
            }
        }

        else{
            newRaw = timeFormattedString.toCharArray();
        }



        if(newRaw[9] == 'A' || newRaw[9] == 'a'){
            if(newRaw[0] == '1' && newRaw[1] == '2'){
                result = (Integer.parseInt(Character.toString(newRaw[3]).concat(Character.toString(newRaw[4]))) * 60) + (Integer.parseInt(Character.toString(newRaw[6]).concat(Character.toString(newRaw[7]))));
            }
            else{
                result = (Integer.parseInt(Character.toString(newRaw[0]).concat(Character.toString(newRaw[1]))) * 3600) + (Integer.parseInt(Character.toString(newRaw[3]).concat(Character.toString(newRaw[4]))) * 60) + (Integer.parseInt(Character.toString(newRaw[6]).concat(Character.toString(newRaw[7]))));
            }
        }



        else if(rawFormOfTime[6] == 'P' || rawFormOfTime[6] == 'p'){
            if(rawFormOfTime[0] == '1' && rawFormOfTime[1] == '2'){
                result = (12 * 3600) + (Integer.parseInt(Character.toString(rawFormOfTime[3]).concat(Character.toString(rawFormOfTime[4]))) * 60) + (Integer.parseInt(Character.toString(newRaw[6]).concat(Character.toString(newRaw[7]))));
            }
            else{
                result = ((Integer.parseInt(Character.toString(rawFormOfTime[0]).concat(Character.toString(rawFormOfTime[1]))) + 12) * 3600) + (Integer.parseInt(Character.toString(rawFormOfTime[3]).concat(Character.toString(rawFormOfTime[4]))) * 60) + (Integer.parseInt(Character.toString(newRaw[6]).concat(Character.toString(newRaw[7]))));
            }

        }

        return result;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allnetworks);

        overridePendingTransition(0, 0);


        final WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        today = findViewById(R.id.today);
        tod = ArrayAdapter.createFromResource(this, R.array.today, android.R.layout.simple_spinner_item);
        tod.setDropDownViewResource(R.layout.spin);
        today.setAdapter(tod);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        head = findViewById(R.id.nameOfCarrierIntent);
        turner = findViewById(R.id.turnonofftext);
        cancel = findViewById(R.id.cancel);
        ahead = findViewById(R.id.ahead);
        stateSwitch = findViewById(R.id.state);
        process = findViewById(R.id.chooseTime);



        String leadString = getIntent().getStringExtra("NAME");


        switch(leadString){
            case "WiFi":
                head.setText(leadString);
                if(wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED){
                    turner.setText(R.string.turnOnString);
                    stateSwitch.setChecked(false);
                }
                else{
                    turner.setText(R.string.turnOffString);
                    stateSwitch.setChecked(true);
                }
                break;


            case "Bluetooth":
                head.setText(leadString);
                if(bluetoothAdapter.isEnabled()){
                    turner.setText(R.string.turnOffString);
                    stateSwitch.setChecked(true);
                }
                else{
                    turner.setText(R.string.turnOnString);
                    stateSwitch.setChecked(false);
                }
                break;


            case "Flight mode":
                head.setText(leadString);
                if(Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1){
                    turner.setText(R.string.turnOffString);
                    stateSwitch.setChecked(true);
                }
                else{
                    turner.setText(R.string.turnOnString);
                    stateSwitch.setChecked(false);
                }
                break;


            case "Hotspot":
                head.setText(leadString);
                try{
                    if((Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager) == 13){
                        turner.setText(R.string.turnOffString);
                        stateSwitch.setChecked(true);
                    }
                    else{
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
                    turner.setText(R.string.turnOffString);
                    stateSwitch.setChecked(true);
                }
                else{
                    turner.setText(R.string.turnOnString);
                    stateSwitch.setChecked(false);
                }
        }



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(getApplicationContext(), Display.class);
                startActivity(goBack);
            }
        });


        ahead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(process.getText().toString().equals("Choose time")){
                    Toast.makeText(getApplicationContext(), "Choose a valid time", Toast.LENGTH_LONG).show();
                }
                else{
                    String currentTime = new SimpleDateFormat("hh:mm:ss a").format(new Date());

                    long diff = convertTimeStringsToTime(process.getText().toString().trim()) - convertTimeStringsToTime(currentTime);

                    if(convertTimeStringsToTime(process.getText().toString().trim()) < convertTimeStringsToTime(currentTime)){
                        Toast.makeText(getApplicationContext(), "Sorry.. Can't go back in time.", Toast.LENGTH_LONG).show();
                    }
                    else if(convertTimeStringsToTime(process.getText().toString().trim()) == convertTimeStringsToTime(currentTime)){
                        Toast.makeText(getApplication(), String.format("It's already %s!", process.getText().toString()), Toast.LENGTH_LONG).show();
                    }
                    else{
                        switch(head.getText().toString().trim()){


                            //WiFi......................................................................


                            case "WiFi":
                                if(wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED){
                                    notificationTitle = "Turning WiFi on by "+ process.getText().toString().trim();
                                    userTerminatedText = "Terminated. WiFi has been turned on by user";
                                    normalTerminatedText = "WiFi has been turned on";
                                    terminator = false;
                                }
                                else{
                                    notificationTitle = "Turning WiFi off by "+ process.getText().toString().trim();
                                    userTerminatedText = "Terminated. WiFi has been turned off by user";
                                    normalTerminatedText = "WiFi has been turned off";
                                    terminator = true;
                                }
                                ticker = new CountDownTimer(diff * 1000, 1000){
                                    @Override
                                    public void onTick(long millisUntilFinished){

                                        android.support.v4.app.NotificationCompat.InboxStyle extra = new android.support.v4.app.NotificationCompat.InboxStyle();
                                        extra.setBigContentTitle("Click to dismiss");
                                        final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setStyle(extra).setContentTitle(notificationTitle).setContentText("").setAutoCancel(false).build();
                                        NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        mng.notify(0, notification);

                                        if(terminator){
                                            if(!(terminator && wifiManager.getWifiState() != WifiManager.WIFI_STATE_DISABLED)){
                                                happyEnding = false;
                                                ticker.cancel();
                                                mng.cancel(0);
                                                ticker.onFinish();
                                            }
                                        }
                                        else{
                                            if(!(!terminator && wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED)){
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
                                            if(wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED){
                                                wifiManager.setWifiEnabled(false);
                                            }
                                            else{
                                                wifiManager.setWifiEnabled(true);
                                            }
                                            vibrator.vibrate(2000);
                                        }
                                        else{
                                            vibrator.vibrate(1200);
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
                                    notificationTitle = "Turning Bluetooth off by "+ process.getText().toString().trim();
                                    userTerminatedText = "Terminated. Bluetooth has been turned off by user";
                                    normalTerminatedText = "Bluetooth has been turned off";
                                    terminator = true;
                                }
                                else{
                                    notificationTitle = "Turning Bluetooth on by "+ process.getText().toString().trim();
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
                                    public void onFinish(){
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
                                            vibrator.vibrate(2000);
                                        }
                                        else{
                                            vibrator.vibrate(1200);
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
                                    notificationTitle = "Turning Flight mode off by "+ process.getText().toString().trim();
                                    userTerminatedText = "Terminated. Flight mode has been turned off by user";
                                    normalTerminatedText = "Flight mode has been turned off";
                                    terminator = true;
                                }
                                else{
                                    notificationTitle = "Turning Flight mode on by "+ process.getText().toString().trim();
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
                                                Settings.System.putInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
                                            }
                                            else{
                                                Settings.System.putInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 1);
                                            }
                                            vibrator.vibrate(2000);
                                        }
                                        else{
                                            vibrator.vibrate(1200);
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
                                    if((Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager) == 13){
                                        notificationTitle = "Turning Hotspot off by "+ process.getText().toString().trim();
                                        userTerminatedText = "Terminated. Hotspot has been turned off by user";
                                        normalTerminatedText = "Hotspot has been turned off";
                                        terminator = true;
                                    }


                                    else{
                                        notificationTitle = "Turning Hotspot on by "+ process.getText().toString().trim();
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

                                            try{
                                                if((Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager) == 13){
                                                    WifiConfiguration wifiConfiguration = new WifiConfiguration();
                                                    wifiConfiguration.SSID = "Connect-to-me";
                                                    wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                                                    wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                                                    wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

                                                    try{
                                                        Method turnHotspotOn = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                                                        turnHotspotOn.invoke(wifiManager, wifiConfiguration, false);
                                                    }
                                                    catch(Exception e){

                                                    }

                                                }
                                                else{
                                                    if(wifiManager.isWifiEnabled()){
                                                        wifiManager.setWifiEnabled(false);
                                                    }
                                                    WifiConfiguration wifiConfiguration = new WifiConfiguration();
                                                    wifiConfiguration.SSID = "Connect-to-me";
                                                    wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                                                    wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                                                    wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

                                                    try{
                                                        Method turnHotspotOn = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                                                        turnHotspotOn.invoke(wifiManager, wifiConfiguration, true);
                                                    }
                                                    catch(Exception e){

                                                    }
                                                }
                                            }
                                            catch(Exception e){

                                            }

                                            vibrator.vibrate(2000);

                                        }
                                        else{
                                            vibrator.vibrate(1200);
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
                                    notificationTitle = "Turning Data Conn. off by "+ process.getText().toString().trim();
                                    userTerminatedText = "Terminated. Data Connection has been turned off by user";
                                    normalTerminatedText = "Data Conn. has been turned off";
                                    terminator = true;
                                }
                                else{
                                    notificationTitle = "Turning Data Conn. on by "+ process.getText().toString().trim();
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

                                            vibrator.vibrate(2000);
                                        }
                                        else{
                                            vibrator.vibrate(1200);
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
            }
        });



        process.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });


    }


    @Override
    public void onBackPressed(){
        cancel.performClick();
    }



    public void showTimePickerDialog(View v){
        DialogFragment timeFragment = new TimePicker();
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }


}