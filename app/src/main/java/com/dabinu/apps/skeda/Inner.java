package com.dabinu.apps.skeda;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Inner extends AppCompatActivity{


    TextView head, turner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);


        head = (TextView) findViewById(R.id.nameOfCarrierIntent);
        turner = (TextView) findViewById(R.id.turner);


        head.setText(getIntent().getStringExtra("NAME"));
        turner.setText(R.string.turnOnString);
    }
}
