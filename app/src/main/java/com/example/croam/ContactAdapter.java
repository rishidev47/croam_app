package com.example.croam;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContactAdapter extends ArrayAdapter <String> {
    private Context mContext;
    private Contact contactFrag;
    private static final String CONTACT1 = "contact1";
    private static final String CONTACT2 = "contact2";
    private static final String CONTACT3 = "contact3";
    private static final String CONTACT4 = "contact4";
    private static final String CONTACT5 = "contact5";
    private String[] contacts = {CONTACT1,CONTACT2,CONTACT3,CONTACT4,CONTACT5};
    private static final String NAME1 = "name1";
    private static final String NAME2 = "name2";
    private static final String NAME3 = "name3";
    private static final String NAME4 = "name4";
    private static final String NAME5 = "name5";
    private String[] names = {NAME1,NAME2,NAME3,NAME4,NAME5};
    private SharedPreferences    prefs = PreferenceManager.getDefaultSharedPreferences(
            Objects.requireNonNull(getContext()).getApplicationContext()); //Get the preferences
    private SharedPreferences.Editor edit = prefs.edit();

    ContactAdapter(@NonNull Context context, int resource, @NonNull List<String> objects,
            Contact activity) {
        super(context, 0, objects);
        mContext=context;
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
                    int i;
                    for(i=0;i<5;++i){
                        if(contact[1].equals(prefs.getString(contacts[i],null)) && prefs.getString(contacts[i],null)!=null)break;
                    }
                    Log.e("no",String.valueOf(i));
                    edit.putString(contacts[i],null);
                    edit.putString(names[i],null);
                    edit.commit();
                    contactFrag.updateContactList();

                }
            });
        }



        return listItem;
    }
}
