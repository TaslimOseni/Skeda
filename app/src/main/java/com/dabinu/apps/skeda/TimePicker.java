package com.dabinu.apps.skeda;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;


public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener{


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }



    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1){

        String ampm;
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();

        Button process = (Button) getActivity().findViewById(R.id.chooseTime);

        if(timePicker.getCurrentHour() >= 12){
            ampm = "PM";
            hour -= 12;
        }
        else{
            ampm = "AM";
        }

//        String onoff = getOnOff.getText().toString().replace(':', ' ');
//        process.setText(String.format("%s%02d:%02d %s", onoff, hour, min, ampm));

        process.setText(String.format("%02d:%02d %s", hour, min, ampm));

    }


}