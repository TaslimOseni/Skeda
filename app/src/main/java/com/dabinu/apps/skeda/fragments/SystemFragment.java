package com.dabinu.apps.skeda.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.activities.Silento;


public class SystemFragment extends Fragment {


    public SystemFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_system, container, false);

        TextView power = v.findViewById(R.id.power);
        TextView bSaver = v.findViewById(R.id.bSaver);
        TextView reminder = v.findViewById(R.id.reminder);
        TextView silento = v.findViewById(R.id.silento);

        silento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext().getApplicationContext(), Silento.class));
            }
        });


        return v;
    }

}
