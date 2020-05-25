package com.example.croam;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Contact extends Fragment {
    private static final int ADD_CONTACT=1;
    ArrayList<String> contactList;
    ContactAdapter adapter;
    DBHandler db;
    ListView list;
    View view;
    FloatingActionButton add_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_contact, container, false);
        list =view.findViewById(R.id.list_contact);
        add_btn =view.findViewById(R.id.fab_add_contact);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, ADD_CONTACT);
//                output.setText(db.getRecords());
                // Toast.makeText(getActivity(),"you are done bro", Toast.LENGTH_SHORT).show();
            }
        });
        final BottomNavigationView navView=((MainActivity)getActivity()).navView;
        navView.setBackgroundColor(getResources().getColor(R.color.white));
//        if(((MainActivity)getActivity()).isOn){
//            view.setBackgroundColor(getResources().getColor(R.color.light_green));
//        }
//        else {
//            view.setBackgroundColor(getResources().getColor(R.color.light_red));
//        }

        list.setAdapter(adapter);
        updateContactList();
        return view ;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHandler(getActivity().getApplicationContext());
        contactList=new ArrayList<>();
        adapter=new ContactAdapter(getActivity().getApplicationContext(),0,contactList,this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(db.noofemergencycontacts()==0){
            String alert="         No Contact Added"+"\n"+"         Add for your safety";
//            output.setText(alert);
        }
        if (requestCode ==  ADD_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };
                Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();
                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);
                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(nameColumnIndex);

                String contact = name;
                String num = number;

                if(db.checkifthisnumberisadded(number)){
                    Toast.makeText(getActivity(),"This contact is already added",Toast.LENGTH_LONG).show();
                }
                else {
                    db.insertRecord(name,number);
                }
//                output.setText(db.getRecords());

                //  Log.d(TAG, "ZZZ number : " + number +" , name : "+name);
                updateContactList();
            }
        }

        if(db.noofemergencycontacts()==0){
            String alert="         No Contact Added"+"\n"+"         Add for your safety";
            Toast.makeText(getActivity(),alert,Toast.LENGTH_LONG).show();
//            output.setText(alert);
        }
    }
    void updateContactList(){
        String contacts=db.getRecords();
        contactList.clear();
        for(String c : contacts.split("\n",0)){
            if(c!="")contactList.add(c);
        }
        if(contactList.size()==0){
            ((MainActivity)getActivity()).onSwitchOff();
            Toast.makeText(getActivity(),"Add a Contact to enable help services",Toast.LENGTH_LONG).show();
//            view.setBackgroundColor(getResources().getColor(R.color.light_red));
        }
        adapter.notifyDataSetChanged();
    }
}
