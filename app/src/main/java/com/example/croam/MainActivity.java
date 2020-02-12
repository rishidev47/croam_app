//package com.example.croam;
//
//import android.os.Bundle;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.support.annotation.NonNull;
//import android.view.MenuItem;
//import android.widget.TextView;
//
//public class MainActivity extends AppCompatActivity {
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment = null;
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    fragment=new Home();
//                    break;
//                case R.id.navigation_police:
//                    fragment=new Police();
//                    break;
//                case R.id.navigation_contacts:
//                    fragment=new Contact();
//                    break;
//                case R.id.navigation_profile:
//                    fragment=new Profile();
//                    break;
//            }
//            return loadFragment(fragment);
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//
//        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
//        loadFragment(new Home());
//
//    }
//
//    private boolean loadFragment(Fragment fragment) {
//        //switching fragment
//        if (fragment != null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, fragment)
//                    .commit();
//            return true;
//        }
//        return false;
//    }
//
//}
//
//
//
//


package com.example.croam;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.Image;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_CALENDAR;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.support.constraint.Constraints.TAG;

public class MainActivity extends AppCompatActivity {
    public static boolean screenOff1=false;
    public static boolean screenOff2=false;
    public static boolean screenOn1=false;
    public static boolean screenOn2=false;

    public static boolean btnPress1=false;
    public static boolean btnPress2=false;
    public static boolean btnPress3=false;

    public static String policeDetails="";
    public static double lat;
    public static double lng;

    public static CountDownTimer timer;
    public static String API_KEY="AIzaSyB5A7N_tKnjwSdsmRinYaOVLbAOana_A9s";
    public static String allmobilenumberofpolice="";
    public static String str2="";
    public static String latituteField="";
    public static String longitudeField="";
    public boolean isOn;
    public static String url=null;
    Fragment fragment=null;
    public static String phone;
    private Thread recordingThread;
    private Thread recognitionThread;
    CRoamService croamService;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final String OUTPUT_SCORES_NAME = "output0";
    private static final String INPUT_DATA_NAME = "sequential_1_input";
    public final static String DEBUG_TAG = "MainActivity";
    private static final String clientID = "c26974dfd247815";
    private static final String MODEL_FILENAME = "file:///android_asset/tf_help_model10dB.pb";
    public static final String MyPREFERENCES = "MyPrefs_Anjaneya" ;

    private static final int SAMPLE_RATE = 16000;
    private static final int SAMPLE_DURATION_MS = 3000;
    private static final int RECORDING_LENGTH = (int) (SAMPLE_RATE * SAMPLE_DURATION_MS / 1000);
    private static final int REQUEST_RECORD_AUDIO = 13;
    public static final int PERMISSION_REQUEST_CODE=200;
    public static String[] PERMISSIONS=new String[]{RECORD_AUDIO, ACCESS_FINE_LOCATION, CAMERA,RECEIVE_SMS,SEND_SMS,WRITE_EXTERNAL_STORAGE};


    boolean shouldContinueRecognition = true;
    boolean shouldContinue = true;
    short[] recordingBuffer = new short[RECORDING_LENGTH];
    int recordingOffset = 0;

    private final ReentrantLock recordingBufferLock = new ReentrantLock();
    private TensorFlowInferenceInterface inferenceInterface;

    private static int recognitionResultLength = 5;
    private boolean[] recognitionResult = new boolean[recognitionResultLength];

    public String AddressURL;
    private Camera camera;
    private int cameraId = 0;

    private FusedLocationProviderClient clinent;

    public static DBHandler db;
    public static int noofemergencycontacts=0;
//    public native float[] nativeMFCC(double[] buffer);
    public  FloatingActionButton fab;
    public BottomNavigationView navView;
    String str3="";
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment=new Home();
                    break;
                case R.id.navigation_police:
                    fragment=new Police();
                    Bundle b=new Bundle();
                    Log.d("LATTS", "onNavigationItemSelected: "+latituteField);
                    Log.d("LATTS", "onNavigationItemSelected: "+longitudeField);
                    b.putString("lat",latituteField);
                    b.putString("long",longitudeField);
                    fragment.setArguments(b);

