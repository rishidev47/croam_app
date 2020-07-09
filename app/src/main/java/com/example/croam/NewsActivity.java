package com.example.croam;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                this.getApplicationContext());
        String token = prefs.getString("access_token", null);
        final Map<String, String> auth = new HashMap<>();
        auth.put("Authorization",
                "Token " + token);

        Call<ResponseBody> call = MyApi.Companion.invoke().getReports(auth);
        Objects.requireNonNull(call).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                    Response<ResponseBody> response) {

                try {

                    JSONArray reports = new JSONArray(response.body().string());
                    Log.e("news", reports.toString());
//                    {"id":1,"name":"New","media":"https:\/\/storage.googleapis
//                    .com\/report-media\/VID_20200626_120950.mp4","latitude":"28.433556",
//                    "longitude":"77.31817","country":"India","state":"Haryana",
//                    "city":"Faridabad","description":"I am ____","block":null,"likes":null,
//                    "created":"2020-06-26 06:40:11"}

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("news", t.getMessage());
            }
        });

    }
}