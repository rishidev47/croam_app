package com.example.croam;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Contact extends Fragment {
    private static final int ADD_CONTACT = 1;
    private static final String CONTACT1 = "contact1";
    private static final String CONTACT2 = "contact2";
    private static final String CONTACT3 = "contact3";
    private static final String CONTACT4 = "contact4";
    private static final String CONTACT5 = "contact5";
    private static final String NAME1 = "name1";
    private static final String NAME2 = "name2";
    private static final String NAME3 = "name3";
    private static final String NAME4 = "name4";
    private static final String NAME5 = "name5";
    private String[] contacts = {CONTACT1, CONTACT2, CONTACT3, CONTACT4, CONTACT5};
    private String[] names = {NAME1, NAME2, NAME3, NAME4, NAME5};
    private ArrayList<String> contactList;
    private ContactAdapter adapter;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(
                Objects.requireNonNull(getContext()).getApplicationContext()); //Get the preferences
        edit = prefs.edit();
        ListView list = view.findViewById(R.id.list_contact);
        FloatingActionButton add_btn = view.findViewById(R.id.fab_add_contact);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, ADD_CONTACT);
            }
        });
        list.setAdapter(adapter);
        updateContactList();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String phone=contactList.get(position).split("@@",0)[1];
                callIntent.setData(Uri.parse("tel:"+phone));
                startActivity(callIntent);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactList = new ArrayList<>();
        adapter = new ContactAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(),
                0, contactList, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (prefs.getString(CONTACT1, null) == null && prefs.getString(CONTACT2, null) == null
                && prefs.getString(CONTACT3, null) == null && prefs.getString(CONTACT4, null)
                == null && prefs.getString(CONTACT5, null) == null) {
            String alert = "         No Contact Added" + "\n" + "         Add for your safety";
        }
        if (requestCode == ADD_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(uri,
                        projection,
                        null, null, null);
                cursor.moveToFirst();
                int numberColumnIndex = cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);
                int nameColumnIndex = cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(nameColumnIndex);

                boolean already = false;

                int j = -1;
                int k = 0;
                for (String i : contacts) {

                    String c = prefs.getString(i, null);

                    if (c == null) {
                        j = k;
                    }

                    if (c != null && c.equals(number)) {
                        already = true;
                        break;
                    }
                    ++k;
                }

                if (already) {
                    Toast.makeText(getActivity(), "This contact is already added",
                            Toast.LENGTH_LONG).show();
                } else if (j == -1) {
                    Toast.makeText(getContext(), "MAX Contacts Added", Toast.LENGTH_LONG).show();
                } else {
                    edit.putString(contacts[j], number);
                    edit.putString(names[j], name);
                    edit.commit();

                    Log.e("name", name + number);
                }
                updateContactList();
            }
        }

        if (prefs.getString(CONTACT1, null) == null && prefs.getString(CONTACT2, null) == null
                && prefs.getString(CONTACT3, null) == null && prefs.getString(CONTACT4, null)
                == null && prefs.getString(CONTACT5, null) == null) {
            String alert = "         No Contact Added" + "\n" + "         Add for your safety";
            Toast.makeText(getActivity(), alert, Toast.LENGTH_LONG).show();
        }
    }

    void updateContactList() {

        contactList.clear();
        contactList.add("NEAREST POLICE"+"@@"+new PoliceContacts(getContext()).getPoliceContact());

        for (int i = 0; i < 5; ++i) {
            String n = prefs.getString(names[i], null);
            String c = prefs.getString(contacts[i], null);
//            Log.e("contact", n+c);

            if (n != null && c != null) {
                contactList.add(n + "@@" + c);
            }
            Log.e("size", String.valueOf(contactList.size()));
        }
        if (contactList.size() == 0) {
            ((MainActivity) Objects.requireNonNull(getActivity())).onSwitchOff();
            Toast.makeText(getActivity(), "Add a Contact to enable help services",
                    Toast.LENGTH_LONG).show();
        }
        adapter.notifyDataSetChanged();
    }
}
