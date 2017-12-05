package com.dabinu.apps.skeda.activities;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.utilities.TimePicker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Esemes extends AppCompatActivity{



    private EditText text, number;
    ImageButton cancel, gotoocontacts;
    private Button process;
    Spinner today;
    FloatingActionButton ahead;
    ArrayAdapter tod;
    String notificationTitle = "", normalTerminatedText = "", failedText = "", actualText = "", actualNumber = "";
    CountDownTimer ticker;
    CheckBox wando;
    RelativeLayout hider;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esemes);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        cancel = findViewById(R.id.cancel);
        ahead = findViewById(R.id.ahead);
        text = findViewById(R.id.text);
        number = findViewById(R.id.numb);
        process = findViewById(R.id.chooseTime);
        gotoocontacts = findViewById(R.id.goToContacts);
        hider = findViewById(R.id.wando);
        wando = findViewById(R.id.showWando);

        wando.setChecked(true);
        wando.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    hider.setVisibility(View.VISIBLE);
                }
                else{
                    hider.setVisibility(View.GONE);
                }
            }
        });


        number.setSelectAllOnFocus(true);
        text.setSelectAllOnFocus(true);


        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number.getText().toString().trim().equals("Number")){
                    number.setText("");
                }
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText().toString().trim().equals("Type message here...")){
                    text.setText("");
                }
            }
        });


        today = findViewById(R.id.today);
        tod = ArrayAdapter.createFromResource(this, R.array.today, android.R.layout.simple_spinner_item);
        tod.setDropDownViewResource(R.layout.for_spinner);
        today.setAdapter(tod);




        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
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
                                catch(Exception ex){
                                    final Notification finalNotiff = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(failedText).setContentText("").setAutoCancel(true).build();
                                    NotificationManager mngr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mngr.notify(0, finalNotiff);
                                }


                            }
                        }.start();


                        Toast.makeText(getApplicationContext(), "Successful!!!", Toast.LENGTH_LONG).show();

                        Intent kissTent = new Intent(getApplicationContext(), FirstActivity.class);
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
        super.onBackPressed();
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



    public static boolean sendSMS(Context ctx, int simID, String toNum, String centerNum, String smsText, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        String name;

        try {
            if (simID == 0) {
                name = "isms0";
            } else if (simID == 1) {
                name = "isms1";
            } else {
                throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
            }

            try
            {
                Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", new Class[]{String.class});
                method.setAccessible(true);
                Object param = method.invoke(null, new Object[]{name});
                if (param == null)
                {
                    throw new RuntimeException("can not get service which is named '" + name + "'");
                }
                method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", new Class[]{IBinder.class});
                method.setAccessible(true);
                Object stubObj = method.invoke(null, new Object[]{param});
                method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
                method.invoke(stubObj, ctx.getPackageName(), toNum, centerNum, smsText, sentIntent, deliveryIntent);
            } catch (ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e)
            {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e)
            {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }

            return true;
        } catch (ClassNotFoundException e) {
            Log.e("Exception", "ClassNotFoundException:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.e("Exception", "NoSuchMethodException:" + e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e("Exception", "InvocationTargetException:" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e("Exception", "IllegalAccessException:" + e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", "Exception:" + e);
        }
        return false;
    }

}