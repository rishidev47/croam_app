package com.example.croam;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends ArrayAdapter <String> {
    Context mContext;
    DBHandler db;
    Contact contactFrag;
    public ContactAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, Contact activity) {
        super(context, 0, objects);
        mContext=context;
        db=new DBHandler(context);
        contactFrag=activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String[] contact=getItem(position).split("@@",0);
        View listItem=convertView;

        if(listItem==null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.view_contact,parent,false);

        }
        if(getItem(position)!=""){
            TextView nameView=listItem.findViewById(R.id.contact_name);
            TextView phoneView=listItem.findViewById(R.id.contact_number);
            Button delete_btn=listItem.findViewById(R.id.delete_button);
            nameView.setText(contact[0]);
            phoneView.setText(contact[1]);
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.deleteRecord(contact[1]);
                    contactFrag.updateContactList();

                }
            });
        }



        return listItem;
    }
}
