package com.dabinu.apps.skeda.fragments;


import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.adapters.AdapterForConnectivity;
import com.dabinu.apps.skeda.templates.ConnectivityTemplate;
import java.util.ArrayList;
import java.util.List;


public class ConnectivityFragment extends Fragment {

    RecyclerView recyclerView;

    AdapterForConnectivity adapterForConnectivity;
    List<ConnectivityTemplate> listOfStuff = new ArrayList<>();

    public ConnectivityFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_connectivity, container, false);

        recyclerView = v.findViewById(R.id.recycler);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        boolean leke = false;
        try{
            leke = (Integer) ((WifiManager) this.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getClass().getMethod("getWifiApState").invoke(this.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE)) == 13;
        }
        catch(Exception e){
            leke = false;
        }

        //todo: how dare you disappear on me all the fucking time.. Uhn! I know I'm never going to see you again.


        listOfStuff.add(new ConnectivityTemplate("WiFi", ((WifiManager) this.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getWifiState() == WifiManager.WIFI_STATE_ENABLED));
        listOfStuff.add(new ConnectivityTemplate("Bluetooth", (BluetoothAdapter.getDefaultAdapter()).isEnabled()));
        listOfStuff.add(new ConnectivityTemplate("Flight mode", Settings.System.getInt(getContext().getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1));
        //Location doesn't work from android 4.4
        listOfStuff.add(new ConnectivityTemplate("Location", false));
        listOfStuff.add(new ConnectivityTemplate("Hotspot", leke));
        listOfStuff.add(new ConnectivityTemplate("Data Conn.", ((ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && ((ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()));

        adapterForConnectivity = new AdapterForConnectivity(getContext().getApplicationContext(), listOfStuff);

        recyclerView.setAdapter(adapterForConnectivity);

        return v;
    }

}
