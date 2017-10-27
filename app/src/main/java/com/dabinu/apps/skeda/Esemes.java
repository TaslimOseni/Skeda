package com.dabinu.apps.skeda;



import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;



public class Esemes extends AppCompatActivity {

    private EditText text, number;
//    private Button process, choose;
    Spinner today;
    ArrayAdapter tod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esemes);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        text = (EditText) findViewById(R.id.text);
        number = (EditText) findViewById(R.id.numb);
//        process = (Button) findViewById(R.id.process);
        today = (Spinner) findViewById(R.id.today);
        tod = ArrayAdapter.createFromResource(this, R.array.today, android.R.layout.simple_spinner_item);
        today.setAdapter(tod);

//        process.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                String texts = text.getText().toString().trim();
//                String numbers = number.getText().toString().trim();
//
//                try{
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage(numbers, null, texts, null, null);
//                    Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
//                    }
//                catch (Exception ex) {
//                    Toast.makeText(getApplicationContext(),ex.toString(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });

//        choose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                showTimePickerDialog(v);
//            }
//        });
    }




    public void showTimePickerDialog(View v) {
        DialogFragment timeFragment = new TimePicker();
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }





    public String returnStringFromTextView(Button button){
        char[] rawData = button.getText().toString().trim().toCharArray();
        String ourStandardTime = "";

        if(rawData[6] == 'f'){
            for(int i = 12; i < rawData.length; i++){
                ourStandardTime += (Character.toString(rawData[i]));
            }
        }


        else if(rawData[6] == 'n'){
            for(int i = 11; i < rawData.length; i++){
                ourStandardTime += (Character.toString(rawData[i]));
            }
        }

        return ourStandardTime.trim();
    }
}
