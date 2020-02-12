//package com.example.croam;
//
//import android.app.DatePickerDialog;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Color;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
////import com.hitomi.cmlibrary.CircleMenu;
////import com.hitomi.cmlibrary.OnMenuSelectedListener;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Calendar;
//import java.util.List;
//
//public class Register extends AppCompatActivity {
//
//
//    String arrayName[]={"Facebook",
//            "Twitter",
//            "Google",
//            "Youtube",
//            "Drive"};
//
//    Button register,date_of_birth;
//    EditText firstname,lastname,userId,password,confirmpassword,contact;
//    Calendar calender;
//    int year,month,day;
//    DatePickerDialog datePicker;
//    TextView date_view;
//    RadioButton gender_radiobutton;
//    RadioGroup gender_radiogroup;
//    String gender;
//    ProgressDialog progressDialog;
//    TextView LoginText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        firstname=(EditText)findViewById(R.id.firstname);
//        lastname=(EditText)findViewById(R.id.lastname);
//        userId=(EditText)findViewById(R.id.userId);
//        contact=(EditText)findViewById(R.id.contact);
//        password=(EditText)findViewById(R.id.password);
//        confirmpassword=(EditText)findViewById(R.id.confirmpassword);
//        date_of_birth=(Button)findViewById(R.id.date_btn);
//        date_view=(TextView)findViewById(R.id.date_view);
//        gender_radiogroup=(RadioGroup)findViewById(R.id.gender_radiogroup);
//        register=(Button)findViewById(R.id.register);
//        date_view.setText("DD/MM/YYYY");
//        LoginText=(TextView)findViewById(R.id.LoginText);
//
//
//
//        progressDialog=new ProgressDialog(this);
//        progressDialog.setMessage("Registering...");
//
//
//        date_of_birth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                calender=Calendar.getInstance();
//                year=calender.get(Calendar.YEAR);
//                month=calender.get(Calendar.MONTH);
//                day=calender.get(Calendar.DAY_OF_MONTH);
//                datePicker=new DatePickerDialog(Register.this,new DatePickerDialog.OnDateSetListener(){
//                    @Override
//                    public void onDateSet(android.widget.DatePicker datePicker,int year1,int month1,int day1){
//                        day=day1;
//                        month=month1+1;
//                        year=year1;
//                        String str=day1+"/"+(month1+1)+"/"+year1;
//                        date_view.setText(str);
//                    }
//                },year,month,day);
//                datePicker.show();
//            }
//        });
//
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try{
//                    int id_gender=gender_radiogroup.getCheckedRadioButtonId();
//                    gender_radiobutton=(RadioButton)findViewById(id_gender);
//                    gender=gender_radiobutton.getText().toString();
//                }catch(Exception e)
//                {
//                    Toast.makeText(getApplicationContext(),"Select your gender",Toast.LENGTH_LONG).show();
//                }
//
//                String first_name=firstname.getText().toString();
//                String last_name=lastname.getText().toString();
//                String contactnumber=contact.getText().toString();
//                String pass=password.getText().toString();
//                String confirm=confirmpassword.getText().toString();
//                String user=userId.getText().toString();
//                String day_string=day+"";
//                String month_string=month+"";
//                String year_string=year+"";
//                String sex=gender;
//
//                progressDialog.show();
//
//
//
//                if(senddatatoserver(first_name,last_name,user,pass,contactnumber,sex,day_string,month_string,year_string)){
//                    Toast.makeText(Register.this,"Registered Successfully",Toast.LENGTH_LONG).show();
//                    Intent intent=new Intent(Register.this,MainActivity.class);
//                    startActivity(intent);
//                }else{
//                    Toast.makeText(Register.this,"This User ID already used, try another.",Toast.LENGTH_LONG).show();
//                }
//
//
//            }
//        });
//
//        LoginText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(Register.this,Login.class);
//                startActivity(intent);
//            }
//        });
//
//
//    }
//
//
//    public boolean senddatatoserver(final String firstname, final String lastname, final String Id, final String pass, final String phone, final String Gender, final String dayofbirth, final String monthofbirth, final String yearofbirth)
//    {   //WORKING
//        /*try{
//
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try  {
//                        String Sex="";
//                        if(Gender.equals("Male")){
//                            Sex="M";
//                        }
//                        else{
//                            Sex="F";
//                        }
//
//                        HttpURLConnection client=null;
//                        URL url=new URL("http://192.168.43.99:8089/newuser");
//                        client=(HttpURLConnection)url.openConnection();
//                        client.setDoOutput(true);
//                        client.setRequestMethod("POST");
//
//                        client.setRequestProperty("process","register");
//                        client.setRequestProperty("uid",Id);
//                        client.setRequestProperty("password",pass);
//                        client.setRequestProperty("firstname",firstname);
//                        client.setRequestProperty("lastname",lastname);
//                        client.setRequestProperty("phone",phone);
//                        client.setRequestProperty("sex",Sex);
//                        client.setRequestProperty("dayofbirth",dayofbirth);
//                        client.setRequestProperty("monthofbirth",monthofbirth);
//                        client.setRequestProperty("yearofbirth",yearofbirth);
//
//
//                        //String str="abcd";
//                        DataOutputStream outputPost=new DataOutputStream(client.getOutputStream());
//                        System.out.println("K");
//                        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputPost,"UTF-8"));
//                        //outputPost.write(str.getBytes());
//                        outputPost.flush();
//                        outputPost.close();
//                        String response="";
//                        int responsecode=client.getResponseCode();
//
//                        if(responsecode==HttpURLConnection.HTTP_OK){
//                            String line;
//
//                            System.out.println("output");
//                            BufferedReader br=new BufferedReader(new InputStreamReader(client.getInputStream()));
//                            while((line=br.readLine())!=null){
//                                response=response+line;
//                                System.out.println("taking input");
//                            }
//                        }
//                        else{
//                            response="No as such responce is sent from Server";
//                        }
//                        System.out.println(response);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            thread.start();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            System.out.println(e);
//        }*/
//
//        boolean userexist=true;
//
//
//
//        Thread t=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                StringBuilder stb = new StringBuilder();
//                try {
//                    System.out.println("1==");
//                    System.out.println("2==");
//                    System.out.println("3==");
//
//                    String charset = "UTF-8";
//                    //String requestURL = "http://192.168.43.207:8080/register/";
//                    String requestURL = "http://hot-pig-46.localtunnel.me/register/";
//
//                    MultipartUtility multipart = new MultipartUtility(requestURL, charset);
//                    System.out.println("4==");
//
////            multipart.addHeaderField("User-Agent", "CodeJava");
////            multipart.addHeaderField("Test-Header", "Header-Value");
//
//                    multipart.addFormField("name", firstname);
//                    System.out.println("5==");
//                    multipart.addFormField("password", pass);
//                    multipart.addFormField("age", "22");
//                    multipart.addFormField("uid", Id);
//
//                    multipart.addFormField("phone", phone);
//
//                    multipart.addFormField("email", "EMAIL@EMAIL.COM");
//
//                    multipart.addFormField("salt", "NAMAK");
//
//                    System.out.println("6==");
//                    System.out.println("7==");
//                    List<String> response = multipart.finish();
//
//                    System.out.println("8==");
//                    Log.v("rht", "SERVER REPLIED:");
//
//                    for (String line : response) {
//                        Log.v("rht", "Line : "+line);
//                        stb.append(line).append("\n");
//                        Log.v("dd",line);
//
//                    }
//                    System.out.println(response);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("ERROR===");
//                }
//            }
//        });
//        t.start();
//
//
//        return true;
//    }
//
//
//    //Working
//    public void server()
//    {
//        try{
//
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try  {
//
//                        HttpURLConnection client=null;
//                        URL url=new URL("http://192.168.43.99:8089/newuser");
//                        client=(HttpURLConnection)url.openConnection();
//                        client.setDoOutput(true);
//                        client.setRequestMethod("POST");
//                        client.setRequestProperty("name","DIWAKAR Prajapati");
//                        client.setRequestProperty("sex","M");
//                        client.setRequestProperty("phone","9674418222");
//                        client.setRequestProperty("uid","54321");
//
//
//                        //String str="abcd";
//                        DataOutputStream outputPost=new DataOutputStream(client.getOutputStream());
//                        System.out.println("K");
//                        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputPost,"UTF-8"));
//                        //outputPost.write(str.getBytes());
//                        System.out.println("L");
//                        outputPost.flush();
//                        System.out.println("M");
//                        outputPost.close();
//                        String response="";
//                        int responsecode=client.getResponseCode();
//
//                        if(responsecode==HttpURLConnection.HTTP_OK){
//                            String line;
//
//                            System.out.println("output");
//                            BufferedReader br=new BufferedReader(new InputStreamReader(client.getInputStream()));
//                            while((line=br.readLine())!=null){
//                                response=response+line;
//                                System.out.println("taking input");
//                            }
//                        }
//                        else{
//                            response="No as such responce is sent from Server";
//                        }
//                        System.out.println(response);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            thread.start();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            System.out.println(e);
//        }
//    }
//}
