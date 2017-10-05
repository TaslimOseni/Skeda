package com.dabinu.apps.skeda;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Inner extends AppCompatActivity{


    TextView head, turner, stateText;
    Spinner hour, minute, ampm;
    ArrayAdapter<CharSequence> hourAdapt, minAdapt, ampmAdapt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);


        head = (TextView) findViewById(R.id.nameOfCarrierIntent);
        turner = (TextView) findViewById(R.id.turner);
        stateText = (TextView) findViewById(R.id.stateText);
        hour = (Spinner) findViewById(R.id.hourSpin);
        minute = (Spinner) findViewById(R.id.minSpin);
        ampm = (Spinner) findViewById(R.id.ampmSpin);


        head.setText(getIntent().getStringExtra("NAME"));
        turner.setText(R.string.turnOnString);
        stateText.setText(R.string.onString);

        hourAdapt = ArrayAdapter.createFromResource(this, R.array.hour, R.layout.timespinner);
        minAdapt = ArrayAdapter.createFromResource(this, R.array.minute, R.layout.timespinner);
        ampmAdapt = ArrayAdapter.createFromResource(this, R.array.apm, R.layout.timespinner);
        hour.setAdapter(hourAdapt);
        minute.setAdapter(minAdapt);
        ampm.setAdapter(ampmAdapt);





    }
}
