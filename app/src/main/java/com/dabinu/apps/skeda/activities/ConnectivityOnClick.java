package com.dabinu.apps.skeda.activities;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.broadcasts.MyBroadcastReceiver;
import com.dabinu.apps.skeda.utilities.TimePicker;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ConnectivityOnClick extends AppCompatActivity{


    WifiManager wifiManager;
    NotificationManager mng;
    AlarmManager alarmManager;
    Context context;
    CountDownTimer ticker;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allnetworks);

        context = this;

        ArrayAdapter tod = ArrayAdapter.createFromResource(this, R.array.today, android.R.layout.simple_spinner_item);
        tod.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        ((Spinner) findViewById(R.id.today)).setAdapter(tod);
        ((Spinner) findViewById(R.id.today)).setSelection(0);


        final Button process = findViewById(R.id.chooseTime);


        ((CheckBox) findViewById(R.id.shouldIVibrate)).setChecked(true);
        ((CheckBox) findViewById(R.id.shouldIRing)).setChecked(false);


        final String leadString = getIntent().getStringExtra("NAME").trim();
        ((TextView) findViewById(R.id.nameOfCarrierIntent)).setText(leadString);
        ((Switch) findViewById(R.id.state)).setChecked(checkState(context, leadString));
        ((TextView) findViewById(R.id.turnonofftext)).setText(String.format("Turn %s by:", stringReturner(checkState(context, leadString))));


        process.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

        (findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        findViewById(R.id.ahead).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(process.getText().toString().equals("Choose time")){
                    Toast.makeText(getApplicationContext(), "Choose a valid time", Toast.LENGTH_LONG).show();
                }
                else{
                    final long diff = convertTimeStringsToTime(process.getText().toString().trim()) - convertTimeStringsToTime(new SimpleDateFormat("hh:mm:ss a").format(new Date()).trim());

                    if(diff <= 0){
                        Toast.makeText(getApplicationContext(), String.format("It's already past %s, choose another time", process.getText().toString()), Toast.LENGTH_LONG).show();
                    }
                    else{
                        new AlertDialog.Builder(context)
                                .setMessage(String.format("Turn %s %s %s by %s?", leadString, stringReturner(checkState(context, leadString)), "today", process.getText().toString().trim()))
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id){
                                        implementCountDown(leadString, process.getText().toString().trim(), diff);
                                        startActivity(new Intent(getApplicationContext(), FirstActivity.class));
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();

                    }


                }
            }
        });

    }


    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), FirstActivity.class));
    }


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



        else if(newRaw[9] == 'P' || newRaw[9] == 'p'){
            if(rawFormOfTime[0] == '1' && rawFormOfTime[1] == '2'){
                result = (12 * 3600) + (Integer.parseInt(Character.toString(rawFormOfTime[3]).concat(Character.toString(rawFormOfTime[4]))) * 60) + (Integer.parseInt(Character.toString(newRaw[6]).concat(Character.toString(newRaw[7]))));
            }
            else{
                result = ((Integer.parseInt(Character.toString(rawFormOfTime[0]).concat(Character.toString(rawFormOfTime[1]))) + 12) * 3600) + (Integer.parseInt(Character.toString(rawFormOfTime[3]).concat(Character.toString(rawFormOfTime[4]))) * 60) + (Integer.parseInt(Character.toString(newRaw[6]).concat(Character.toString(newRaw[7]))));
            }

        }

        return result;
    }


    public void showTimePickerDialog(View v){
        DialogFragment timeFragment = new TimePicker();
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public void implementCountDown(final String name, String timeToExplode, long diff){

        mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        final boolean currentState = checkState(context, name);

        final String notificationTitle = String.format("Turning %s %s by %s", name, stringReturner(currentState), timeToExplode);

        Bundle bundle = new Bundle();
        bundle.putString("NAME", name);
        bundle.putBoolean("TASK", !checkState(context, name));

        final Intent intent = new Intent(this, MyBroadcastReceiver.class);
        intent.putExtras(bundle);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), idSetter(name), intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (diff * 1000), pendingIntent);


        ticker = new CountDownTimer(diff * 1000, 1000){
            @Override
            public void onTick(long millisUntilFinished){

                Notification notification = new NotificationCompat.Builder(getApplicationContext()).setPriority(idSetter(name) - 2).setContentTitle(notificationTitle).setSmallIcon(R.drawable.notif_icon).setColor(getResources().getColor(R.color.white)).setContentText("").setAutoCancel(false).build();
                mng.notify(idSetter(name), notification);
//
//                if(currentState != checkState(name)){
//                    mng.cancel(idSetter(name));
//                    alarmManager.cancel(PendingIntent.getBroadcast(getApplicationContext(), idSetter(name), intent, 0));
//
//                    Bundle bundl = new Bundle();
//                    bundl.putString("NAME", name);
//                    bundl.putBoolean("TASK", checkState(name));
//
//                    Intent inten = new Intent(getApplicationContext(), TerminatedBroadcastReceiver.class);
//                    inten.putExtras(bundl);
//                    PendingIntent pendingIntention = PendingIntent.getBroadcast(getApplicationContext(), idSetter(name) + 100, inten, 0);
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, 1000, pendingIntention);
//
//                    ticker.onFinish();
//                }
            }

            @Override
            public void onFinish(){
                mng.cancel(idSetter(name));
            }
        }.start();

    }


    public String stringReturner(boolean bool){
        if(bool){
            return "off";
        }
        return "on";
    }


    public boolean checkState(Context context, String name){
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        switch(name){
            case "WiFi":
                return wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
            case "Bluetooth":
                return bluetoothAdapter.isEnabled();
            case "Flight mode":
                return Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
            case "Location":
                return false;
            case "Hotspot":
                try{
                    return (Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager) == 13;
                }
                catch(Exception e){
                    return false;
                }
            case "Data Conn.":
                return ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
        }
        return false;
    }


    public void settingSetter(Context context, String name, boolean changeTo){
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        switch(name){
            case "WiFi":
                wifiManager.setWifiEnabled(changeTo);
                break;
            case "Bluetooth":
                if(changeTo){
                    bluetoothAdapter.enable();
                }
                else{
                    bluetoothAdapter.disable();
                }
                break;
            case "Flight mode":
                if(changeTo){
                    Settings.System.putInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 1);
                }
                else{
                    Settings.System.putInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
                }
                break;
            case "Location":
                break;
            case "Hotspot":
                try{
                    if(!changeTo){
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
            case "Data Conn.":
                ConnectivityManager conman = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                try{
                    Method setMobileDataEnabledMethod = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
                    setMobileDataEnabledMethod.setAccessible(true);
                    setMobileDataEnabledMethod.invoke(conman, !(((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()));
                }
                catch(Exception e){
                }

        }

        ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(1200);
    }


    public int idSetter(String name){
        switch(name){
            case "WiFi":
                return 0;
            case "Bluetooth":
                return 1;
            case "Flight mode":
                return 2;
            case "Location":
                return 3;
            case "Hotspot":
                return 0;
            case "Data Conn.":
                return 4;
        }
        return 98;
    }


    public String returnDoneString(String name, boolean state){
        return String.format("%s has been turned %s", name, stringReturner(!state));
    }
    //todo: fix fixed notification!
}