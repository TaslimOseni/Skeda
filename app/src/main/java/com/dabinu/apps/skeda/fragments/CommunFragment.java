package com.dabinu.apps.skeda.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.activities.CallsActivity;
import com.dabinu.apps.skeda.activities.Esemes;


public class CommunFragment extends Fragment {


    public CommunFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_commun, container, false);

//        GridView gv = v.findViewById(R.id.greedy);
//        ListAdapter gadapt = ArrayAdapter.createFromResource(getContext().getApplicationContext(), R.array.today, R.layout.greedy_layout);
//        gv.setAdapter(gadapt);

        ImageView call = v.findViewById(R.id.calls);
        ImageView sms = v.findViewById(R.id.esemess);

        call.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext().getApplicationContext(), CallsActivity.class));
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext().getApplicationContext(), Esemes.class));
            }
        });
        return v;
    }

}
