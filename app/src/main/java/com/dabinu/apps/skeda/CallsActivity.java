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

public class CallsActivity extends AppCompatActivity {

    EditText number;
    Button process, choose;
    ImageButton cancel, ahead;
    Spinner today;
    ArrayAdapter tod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        today = (Spinner) findViewById(R.id.today);
        tod = ArrayAdapter.createFromResource(this, R.array.today, android.R.layout.simple_spinner_item);
        tod.setDropDownViewResource(R.layout.spin);
        today.setAdapter(tod);


        final Intent moonWalkIntent = new Intent(this, Display.class);


        number = (EditText) findViewById(R.id.numb);
        process = (Button) findViewById(R.id.chooseTime);
        cancel = (ImageButton) findViewById(R.id.cancel);
        ahead = (ImageButton)findViewById(R.id.ahead);

        process.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });




        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(moonWalkIntent);
            }
        });

        ahead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });


    }


    public void showTimePickerDialog(View v) {
        DialogFragment timeFragment = new TimePicker();
        timeFragment.setCancelable(true);
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }


    @Override
    public void onBackPressed(){
        cancel.performClick();
    }

}
