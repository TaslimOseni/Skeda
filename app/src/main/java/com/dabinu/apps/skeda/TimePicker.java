package com.dabinu.apps.skeda;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
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
    public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
        // Do something with the time chosen by the user
//        TextView tv1=(TextView) getActivity().findViewById(R.id.textView1);
//        tv1.setText("Hour: "+view.getCurrentHour()+" Minute: "+view.getCurrentMinute());
    }
}