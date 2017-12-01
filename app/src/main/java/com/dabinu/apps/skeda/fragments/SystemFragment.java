package com.dabinu.apps.skeda.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.activities.Silento;


public class SystemFragment extends Fragment {


    public SystemFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_system, container, false);

        GridView greedy = v.findViewById(R.id.theGreedyView);

        return v;
    }

}
