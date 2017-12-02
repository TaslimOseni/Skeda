package com.dabinu.apps.skeda.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.adapters.AdapterForSystem;
import com.dabinu.apps.skeda.templates.SystemTemplate;

import java.util.ArrayList;
import java.util.List;


public class SystemFragment extends Fragment {


    public SystemFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_system, container, false);

        GridView greedy = v.findViewById(R.id.theGreedyView);

        List<SystemTemplate> listed = new ArrayList<>();
        listed.add(new SystemTemplate("Power", R.drawable.power));
        listed.add(new SystemTemplate("Profiles", R.drawable.silent));
        listed.add(new SystemTemplate("Battery saver", R.drawable.bsaver));
        listed.add(new SystemTemplate("Reminder", R.drawable.reminder));

        AdapterForSystem adapterForSystem = new AdapterForSystem(listed, getContext().getApplicationContext());
        greedy.setAdapter(adapterForSystem);

        return v;
    }

}
