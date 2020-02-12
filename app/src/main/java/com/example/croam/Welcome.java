//package com.example.croam;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.PorterDuff;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.ProgressBar;
//
////import net.steamcrafted.loadtoast.LoadToast;
//
//public class Welcome extends AppCompatActivity {
//    ProgressBar progressBar;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_welcome);
//
//
//
//        progressBar=(ProgressBar)findViewById(R.id.progress_circular);
//
//        Thread splash=new Thread(){
//            public void run(){
//                try{
//                    sleep(3000);
//
//                    Intent i=new Intent(getBaseContext(),Login.class);
//                    startActivity(i);
//                    finish();
//                }catch (Exception e){
//
//                }
//            }
//        };
//        splash.start();
//
//
//    }
//}
