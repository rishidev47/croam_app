
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//public class Police extends Fragment {
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_police, container, false);
//    }
//}

package com.example.croam;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Police extends Fragment {

    private static String allmobilenumberofpolice11="";
    private static String str2_="";
    private String latitude="",longitude="";
    private String[] placeName;

    private TextView json_textview;
    private Button getpolicestationsbtn;
    private TextView intro;
    private String policenames="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_police, container, false);
        final BottomNavigationView navView=((MainActivity) Objects.requireNonNull(getActivity())).navView;
        navView.setBackgroundColor(getResources().getColor(R.color.white));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle b1=getArguments();
        if (b1 != null) {
            latitude=b1.getString("lat");
            longitude= b1.getString("long");
        }
        json_textview= (TextView)view.findViewById(R.id.policedetails);
        intro=(TextView)view.findViewById(R.id.intro);
        getpolicestationsbtn=(Button)view.findViewById(R.id.policegetdetailbtn);
        getpolicestationsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetPolice(getActivity()).execute();
            }
        });

    }

    class GetPolice extends AsyncTask<Void, Void, Void>{
        public Context context;
        ProgressDialog progressDialog;

        GetPolice(Context context) {
            this.context=context;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();

            String str="Nearby Police Stations in a radius of 4000m";
            intro.setText(str);

            System.out.println("RESONSE="+str2_);
            System.out.println("PARAMETERS=");
            try {
                JSONObject object=new JSONObject(str2_);
                JSONArray ja = object.getJSONArray("results");
                System.out.println(ja.length());

                for(int iter=0; iter<ja.length(); iter++){
                    JSONObject nameobj=ja.getJSONObject(iter);
                    final String name=nameobj.getString("name");
                    System.out.println(iter+"="+name);
                    policenames+=name+"\n";
                    final String plcaeid=nameobj.getString("place_id");
                    final String phoneurl="https://maps.googleapis.com/maps/api/place/details/json?placeid=" + plcaeid + "&fields=name,rating,formatted_phone_number&key=AIzaSyB5A7N_tKnjwSdsmRinYaOVLbAOana_A9s";
                    Thread newthread=new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                StringBuilder content1 = new StringBuilder();

                                URL url = new URL(phoneurl);
                                URLConnection urlConnection = url.openConnection();
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8);
                                String line;
                                while ((line = bufferedReader.readLine()) != null) {
                                    content1.append(line + "\n");
                                }
                                String idresponse = content1.toString();
                                System.out.println(idresponse);
                                System.out.println("==");
                                JSONObject idobj=new JSONObject(idresponse);
                                try {
                                    String phonedata2="";
                                    String phonedata1="";
                                    phonedata1 = idobj.getJSONObject("result").getString("name");
                                    phonedata2 = idobj.getJSONObject("result").getString("formatted_phone_number");
                                    System.out.println(phonedata1);
                                    System.out.println(phonedata2);
                                    if(!(phonedata2.equals(""))) {
                                        System.out.println("NAME="+name);
                                        String tempstr="abcd";
                                        allmobilenumberofpolice11 += phonedata2 + "\n";
                                        System.out.println("LIST=: "+allmobilenumberofpolice11);
                                    }
                                }
                                catch (Exception e){
                                    System.out.println("No Number of this Police Station found");
                                }
                                System.out.println("--");

                            }
                            catch(Exception e){
                                System.out.println("ERROR...");
                            }
                        }
                    });
                    newthread.start();
                }
            } catch (Exception e){

            }
            json_textview.setText(policenames);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Bundle b1=getArguments();
            latitude=b1.getString("lat");
            longitude= b1.getString("long");

//            progressDialog = new ProgressDialog(context);
//            progressDialog.setCancelable(false);
//            progressDialog.setTitle("Retreiving data....");
//            progressDialog.setMessage("Please wait for a moment...");
//            progressDialog.isIndeterminate();
//
//            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... args) {
            try {
                final String theUrl="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=3000&types=police&key=AIzaSyB5A7N_tKnjwSdsmRinYaOVLbAOana_A9s";
                StringBuilder content=new StringBuilder();
                URL url = new URL(theUrl);
                URLConnection urlConnection = url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8);
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line).append("\n");
                }

                bufferedReader.close();
                str2_=content.toString();
                System.out.println(str2_);



            }
            catch (Exception e){

            }
            return null;
        }
    }


    public void newdetail(String latitude,String longitude){
        //final String theUrl="https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJbc01iWKd-DkRcrY5mj1JlGk&fields=name,rating,formatted_phone_number,formatted_address,geometry,review&key=AIzaSyDW4eROan5nUX9HxHhTo_ntwqinJCZeoAI";
        final String theUrl="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=5000&types=police&key=AIzaSyB5A7N_tKnjwSdsmRinYaOVLbAOana_A9s";
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder content=new StringBuilder();
                    URL url = new URL(theUrl);
                    URLConnection urlConnection = url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8);
                    String line;
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        content.append(line + "\n");
                    }

                    bufferedReader.close();
                    str2_=content.toString();
                    System.out.println("RESONSE="+str2_);

                }
                catch (Exception e)
                {

                    e.printStackTrace();

                }

            }
        });

        thread.start();
    }

    public String getJson(String latitude,String longitude){
        return null;
    }
}
