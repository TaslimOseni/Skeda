package com.dabinu.apps.skeda;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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




        Group settingsGroup = new Group();
        settingsGroup.setName("Settings");

        ArrayList<Child> settingsChild = new ArrayList<>();

        Child ch0 = new Child();
        ch0.setName("Calls");
        settingsChild.add(ch0);

        Child ch1 = new Child();
        ch1.setName("SMS");
        settingsChild.add(ch1);

        settingsGroup.setItems(settingsChild);
        list.add(settingsGroup);




        Group commGroup = new Group();
        commGroup.setName("Communication");

        ArrayList<Child> commChild = new ArrayList<>();

        Child omo0 = new Child();
        omo0.setName("WiFi");
        commChild.add(omo0);

        Child omo1 = new Child();
        omo1.setName("Bluetooth");
        commChild.add(omo1);

        Child omo2 = new Child();
        omo2.setName("Flight mode");
        commChild.add(omo2);

        Child omo3 = new Child();
        omo3.setName("Hotspot");
        commChild.add(omo3);

        Child omo4 = new Child();
        omo4.setName("Data Connection");
        commChild.add(omo4);

        commGroup.setItems(commChild);
        list.add(commGroup);





        Group netGroup = new Group();
        netGroup.setName("Networks");

        ArrayList<Child> netChild = new ArrayList<>();

        Child aligo0 = new Child();
        aligo0.setName("Power");
        netChild.add(aligo0);

        Child aligo1 = new Child();
        aligo1.setName("Battery saver");
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
        } else {
            super.onBackPressed();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}