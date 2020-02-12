//package com.example.croam;
//
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.List;
//
//public class policetemp extends AppCompatActivity {
//
//    private String[] placeName;
//    TextView policestations_textview;
//    Button btn;
//    String str2;
//
//    String placeid[]=new String[5];
//    int c=0;
//    ProgressDialog progressDialog;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_police_station);
//        newdetail(getIntent().getStringExtra("lat"),getIntent().getStringExtra("long"));
//
//
//
//        progressDialog=new ProgressDialog(this);
//        Button btn=(Button)findViewById(R.id.policestationbtn1);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressDialog.setMessage("Retreiving information..."+"\n"+"Wait a moment...");
//
//                progressDialog.show();
//                Handler handler=new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressDialog.dismiss();
//                        System.out.println("SETTING");
//                        TextView json_textview=(TextView)findViewById(R.id.json_textview1);
//                        json_textview.setText(str2);
//                    }
//                },10000);
//
//            }
//        });
//
//
//
//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    StringBuilder content1 = new StringBuilder();
//                    String phoneurl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + "ChIJPUCONxLiDDkRoJVWyuYkDjI" + "&fields=name,rating,formatted_phone_number&key=AIzaSyDW4eROan5nUX9HxHhTo_ntwqinJCZeoAI";
//                    URL url = new URL(phoneurl);
//                    URLConnection urlConnection = url.openConnection();
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8);
//                    String line;
//                    while ((line = bufferedReader.readLine()) != null) {
//                        content1.append(line + "\n");
//                    }
//                    String phone = content1.toString();
//                    System.out.println("PHONE = " + phone);
//                }
//                catch(Exception e){
//                    System.out.println("ERROR...");
//                }
//            }
//        });
//        thread.start();
//
//        /*btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressDialog.show();
//                Handler handler=new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressDialog.dismiss();
//                    }
//                },5000);
//            }
//        });
//*/
//
//    }
//
//   /* public void getPolicStations(){
//        System.out.println("A");
//        PlacesService service = new PlacesService("AIzaSyAzX52Sv2TH5rvGYIQxLzRL7aZNwFqBzjM");
//        System.out.println("B");
//        List<Place> findPlaces = service.findPlaces(22.625071, 88.329514, "atm");
//        System.out.println("C");
//        placeName = new String[findPlaces.size()];
//        System.out.println("D");
//        String str1=service.makeUrl(1.1,1.1,"help");
//        System.out.println("E");
//        json_textview.setText(service.getJSON(str1));
//        System.out.println("F");
//        for (int i = 0; i < findPlaces.size(); i++) {
//
//            Place placeDetail = findPlaces.get(i);
//
//            System.out.println(placeDetail.getName());
//            placeName[i] = placeDetail.getName();
//
//            String str=policestations_textview.getText()+placeDetail.getName()+"\n";
//            policestations_textview.setText(str);
//        }
//        System.out.println("G");
//    }*/
//    public void newdetail(String latitude,String longitude){
//        //final String theUrl="https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJbc01iWKd-DkRcrY5mj1JlGk&fields=name,rating,formatted_phone_number,formatted_address,geometry,review&key=AIzaSyDW4eROan5nUX9HxHhTo_ntwqinJCZeoAI";
//        final String theUrl="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=5000&types=police&key=AIzaSyDW4eROan5nUX9HxHhTo_ntwqinJCZeoAI";
//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//        try {
//
//            StringBuilder content=new StringBuilder();
//            URL url = new URL(theUrl);
//            URLConnection urlConnection = url.openConnection();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8);
//            String line;
//            while ((line = bufferedReader.readLine()) != null)
//            {
//                content.append(line + "\n");
//            }
//
//            bufferedReader.close();
//            str2=content.toString();
//            System.out.println("RESONSE="+str2);
//
//
//
//        }
//
//
//
//
//        catch (Exception e)
//        {
//
//            e.printStackTrace();
//
//        }
//
//            }
//        });
//
//        thread.start();
//    }
//    public void setmessage(String str){
//
//    }
//}
//
