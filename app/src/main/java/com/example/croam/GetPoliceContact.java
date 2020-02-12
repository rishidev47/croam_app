package com.example.croam;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static android.support.constraint.Constraints.TAG;

public class GetPoliceContact extends Thread {
    double lat;
    double lng;
    String API_KEY;
    String policeList=null;
    String policeDetails="";
    MainActivity activity=null;
    public GetPoliceContact(MainActivity activity, double lat, double lng) {
        this.lat=lat;
        this.lng=lng;
        this.activity=activity;
        API_KEY=MainActivity.API_KEY;
    }

    @Override
    public void run() {
        try {
            final String theUrl="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius=3000&types=police&key="+API_KEY;
            StringBuilder content=new StringBuilder();
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
            policeList=content.toString();
            System.out.println("Police List "+policeList);
        }
        catch (Exception e){
            System.out.println(e);
        }

        if(policeList!=null){
            try {
                JSONObject object=new JSONObject(policeList);
                JSONArray ja = object.getJSONArray("results");
                System.out.println(ja.length());
                String name;
                String plcaeid;
                String phoneurl;
                for(int iter=0; iter<ja.length(); iter++){
                    JSONObject nameobj=ja.getJSONObject(iter);
                    name=nameobj.getString("name");
                    plcaeid=nameobj.getString("place_id");
                    phoneurl="https://maps.googleapis.com/maps/api/place/details/json?placeid=" + plcaeid + "&fields=name,rating,formatted_phone_number&key="+API_KEY;
//                    Thread newthread=new Thread(new Runnable() {
//                        @Override
//                        public void run() {

                            try {
                                StringBuilder content1 = new StringBuilder();
                                URL url = new URL(phoneurl);
                                URLConnection urlConnection = url.openConnection();
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                                String line;
                                while ((line = bufferedReader.readLine()) != null) {
                                    content1.append(line + "\n");
                                }
                                String idresponse = content1.toString();
                                System.out.println("Getting number "+idresponse);
                                JSONObject idobj=new JSONObject(idresponse);
                                String policeName = idobj.getJSONObject("result").getString("name");
                                String policeNumber=null;
                                try{
                                    idobj.getJSONObject("result").getString("formatted_phone_number");
                                }
                                catch (JSONException e){
                                    Log.v(TAG, name+" No Number of this Police Station found", e);
                                    continue;
                                }

                                policeNumber = idobj.getJSONObject("result").getString("formatted_phone_number");
                                policeDetails=policeDetails+policeName+" "+policeNumber+"\n";
                                Log.d("GET POLICE", "GetPolice"+policeDetails);

                            }
                            catch(Exception e){
                                System.out.println(e+" ERROR...");
                            }
//                        }
//                    });
//                    newthread.start();
//                    newthread.join();
                }
            } catch (Exception e){

            }
        }
        MainActivity.policeDetails=policeDetails;
//        activity.doThis();

    }
}
