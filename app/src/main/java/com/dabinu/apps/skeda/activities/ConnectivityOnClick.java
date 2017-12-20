package com.dabinu.apps.skeda.activities;


import android.app.Notification;
import android.app.NotificationManager;
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
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.utilities.TimePicker;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ConnectivityOnClick extends AppCompatActivity{


    Button process;
    CountDownTimer ticker;
    boolean happyEnding = true;
    ArrayAdapter tod;
    RelativeLayout wando;
    CheckBox showWando, vibrate, ring;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allnetworks);

        final Context context = this;

        tod = ArrayAdapter.createFromResource(this, R.array.today, android.R.layout.simple_spinner_item);
        tod.setDropDownViewResource(R.layout.for_spinner);
        ((Spinner) findViewById(R.id.today)).setAdapter(tod);


        process = findViewById(R.id.chooseTime);
        wando = findViewById(R.id.wando);
        showWando = findViewById(R.id.showWando);
        vibrate = findViewById(R.id.shouldIVibrate);
        ring = findViewById(R.id.shouldIRing);


        vibrate.setChecked(true);
        showWando.setChecked(true);
        showWando.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    wando.setVisibility(View.VISIBLE);
                }
                else{
                    wando.setVisibility(View.GONE);
                }
            }
        });



        final String leadString = getIntent().getStringExtra("NAME").trim();
        ((TextView) findViewById(R.id.nameOfCarrierIntent)).setText(leadString);
        ((Switch) findViewById(R.id.state)).setChecked(checkState(leadString));
        ((TextView) findViewById(R.id.turnonofftext)).setText(String.format("Turn %s by:", stringReturner(checkState(leadString))));


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
                                .setMessage(String.format("Turn %s %s %s by %s?", leadString, stringReturner(checkState(leadString)), "today", process.getText().toString().trim()))
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
        final boolean currentState = checkState(name);


        final String notificationTitle = String.format("Turning %s %s by %s", name, stringReturner(currentState), timeToExplode);
        final String userTerminatedText = String.format("Terminated. %s has been turned %s by user", name, stringReturner(currentState));
        final String normalTerminatedText = String.format("%s has been turned %s", name, stringReturner(currentState));

        ticker = new CountDownTimer(diff * 1000, 1000){
            @Override
            public void onTick(long millisUntilFinished){

                android.support.v4.app.NotificationCompat.InboxStyle extra = new android.support.v4.app.NotificationCompat.InboxStyle();
                extra.setBigContentTitle("Click to dismiss");
                final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setStyle(extra).setContentTitle(notificationTitle).setSmallIcon(R.drawable.notif_icon).setColor(getResources().getColor(R.color.white)).setContentText("").setAutoCancel(false).build();
                NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mng.notify(0, notification);

                if(currentState != checkState(name)){
                    happyEnding = false;
                    ticker.cancel();
                    mng.cancel(0);
                    ticker.onFinish();
                }
            }

            @Override
            public void onFinish(){
                if(happyEnding){
                    final Notification finalNotif = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.imgg).setContentTitle(normalTerminatedText).setContentText("").setAutoCancel(true).build();
                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mng.notify(0, finalNotif);
                    settingSetter(name, !currentState);
                    ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(2000);
                }
                else{
                    final Notification finalNotiff = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(userTerminatedText).setContentText("").setAutoCancel(true).build();
                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mng.notify(0, finalNotiff);
                    ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(1200);
                }
            }
        }.start();

    }


    public String stringReturner(boolean bool){
        if(bool){
            return "off";
        }
        return "on";
    }


    public boolean checkState(String name){
        final WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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


    public void settingSetter(String name, boolean changeTo){
        final WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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
    }


}