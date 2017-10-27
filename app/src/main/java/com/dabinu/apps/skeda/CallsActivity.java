package com.dabinu.apps.skeda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CallsActivity extends AppCompatActivity {

    EditText number;
    Button process, choose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls);

        number = (EditText) findViewById(R.id.numb);
        process = (Button) findViewById(R.id.process);
        choose = (Button) findViewById(R.id.choose);

        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

            }
        });


    }
}
