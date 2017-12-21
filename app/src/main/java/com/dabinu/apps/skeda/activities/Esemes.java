package com.dabinu.apps.skeda.activities;



import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.utilities.TimePicker;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Esemes extends AppCompatActivity{

    private EditText text, number;
    private Button process;
    CountDownTimer ticker;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esemes);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final Context context = this;

        ArrayAdapter tod = ArrayAdapter.createFromResource(this, R.array.today, android.R.layout.simple_spinner_item);
        tod.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        ((Spinner) findViewById(R.id.today)).setAdapter(tod);
        ((Spinner) findViewById(R.id.today)).setSelection(0);

        ((CheckBox) findViewById(R.id.shouldIVibrate)).setChecked(true);

        text = findViewById(R.id.text);
        number = findViewById(R.id.numb);
        process = findViewById(R.id.chooseTime);

        number.setSelectAllOnFocus(true);
        text.setSelectAllOnFocus(true);

        process.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });


        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });


        findViewById(R.id.ahead).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                final String actualText = text.getText().toString().trim();
                final String actualNumber = number.getText().toString().trim();


                if(process.getText().toString().equals("Choose time")){
                    Toast.makeText(getApplicationContext(), "Choose a valid time", Toast.LENGTH_LONG).show();
                }
                else if(!(android.util.Patterns.PHONE.matcher(actualNumber).matches())){
                    Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_LONG).show();
                }
                else if(actualText.equals("")){
                    Toast.makeText(getApplicationContext(), "Text can't be empty", Toast.LENGTH_LONG).show();
                }
                else{
                    final long diff = convertTimeStringsToTime(process.getText().toString().trim()) - convertTimeStringsToTime(new SimpleDateFormat("hh:mm:ss a").format(new Date()));

                    if(diff <= 0){
                        Toast.makeText(getApplicationContext(), "Sorry.. Can't go back in time.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        new AlertDialog.Builder(context)
                                .setMessage(String.format(String.format("Send SMS to %s %s by %s?", actualNumber, "today", process.getText().toString().trim())))
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id){
                                        sendSMS(actualText, actualNumber, process.getText().toString().trim(), diff);
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


//    public static boolean sendSMS(Context ctx, int simID, String toNum, String centerNum, String smsText, PendingIntent sentIntent, PendingIntent deliveryIntent) {
//        String name;
//
//        try {
//            if (simID == 0) {
//                name = "isms0";
//            } else if (simID == 1) {
//                name = "isms1";
//            } else {
//                throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
//            }
//
//            try
//            {
//                Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", new Class[]{String.class});
//                method.setAccessible(true);
//                Object param = method.invoke(null, new Object[]{name});
//                if (param == null)
//                {
//                    throw new RuntimeException("can not get service which is named '" + name + "'");
//                }
//                method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", new Class[]{IBinder.class});
//                method.setAccessible(true);
//                Object stubObj = method.invoke(null, new Object[]{param});
//                method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
//                method.invoke(stubObj, ctx.getPackageName(), toNum, centerNum, smsText, sentIntent, deliveryIntent);
//            } catch (ClassNotFoundException e)
//            {
//                throw new RuntimeException(e);
//            } catch (NoSuchMethodException e)
//            {
//                throw new RuntimeException(e);
//            } catch (InvocationTargetException e)
//            {
//                throw new RuntimeException(e);
//            } catch (IllegalAccessException e)
//            {
//                throw new RuntimeException(e);
//            }
//
//            return true;
//        } catch (ClassNotFoundException e) {
//            Log.e("Exception", "ClassNotFoundException:" + e.getMessage());
//        } catch (NoSuchMethodException e) {
//            Log.e("Exception", "NoSuchMethodException:" + e.getMessage());
//        } catch (InvocationTargetException e) {
//            Log.e("Exception", "InvocationTargetException:" + e.getMessage());
//        } catch (IllegalAccessException e) {
//            Log.e("Exception", "IllegalAccessException:" + e.getMessage());
//        } catch (Exception e) {
//            Log.e("Exception", "Exception:" + e);
//        }
//        return false;
//    }


    public void sendSMS(final String text, final String phoneNumber, String timeToExplode, long diff){
        final String notificationTitle = "Sending SMS by "+process.getText().toString().trim();
        final String normalTerminatedText = "Text message sent to "+ phoneNumber;
        final String failedText = "Message not sent";

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
                    smsManager.sendTextMessage(phoneNumber, null, text, null, null);
                    final Notification finalNotif = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(normalTerminatedText).setContentText("").setAutoCancel(true).build();
                    NotificationManager mng = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mng.notify(0, finalNotif);
                    if(((CheckBox) findViewById(R.id.shouldIVibrate)).isChecked()){
                        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(2000);
                    }
                }
                catch(Exception ex){
                    final Notification finalNotiff = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(failedText).setContentText("").setAutoCancel(true).build();
                    NotificationManager mngr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mngr.notify(0, finalNotiff);
                    if(((CheckBox) findViewById(R.id.shouldIVibrate)).isChecked()){
                        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(1200);
                    }
                }


            }
        }.start();


    }


}