                    break;
                case R.id.navigation_contacts:
                    fragment=new Contact();
                    break;
                case R.id.navigation_profile:
                    fragment=new Profile();
                    break;
            }
            return loadFragment(fragment);
        }
    };

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Intent opened=getIntent();
//        if(opened.getBooleanExtra("OPENED_FROM_NOTIFICATION",false)){
//            Toast.makeText(getBaseContext(), "Resumed", Toast.LENGTH_SHORT).show();
//            if(opened.getBooleanExtra("IS_ON",false)){
//                Toast.makeText(getBaseContext(), "Resumed ON", Toast.LENGTH_SHORT).show();
//                isOn=true;
//                loadFragment(new Home());
//                startService();
//            }
//            else {
//                Toast.makeText(getBaseContext(), "Resumed OFF", Toast.LENGTH_SHORT).show();
//                isOn=false;
//                loadFragment(new Home());
//                stopService();
//            }
//        }
//
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getBooleanExtra("OPENED_FROM_NOTIFICATION",false)){
            Log.d(TAG, "onNewIntent: opened");
//            Toast.makeText(getBaseContext(), "Opened", Toast.LENGTH_SHORT).show();
            isOn=true;
            loadFragment(new Home());
//            if(opened.getBooleanExtra("IS_ON",false)){
//                Toast.makeText(getBaseContext(), "Resumed ON", Toast.LENGTH_SHORT).show();
//                isOn=true;
//                loadFragment(new Home());
//                startService();
//            }
//            else {
//                Toast.makeText(getBaseContext(), "Resumed OFF", Toast.LENGTH_SHORT).show();
//                isOn=false;
//                loadFragment(new Home());
//                stopService();
//            }
        }

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(isMyServiceRunning(CRoamService.class)){
            isOn=true;
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        navView.setBackgroundColor(getColor(R.color.light_red));
        loadFragment(new Home());

        db=new DBHandler(getApplicationContext());
        noofemergencycontacts=db.noofemergencycontacts();
        inferenceInterface = new TensorFlowInferenceInterface(getAssets(), MODEL_FILENAME);
        clinent = LocationServices.getFusedLocationProviderClient(this);

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        registerReceiver((new MyBroadCastReciever(this)),filter);
        requestAllPermissions1();

        croamService=new CRoamService();
        if ((ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            return;
        }

        timer=new CountDownTimer(1500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                    btnPress1=false;
                    btnPress2=false;
            }
        };

        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
                cameraId = findCamera();
                if (cameraId < 0) {
                    Toast.makeText(this, "No front facing camera found."+ cameraId,
                            Toast.LENGTH_LONG).show();
                    Log.v(DEBUG_TAG, "Camera not OK");
                } else {

                    if(camera!=null)camera = Camera.open(cameraId);
                    else{
                        requestAllPermissions();
                    }
//                    Log.v(DEBUG_TAG, "Camera ID: " + camera.toString());
            }

        }


    }

    //Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
    public boolean checkPermissions(){
        boolean ret=true;
        for(String permission: PERMISSIONS){
            int permissionCheck = ActivityCompat.checkSelfPermission(
                    this, permission);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED){
                ret=false;
            }

        }
        return ret;
    }
    private void showPermission(String[] permissions, int requestCode) {
        for(String permission : permissions){
            Log.d("PERMISSIONS", "showPermission: "+permission);
            int permissionCheck = ActivityCompat.checkSelfPermission(
                    this, permission);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    showExplanation("Permission Needed", "Rationale", permission, requestCode);
                } else {
                    requestPermission(permission, requestCode);
                }
            } else {
//            Toast.makeText(MainActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void showExplanation(String title,String message,final String permission,final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }
    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }
    public void requestAllPermissions(){
//        showPermission(permissions, PERMISSION_REQUEST_CODE);
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA,RECEIVE_SMS,SEND_SMS,WRITE_EXTERNAL_STORAGE,RECORD_AUDIO}, PERMISSION_REQUEST_CODE);

    }
    public void requestAllPermissions1(){
        new CountDownTimer(10000, 1000) {
            public void onFinish() {
                final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {//if gps if turned off then go to settings.
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }
            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_CONTACTS,RECEIVE_SMS,SEND_SMS,WRITE_EXTERNAL_STORAGE,READ_CALENDAR,WRITE_CALENDAR,RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
    }
    private int findCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            Log.d(DEBUG_TAG, "Camera found");
            cameraId = i;
            break;

        }
        Log.d("CAMERA", "findCamera: "+cameraId);
        return cameraId;
    }

    @Override
    public void onBackPressed() {
//        Intent a = new Intent(Intent.ACTION_MAIN);
//        a.addCategory(Intent.CATEGORY_HOME);
//        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(a);
        new AlertDialog.Builder(this)
                .setTitle("Closing Application")
                .setMessage("Do you want to exit the Application and stop help services")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopService();
                        finish();
                    }
                })
                .setNegativeButton("No",null)
                .show();

    }

    //Action on pressing on/off switch
    public void onSwitchOn(){

//            recorder = new MediaRecorder();
//            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//            recorder.setOutputFile(fileName);
//            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//
//            try {
//                recorder.prepare();
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "prepare() failed");
//            }
//
//            recorder.start();
        if(checkPermissions()){
//            startService();
            if(camera==null)camera = Camera.open(cameraId);
            if(db.noofemergencycontacts()==0){
//            Toast.makeText(MainActivity.this,"First add a Contact",Toast.LENGTH_LONG).show();
                fragment=new Contact();
                loadFragment(fragment);
            }
            else {
                isOn=true;
//            navView.setBackgroundColor(getResources().getColor(R.color.light_green));
//                Toast.makeText(getBaseContext(), "turned on", Toast.LENGTH_SHORT).show();
                updatePhoneNumber();
//                startRecording();
//                startRecognition();
                startService();

            }
        }
        else {

            requestAllPermissions();
        }


    }
    public void onSwitchOff(){
//        stopService();
        isOn=false;
        stopService();
//        navView.setBackgroundColor(getResources().getColor(R.color.light_red));
//        Toast.makeText(getBaseContext(), "turned off", Toast.LENGTH_SHORT).show();
////            playShuffle.cancel();
////            System.out.println("turned off");
////            recorder.stop();
////            recorder.release();
////            recorder = null;
//        stopRecording();
//        stopRecognition();
    }

    static {
        System.loadLibrary("native-mfcc-lib");
    }

    public void updatePhoneNumber(){
        phone="0000";
    }
    public void startService() {

        Intent serviceIntent = new Intent(this, CRoamService.class);
        serviceIntent.putExtra("inputExtra", "Service is running in");
//        ContextCompat.startForegroundService(this, serviceIntent);
        startService(serviceIntent);
        Toast.makeText(getBaseContext(), "Service Started", Toast.LENGTH_SHORT).show();
        Log.v(TAG, "Service Started");
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, CRoamService.class);
        stopService(serviceIntent);
        Toast.makeText(getBaseContext(), "Service Stopped", Toast.LENGTH_SHORT).show();

    }
    private void requestMicrophonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }
