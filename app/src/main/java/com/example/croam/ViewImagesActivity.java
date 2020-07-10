package com.example.croam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class ViewImagesActivity extends AppCompatActivity implements MyAdapter.OnItemListener {
    File file;
    ArrayList<File> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_images);
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        file = new File(sdDir, "Croam");
        File[] listFile= null;
        if (file.isDirectory()){
            listFile = file.listFiles();
            Log.e("list",listFile[0].getAbsolutePath());
        }

        list = new ArrayList<>();
        for(File f : listFile){
            list.add(f);
        }
       RecyclerView recyclerView = findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        MyAdapter mAdapter = new MyAdapter(list, this, this);
        recyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onLinkClick(int position) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.withAppendedPath(Uri.fromFile(file), ""+list.get(position).getName()), "image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}