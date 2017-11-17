package com.dabinu.apps.skeda;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import java.util.ArrayList;


public class Display extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    AdapterForExpandableListView foreignAdapter;
    ArrayList<Group> listToPaste;
    ExpandableListView theListViewGanGan;




    public int[] sortAllConnectivityIcons(){

        int[] icons = new int[5];

        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.getWifiState() == 1) {
                    icons[0] = R.drawable.wifioff;
                }
                else{
                    icons[0] = R.drawable.wifion;
                }



        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter.isEnabled()){
                    icons[1] = R.drawable.blueon;
                }
                else{
                    icons[1] = R.drawable.blueoff;
                }



        if(Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1){
                    icons[2] = R.drawable.flighton;
                }
                else if (Settings.System.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 1) {
                    icons[2] = R.drawable.flightoff;
                }



        try{
                final int hotspotState = (Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager);

                if(hotspotState == 13) {
                    icons[3] = R.drawable.hotspoton;
                }
                else{
                    icons[3] = R.drawable.hotspotoff;
                }
            }
        catch(Exception e){

        }



        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (isConnected){
            icons[4] = R.drawable.dataon;
            }
        else{
            icons[4] = R.drawable.dataoff;
            }



                return icons;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        overridePendingTransition(0, 0);


        theListViewGanGan = (ExpandableListView) findViewById(R.id.expansionSlot);
        listToPaste = doTheArrangement();
        foreignAdapter = new AdapterForExpandableListView(Display.this, listToPaste);
        theListViewGanGan.setAdapter(foreignAdapter);

        theListViewGanGan.expandGroup(0);
        theListViewGanGan.expandGroup(1);
        theListViewGanGan.expandGroup(2);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }




    private ArrayList<Group> doTheArrangement(){

        ArrayList<Group> list = new ArrayList<>();
        int allImages[] = sortAllConnectivityIcons();



        Group commGroup = new Group();
        commGroup.setName("Communication");

        ArrayList<Child> settingsChild = new ArrayList<>();

        Child ch0 = new Child();
        ch0.setName("Calls");
        ch0.setImage(R.drawable.clls);
        settingsChild.add(ch0);

        Child ch1 = new Child();
        ch1.setName("SMS");
        ch1.setImage(R.drawable.esemes);
        settingsChild.add(ch1);

        commGroup.setItems(settingsChild);
        list.add(commGroup);




        Group connGroup = new Group();
        connGroup.setName("Connectivity");

        ArrayList<Child> commChild = new ArrayList<>();

        Child omo0 = new Child();
        omo0.setName("WiFi");
        omo0.setImage(allImages[0]);
        commChild.add(omo0);

        Child omo1 = new Child();
        omo1.setName("Bluetooth");
        omo1.setImage(allImages[1]);
        commChild.add(omo1);

        Child omo2 = new Child();
        omo2.setName("Flight mode");
        omo2.setImage(allImages[2]);
        commChild.add(omo2);

        Child omo3 = new Child();
        omo3.setName("Hotspot");
        omo3.setImage(allImages[3]);
        commChild.add(omo3);

        Child omo4 = new Child();
        omo4.setName("Data Connection");
        omo4.setImage(allImages[4]);
        commChild.add(omo4);

        connGroup.setItems(commChild);
        list.add(connGroup);





        Group netGroup = new Group();
        netGroup.setName("Phone settings");

        ArrayList<Child> netChild = new ArrayList<>();

        Child aligo0 = new Child();
        aligo0.setName("Power");
        netChild.add(aligo0);

        Child aligo1 = new Child();
        aligo1.setName("Battery saver");
        netChild.add(aligo1);

        Child aligo2 = new Child();
        aligo1.setName("Silent mode");
        netChild.add(aligo1);

        netGroup.setItems(netChild);
        list.add(netGroup);

        return list;
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you wantsb to exit?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory( Intent.CATEGORY_HOME );
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.display, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}