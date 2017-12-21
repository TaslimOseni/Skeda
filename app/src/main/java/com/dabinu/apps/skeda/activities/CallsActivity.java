package com.dabinu.apps.skeda.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.utilities.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CallsActivity extends AppCompatActivity {

    EditText number;
    Button process;
    ImageButton cancel;
    FloatingActionButton ahead;
    Spinner today;
    ArrayAdapter tod;
    String actualNumber,  notificationTitle = "", normalTerminatedText = "", failedText = "";
    CountDownTimer ticker;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        today = findViewById(R.id.today);
        tod = ArrayAdapter.createFromResource(this, R.array.today, android.R.layout.simple_spinner_item);
        tod.setDropDownViewResource(R.layout.for_spinner);
        today.setAdapter(tod);

        context = this;


        number = findViewById(R.id.numb);
        process = findViewById(R.id.chooseTime);
        cancel = findViewById(R.id.cancel);
        ahead = findViewById(R.id.ahead);

        number.setSelectAllOnFocus(true);

        number.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(number.getText().toString().trim().equals("Number")){
                    number.setText("");
                }
            }
        });



        process.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ahead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                actualNumber = number.getText().toString().trim();

                if(process.getText().toString().equals("Choose time")){
                    Toast.makeText(getApplicationContext(), "Choose a valid time", Toast.LENGTH_LONG).show();
                }

                else if(!(android.util.Patterns.PHONE.matcher(actualNumber).matches())){
                    Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_LONG).show();
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
                        notificationTitle = "Call scheduled for "+ process.getText().toString().trim();
                        normalTerminatedText = String.format("Call to %s successful!", actualNumber);
                        failedText = "Can't make call";

                        ticker = new CountDownTimer(diff * 1000, 1000){
                            @Override
                            public void onTick(long millisUntilFinished){

                                final Notification notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(notificationTitle).setContentText("").setAutoCancel(false).build();
                                NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                mng.notify(0, notification);
                            }

                            @Override
                            public void onFinish(){
                                try{
                                    new AlertDialog.Builder(context)
                                            .setMessage(String.format("Call %s now?", actualNumber))
                                            .setCancelable(true)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                                                public void onClick(DialogInterface dialog, int id){
                                                    try{
                                                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel: "+ number.getText().toString())));
                                                    }
                                                    catch(SecurityException e){
                                                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                                    }
                                                    catch(Exception e){
                                                        Toast.makeText(getApplicationContext(), "Faied still", Toast.LENGTH_LONG).show();
                                                    }

                                                    final Notification finalNotif = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(normalTerminatedText).setContentText("").setAutoCancel(true).build();
                                                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    mng.notify(0, finalNotif);
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    final Notification finalNotiff = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(failedText).setContentText("").setAutoCancel(true).build();
                                                    NotificationManager mngr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    mngr.notify(0, finalNotiff);
                                                }
                                            })
                                            .show();

                                }
                                catch (Exception ex){
                                    final Notification finalNotiff = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(failedText).setContentText("").setAutoCancel(true).build();
                                    NotificationManager mngr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mngr.notify(0, finalNotiff);
                                }


                            }
                        }.start();


                        Toast.makeText(getApplicationContext(), "Successful!!!", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(getApplicationContext(), FirstActivity.class));
                    }



                }
            }
        });


    }


    public void showTimePickerDialog(View v) {
        DialogFragment timeFragment = new TimePicker();
        timeFragment.setCancelable(true);
        timeFragment.show(getSupportFragmentManager(), "timePicker");
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


}