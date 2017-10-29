package com.dabinu.apps.skeda;



import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Esemes extends AppCompatActivity{

    private EditText text, number;
    ImageButton cancel, ahead;
    private Button process;
    Spinner today;
    ArrayAdapter tod;
    String notificationTitle = "", normalTerminatedText = "", failedText = "";
    CountDownTimer ticker;
    String actualText = "", actualNumber = "";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esemes);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        cancel = (ImageButton) findViewById(R.id.cancel);
        ahead = (ImageButton)findViewById(R.id.ahead);
        text = (EditText) findViewById(R.id.text);
        number = (EditText) findViewById(R.id.numb);
        process = (Button) findViewById(R.id.chooseTime);


        today = (Spinner) findViewById(R.id.today);
        tod = ArrayAdapter.createFromResource(this, R.array.today, android.R.layout.simple_spinner_item);
        tod.setDropDownViewResource(R.layout.spin);
        today.setAdapter(tod);


        final Intent moonWalkIntent = new Intent(this, Display.class);


        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(moonWalkIntent);
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(text.getText().toString().trim().equals("Text")){
                    text.setText("");
                }
            }
        });

        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(number.getText().toString().trim().equals("Number")){
                    number.setText("");
                }
            }
        });

        ahead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                actualText = text.getText().toString().trim();
                actualNumber = number.getText().toString().trim();


                if(process.getText().toString().equals("Choose time")){
                    Toast.makeText(getApplicationContext(), "Choose a valid time", Toast.LENGTH_LONG).show();
                }
                else{
                    String currentTime = new SimpleDateFormat("hh:mm a").format(new Date());

                    long diff = convertTimeStringsToTime(process.getText().toString().trim()) - convertTimeStringsToTime(currentTime);

                    if(convertTimeStringsToTime(process.getText().toString().trim()) < convertTimeStringsToTime(currentTime)){
                        Toast.makeText(getApplicationContext(), "Sorry.. Can't go back in time.", Toast.LENGTH_LONG).show();
                    }
                    else if(convertTimeStringsToTime(process.getText().toString().trim()) == convertTimeStringsToTime(currentTime)){
                        Toast.makeText(getApplication(), String.format("It's already %s!", process.getText().toString()), Toast.LENGTH_LONG).show();
                    }
                    else{
                        notificationTitle = "Sending SMS by "+process.getText().toString().trim();
                        normalTerminatedText = "Text message sent to "+ actualNumber;
                        failedText = "Message not sent";

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
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(actualNumber, null, actualText, null, null);
                                    final Notification finalNotif = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(normalTerminatedText).setContentText("").setAutoCancel(true).build();
                                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mng.notify(0, finalNotif);
                                }
                                catch (Exception ex){
                                    final Notification finalNotiff = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(failedText).setContentText("").setAutoCancel(true).build();
                                    NotificationManager mngr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mngr.notify(0, finalNotiff);
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




    public void showTimePickerDialog(View v){
        DialogFragment timeFragment = new TimePicker();
        timeFragment.setCancelable(true);
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }





    @Override
    public void onBackPressed(){
        cancel.performClick();
    }




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


}