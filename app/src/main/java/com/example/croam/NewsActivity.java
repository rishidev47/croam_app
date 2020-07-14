package com.example.croam;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity  implements NewsAdapter.OnItemListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ReportModel> reportsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        mRecyclerView = findViewById(R.id.newsRecyclerView);
        reportsList = new ArrayList<>();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                this.getApplicationContext());
        String token = prefs.getString("access_token", null);
        final Map<String, String> auth = new HashMap<>();
        auth.put("Authorization",
                "Token " + token);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //  mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // specify an adapter and pass in our data model list

        mAdapter = new NewsAdapter(reportsList, this,this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        Call<ResponseBody> call = MyApi.Companion.invoke().getReports(auth);
        Objects.requireNonNull(call).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                    Response<ResponseBody> response) {

                try {

                    JSONArray reports = new JSONArray(response.body().string());
                    Log.e("news", reports.toString());

                    for (int i = 0; i < reports.length(); i++) {
                        JSONObject report = reports.getJSONObject(i);
                        Log.e("report", report.toString());
                        reportsList.add(
                                new ReportModel(report.getString("name"), report.getString("media"),
                                        report.getString("longitude"), report.getString("latitude"),
                                        report.getString("country"), report.getString("state"),
                                        report.getString("city"), report.getString("description"),
                                        report.getString("created")));
                        mAdapter.notifyDataSetChanged();

                    }
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

    @Override
    public void OnBookClick(int position) {

    }

    @Override
    public void OnBookLongClick(int position, View view) {

    }

    @Override
    public void OnButton1Click(int position, View view) {

    }

    @Override
    public void OnButton2Click(int position, View view) {

    }
}