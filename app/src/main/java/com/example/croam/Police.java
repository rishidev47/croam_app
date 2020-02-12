
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


public class Police extends Fragment {

    public static String allmobilenumberofpolice11="";
    static String str2_="";
    String latitude="",longitude="";
    private String[] placeName;

    TextView json_textview;
    Button getpolicestationsbtn;
    TextView intro;
    public String policenames="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_police, container, false);
        final BottomNavigationView navView=((MainActivity)getActivity()).navView;
        navView.setBackgroundColor(getResources().getColor(R.color.white));

//        if(((MainActivity)getActivity()).isOn){
//            view.setBackgroundColor(getResources().getColor(R.color.light_green));
//        }
//        else {
//            view.setBackgroundColor(getResources().getColor(R.color.light_red));
//        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle b1=getArguments();
        latitude=b1.getString("lat");
        longitude= b1.getString("long");
        json_textview= (TextView)view.findViewById(R.id.policedetails);
        intro=(TextView)view.findViewById(R.id.intro);
        getpolicestationsbtn=(Button)view.findViewById(R.id.policegetdetailbtn);
        getpolicestationsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetPolice(getActivity()).execute();
            }
        });
//        new GetPolice(getActivity()).execute();


        /*
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    StringBuilder content1 = new StringBuilder();
                    String phoneurl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + "ChIJPUCONxLiDDkRoJVWyuYkDjI" + "&fields=name,rating,formatted_phone_number&key=AIzaSyDW4eROan5nUX9HxHhTo_ntwqinJCZeoAI";
                    URL url = new URL(phoneurl);
                    URLConnection urlConnection = url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        content1.append(line + "\n");
                    }
                    String phone = content1.toString();
                    System.out.println("PHONE = " + phone);
                }
                catch(Exception e){
                    System.out.println("ERROR...");
                }
            }
        });
        thread.start();*/


        /*
        newdetail(latitude,longitude);

        final View view1=view;

        progressDialog=new ProgressDialog(getActivity().getApplicationContext());
        Button btn=(Button)view.findViewById(R.id.error);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=new ProgressDialog(getActivity());
                progressDialog.setMessage("Retreiving data..."+"\n"+"Please wait for a moment...");
                progressDialog.show();
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if(str2_.equalsIgnoreCase("")){
                            Toast.makeText(getActivity(),"Network issue"+"\n"+"Please try again!",Toast.LENGTH_LONG).show();
                        }
                        else {
                            TextView json_textview = (TextView) view1.findViewById(R.id.policedetails);
                            json_textview.setText(str2_);
                        }
                    }
                },8000);

            }
        });

        */

    }

    class GetPolice extends AsyncTask<Void, Void, Void>{
        public Context context;
        ProgressDialog progressDialog;

        public GetPolice(Context context) {
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
                    content.append(line + "\n");
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
