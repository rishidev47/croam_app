package com.example.croam;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class PoliceContacts {

    private Map<Location, String> contacts;
    private Context mContext;

    PoliceContacts(Context context){
        mContext=context;
        contacts = new HashMap<>();

        Location Delhi = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Delhi,"123");

        Location Haryana = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Haryana,"123");

        Location JK = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(JK,"123");

        Location UP = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(UP,"123");

        Location MP = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(MP,"123");

        Location Gujarat = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Gujarat,"123");

        Location Rajasthan = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Rajasthan,"123");

        Location TamilNadu = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(TamilNadu,"123");

        Location Kerala = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Kerala,"123");

        Location AndhraPradesh = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(AndhraPradesh,"123");

        Location Maharashtra = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Maharashtra,"123");

        Location Chattisgarh = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Chattisgarh,"123");

        Location Uttrakhand = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Uttrakhand,"123");

        Location Himachal = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Himachal,"123");

        Location Punjab = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Punjab,"123");

        Location Assam = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Assam,"123");

        Location ArunachalPradesh = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(ArunachalPradesh,"123");

        Location Sikkim = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Sikkim,"123");

        Location Karnatka = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Karnatka,"123");

        Location Goa = new Location("");
        Delhi.setLatitude(12);
        Delhi.setLongitude(13);
        contacts.put(Goa,"123");

    }


    String getPoliceContact(){

        final Location current = new Location("");
        SingleShotLocationProvider.requestSingleUpdate(mContext,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(
                            SingleShotLocationProvider.GPSCoordinates location) {
                            current.setLatitude(location.latitude);
                            current.setLongitude(location.longitude);
                    }
                });

        double min = 1e18;
        Location nearest= new Location("");
        for(Location k: contacts.keySet()){
            Log.e("dist",String.valueOf(current.distanceTo(k)));
            if(current.distanceTo(k)<min){
                nearest=k;
                min = current.distanceTo(k);
            }
        }
        return contacts.get(nearest);

    }
}