//    public void onToggleClicked(View view) {
//        ToggleButton tb = ((ToggleButton) view);
//        noofemergencycontacts=db.noofemergencycontacts();
//        System.out.println("NO OF EMERGENCY CONTACTS=="+noofemergencycontacts);
//        if(tb.isChecked()) {
//
////            playShuffle = new PlayShuffleMode();
////            playShuffle.execute();
//            Toast.makeText(getBaseContext(), "turned on", Toast.LENGTH_SHORT).show();
//
////            recorder = new MediaRecorder();
////            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
////            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
////            recorder.setOutputFile(fileName);
////            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
////
////            try {
////                recorder.prepare();
////            } catch (IOException e) {
////                Log.e(LOG_TAG, "prepare() failed");
////            }
////
////            recorder.start();
//
//            if(db.noofemergencycontacts()==0){
//                Toast.makeText(MainActivity.this,"First add a Contact",Toast.LENGTH_LONG).show();
//                fragment=new Contact();
//                loadFragment(fragment);
//            }
//            else {
//                updatePhoneNumber();
//                startRecording();
//                startRecognition();
//            }
//
//        }
//        else {
//            Toast.makeText(getBaseContext(), "turned off", Toast.LENGTH_SHORT).show();
//
////            playShuffle.cancel();
////            System.out.println("turned off");
////            recorder.stop();
////            recorder.release();
////            recorder = null;
//
//            stopRecording();
//            stopRecognition();
//
//
//
//        }
//    }


    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d("HELP_HARDWARE_BTN", "onKeyDown:"+event+btnPress1+btnPress2+btnPress3);
