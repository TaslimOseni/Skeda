package com.dabinu.apps.skeda.templates;


import com.dabinu.apps.skeda.R;

public class ConnectivityTemplate {

    private String name;
    private boolean state;


    public ConnectivityTemplate(String name, boolean state) {
        this.name = name;
        this.state = state;
    }


    public String getName() {
        return name;
    }

    public int getImage() {
        switch(name){
            case "WiFi":
                return R.drawable.wifi;
            case "Bluetooth":
                return R.drawable.bluetooth;
            case "Flight mode":
                return R.drawable.airplane;
            case "Hotspot":
                return R.drawable.hotspot;
            case "Data Conn.":
                return R.drawable.data;
        }
        return R.drawable.ahead;
    }

    public boolean isState() {
        return state;
    }
}
