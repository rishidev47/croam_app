package com.example.croam;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlacesService {

    private String API_KEY;

    public PlacesService(String apikey) {
        this.API_KEY = apikey;
    }

    public void setApiKey(String apikey) {
        this.API_KEY = apikey;
    }

    public List<Place> findPlaces(double latitude, double longitude, String temp) {
        String urlString = makeUrl(latitude, longitude);
        try {
            String json = getJSON(urlString);
            System.out.println(json);
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("results");
            ArrayList<Place> arrayList = new ArrayList<Place>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    Place place = Place.jsonToPontoReferencia((JSONObject) array.get(i));
                    if(i==0){
                        System.out.println("DETAIL");
                        System.out.println("RESPONSE= "+getDetail("91312341d9f619dd478c763ee23b856a1d1d8523"));
                    }
                    //System.out.println("RESPONSE= "+getDetail(place.getId()));
                    Log.v("Places Services ", ""+place);
                    arrayList.add(place);
                } catch (Exception e) {
                }
            }
            return arrayList;
        } catch (JSONException ex) {
            Logger.getLogger(PlacesService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getDetail(String id){
        String url="https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJbc01iWKd-DkRcrY5mj1JlGk&fields=name,rating,formatted_phone_number,formatted_address,geometry,review&key=AIzaSyB5A7N_tKnjwSdsmRinYaOVLbAOana_A9s";
        //String url="https://maps.googleapis.com/maps/api/place/details/json?plceid="+id+"&fields=name,rating,formatted_phone_number,formatted_address,geometry,review&key=AIzaSyDW4eROan5nUX9HxHhTo_ntwqinJCZeoAI";
        String response=getJSON(url);
        return response;
    }

    public String makeUrl(double latitude, double longitude) {
        StringBuilder urlString = new StringBuilder("https://maps.googleapis.com/maps/api/place/search/json?");
        urlString.append("&location=");
        urlString.append(latitude);
        urlString.append(",");
        urlString.append(longitude);
        urlString.append("&radius=10000");
        urlString.append("&types=police");
        urlString.append("&key=" + API_KEY);

        return urlString.toString();
    }

    public String getJSON(String url) {
        return getUrlContents(url);
    }

    public String getUrlContents(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8);
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }
}