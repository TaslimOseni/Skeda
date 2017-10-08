package com.dabinu.apps.skeda;


import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class Inner extends AppCompatActivity{


    TextView head, turner, stateText;
    ImageButton back;
    Button process, timeSelector;
    Switch stateSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);


        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



        head = (TextView) findViewById(R.id.nameOfCarrierIntent);
        turner = (TextView) findViewById(R.id.turner);
        stateText = (TextView) findViewById(R.id.stateText);
        back = (ImageButton) findViewById(R.id.back);
        stateSwitch = (Switch) findViewById(R.id.state);
        process = (Button) findViewById(R.id.process);
        timeSelector = (Button) findViewById(R.id.timeSelector);



        String leadString = getIntent().getStringExtra("NAME");


        switch(leadString){
            case "WiFi":
                head.setText(leadString);
                if(wifiManager.getWifiState() == 1){
                    stateText.setText(R.string.offString);
                    turner.setText(R.string.turnOnString);
                    stateSwitch.setChecked(false);
                }
                else{
                    stateText.setText(R.string.onString);
                    turner.setText(R.string.turnOffString);
                    stateSwitch.setChecked(true);
                }
                break;


            case "Bluetooth":
                head.setText(leadString);
                if(bluetoothAdapter.isEnabled()){
                    stateText.setText(R.string.onString);
                    turner.setText(R.string.turnOffString);
                    stateSwitch.setChecked(true);
                }
                else{
                    stateText.setText(R.string.offString);
                    turner.setText(R.string.turnOnString);
                    stateSwitch.setChecked(false);
                }
                break;


            case "Flight mode":
                head.setText(leadString);
                if(Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1) {
                    stateText.setText(R.string.onString);
                    turner.setText(R.string.turnOffString);
                    stateSwitch.setChecked(true);
                }
                else{
                    stateText.setText(R.string.offString);
                    turner.setText(R.string.turnOnString);
                    stateSwitch.setChecked(false);
                }
                break;
        }




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(getApplicationContext(), Display.class);
                startActivity(goBack);
            }
        });


        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processFeedBack();
            }
        });


        timeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });



    }


    @Override
    public void onBackPressed() {
        back.performClick();
    }


    public void showTimePickerDialog(View v) {
        DialogFragment timeFragment = new TimePicker();
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public void processFeedBack(){
        char[] rawData = process.getText().toString().toCharArray();


        String step1 = "";
        String step2 = "";

        if(rawData[6] == 'f'){
            for(int i = 12; i < rawData.length; i++){
                step1 += (Character.toString(rawData[i]));
                    }
            Toast.makeText(getApplicationContext(), step1, Toast.LENGTH_LONG).show();
                }



        else if(rawData[6] == 'n'){
            for(int i = 11; i < rawData.length; i++){
                step1 += (Character.toString(rawData[i]));
                    }
            Toast.makeText(getApplicationContext(), step1, Toast.LENGTH_LONG).show();
                }

    }


}
