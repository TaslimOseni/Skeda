package com.dabinu.apps.skeda.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.deleteSoon.Display;
import com.dabinu.apps.skeda.utilities.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Silento extends AppCompatActivity {


    TextView currentstate;
    ImageButton cancel;
    FloatingActionButton ahead;
    Button process;
    Switch stateSwitch;
    CountDownTimer ticker;
    String notificationTitle = "", userTerminatedText = "", normalTerminatedText = "", nameGuy = "";
    boolean terminator, happyEnding = true;
    Spinner today, mode;
    ArrayAdapter tod, mod;
    Vibrator vibrator;
    int picked = 0;



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
        setContentView(R.layout.activity_silento);

        today = findViewById(R.id.today);
        tod = ArrayAdapter.createFromResource(this, R.array.today, android.R.layout.simple_spinner_item);
        tod.setDropDownViewResource(R.layout.spin);
        today.setAdapter(tod);


        mode = findViewById(R.id.newMode);
        mod = ArrayAdapter.createFromResource(this, R.array.mode, android.R.layout.simple_spinner_item);
        mod.setDropDownViewResource(R.layout.spin);
        mode.setAdapter(mod);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        currentstate = findViewById(R.id.currentState);
        cancel = findViewById(R.id.cancel);
        ahead = findViewById(R.id.ahead);
        stateSwitch = findViewById(R.id.state);
        process = findViewById(R.id.chooseTime);


        final AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int md = am.getRingerMode();

        switch(md){
            case AudioManager.RINGER_MODE_NORMAL:
                currentstate.setText("Normal");
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                currentstate.setText("Vibration");
                break;
            case AudioManager.RINGER_MODE_SILENT:
                currentstate.setText("Silent");
                break;
        }


        mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        picked = AudioManager.RINGER_MODE_NORMAL;
                        nameGuy = "normal mode";
                        break;
                    case 1:
                        picked = AudioManager.RINGER_MODE_VIBRATE;
                        nameGuy = "vibration mode";
                        break;
                    case 2:
                        picked = AudioManager.RINGER_MODE_SILENT;
                        nameGuy = "silent mode";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                        notificationTitle = String.format("Turning %s on by %s", nameGuy, process.getText().toString().trim());
                        userTerminatedText = String.format("Terminated. %s has been turned on by user", nameGuy);
                        normalTerminatedText = String.format("%s has been turned on!", nameGuy);
                        terminator = true;

                        ticker = new CountDownTimer(diff * 1000, 1000){
                            @Override
                            public void onTick(long millisUntilFinished){

                                final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(notificationTitle).setContentText("").setAutoCancel(false).build();
                                NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                mng.notify(0, notification);

                                if(am.getRingerMode() == picked){
                                        happyEnding = false;
                                        ticker.cancel();
                                        mng.cancel(0);
                                        ticker.onFinish();
                                }
                            }

                            @Override
                            public void onFinish(){
                                if(happyEnding){
                                    final Notification finalNotif = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(normalTerminatedText).setContentText("").setAutoCancel(true).build();
                                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mng.notify(0, finalNotif);
                                    am.setRingerMode(picked);
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
