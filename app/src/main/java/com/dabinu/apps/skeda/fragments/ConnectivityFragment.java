package com.dabinu.apps.skeda.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.adapters.AdapterForConnectivity;
import com.dabinu.apps.skeda.templates.ConnectivityRally;
import java.util.ArrayList;
import java.util.List;


public class ConnectivityFragment extends Fragment {

    RecyclerView recyclerView;

    AdapterForConnectivity adapterForConnectivity;
    List<ConnectivityRally> listOfStuff = new ArrayList<>();

    public ConnectivityFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_connectivity, container, false);

        recyclerView = v.findViewById(R.id.recycler);

        listOfStuff.add(new ConnectivityRally("Taslim", true));
        listOfStuff.add(new ConnectivityRally("Tassfglim", true));
        listOfStuff.add(new ConnectivityRally("Taim", true));
        listOfStuff.add(new ConnectivityRally("Tasim", true));

        adapterForConnectivity = new AdapterForConnectivity(listOfStuff);

        recyclerView.setAdapter(adapterForConnectivity);

        return v;
    }

}
