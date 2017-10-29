package com.dabinu.apps.skeda;



import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


public class Esemes extends AppCompatActivity{

    private EditText text, number;
    ImageButton cancel, ahead;
    private Button process;
    Spinner today;
    ArrayAdapter tod;


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

        ahead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String texts = text.getText().toString().trim();
                String numbers = number.getText().toString().trim();

                try{
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(numbers, null, texts, null, null);
                    Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                }
                catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),ex.toString(), Toast.LENGTH_LONG).show();
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
}
