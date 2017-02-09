package com.apps.mustango.atmfinder;

import org.json.JSONArray;

/**
 * Created by mustango on 06.02.2017.
 */

public class Atminfo {

    private String city;
    private String address;
    private String devices;


    public String getCity(){
        return city;
    }

    public String getDevices(){
        return devices;
    }

    public String getAddress() {
        return address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDevices(String devices) {
        this.devices = devices;
    }
}
