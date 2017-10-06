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
        Button process = (Button) getActivity().findViewById(R.id.process);
        TextView getOnOff = (TextView) getActivity().findViewById(R.id.turner);
        String onoff = getOnOff.getText().toString().replace(':', ' ');
        process.setVisibility(View.VISIBLE);
        process.setText(String.format("%s%s:%s?", onoff, timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
    }
}