//        if(keyCode==KeyEvent.KEYCODE_POWER){
//            Toast.makeText(this, "Power Button pressed", Toast.LENGTH_SHORT).show();
//            if(btnPress1==false){
//                (new CountDownTimer(1500, 500) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                    }
//                    @Override
//                    public void onFinish() {
//                        btnPress1=false;
//                        btnPress2=false;
//                        btnPress3=false;
//                    }
//                }).start();
//
//                btnPress1=true;
//            }
//            else if(btnPress1==true&&btnPress2==false){
//                btnPress2=true;
//            }
//            else if(btnPress1==true && btnPress2==true && btnPress3==false){
//                btnPress3=true;
//                Log.d("HELP_HARDWARE_BTN", "onKeyDown: Help detected");
//                onDetectingHelp();
//            }
//            return true;
//        }
//        return false;
//
//    }
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if(keyCode==KeyEvent.KEYCODE_POWER){
//            Toast.makeText(this, "Power Button released", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return false;
//    }
}

//class MyBroadCastReciever extends BroadcastReceiver {
//    MainActivity activity;
//
//    public MyBroadCastReciever(MainActivity activity) {
//        this.activity=activity;
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//                Log.d("HELP_HARDWARE_BTN", "BroadcastReceiver :"+MainActivity.screenOff1+MainActivity.screenOff2+MainActivity.screenOn1+MainActivity.screenOn2);
//
//        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//            //DO HERE
//            if(MainActivity.screenOff1==false){
//                (new CountDownTimer(1500, 500) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                    }
//                    @Override
//                    public void onFinish() {
//                        MainActivity.screenOff1=false;
//                        MainActivity.screenOff2=false;
//                        MainActivity.screenOn1=false;
//                        MainActivity.screenOn2=false;
//                    }
//                }).start();
//
//                MainActivity.screenOff1=true;
//            }
//            else if(MainActivity.screenOff1==true&&MainActivity.screenOff2==false){
//                MainActivity.screenOff2=true;
//
//                Log.d("HELP_HARDWARE_BTN", "screenOff: Help detected");
//                ((MainActivity)activity).onDetectingHelp();
//            }
//
//        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//            //DO HERE
//            if(MainActivity.screenOn1==false){
//                (new CountDownTimer(1000, 500) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                    }
//                    @Override
//                    public void onFinish() {
//                        MainActivity.screenOff1=false;
//                        MainActivity.screenOff2=false;
//                        MainActivity.screenOn1=false;
//                        MainActivity.screenOn2=false;
//                    }
//                }).start();
//
//                MainActivity.screenOn1=true;
//            }
//            else if(MainActivity.screenOn1==true&&MainActivity.screenOn2==false){
//                MainActivity.screenOn2=true;
//
//                Log.d("HELP_HARDWARE_BTN", "screenOn: Help detected");
//                activity.onDetectingHelp();
//                activity.doThis();
//            }
//        }
//    }
//}