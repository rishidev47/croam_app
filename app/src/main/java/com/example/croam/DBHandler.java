package com.example.croam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper{
    private static final String DB_NAME="demo3";
    private static final int DB_VERSION=1;

    private static final String E_CONTACT_TABLE="record";
    private static final String PROFILE_TABLE="profile";
    private static final String POLICE_TABLE="police";
    private static final String HELP_TABLE="detection";


    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String query="CREATE TABLE "+TABLE_NAME+" ("+ID_COL+" INTEGER PRIMARY KEY AUTOINCREMENT,"+NAME_COL+" TEXT)";
        String query="CREATE TABLE "+E_CONTACT_TABLE+" (id INTEGER PRIMARY KEY AUTOINCREMENT,Name TEXT,Contact TEXT)";
        db.execSQL(query);

        String query1="CREATE TABLE "+PROFILE_TABLE+" (id INTEGER PRIMARY KEY AUTOINCREMENT,Name TEXT,Phone TEXT,Email TEXT,Dob TEXT)";
        db.execSQL(query1);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " +E_CONTACT_TABLE);

        // Create table again
        onCreate(db);
    }

    public void insertRecord(String name,String contact){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put("Name",name);
        values.put("Contact",contact);
        db.insert(E_CONTACT_TABLE,null,values);
        db.close();
    }

    public String getRecords(){
        String query="SELECT * FROM "+E_CONTACT_TABLE;
        String result="";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);

        cursor.moveToFirst();
        while(cursor.isAfterLast()==false){
            result+=cursor.getString(1)+"@@"+cursor.getString(2)+"\n";
            cursor.moveToNext();
        }

        db.close();
        return result;
    }

    public void updateRecord(String id,String name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("Name",name);

        db.update(E_CONTACT_TABLE,values,"id=?",new String[]{id});
        db.close();
    }

    public void deleteRecord(String number){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(E_CONTACT_TABLE,"Contact=?",new String[]{number});

        db.close();
    }
    public String getphone(int i){
        String query="SELECT * FROM "+E_CONTACT_TABLE;
        String result="";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();

        for(int j=1; j<i; j++){
            cursor.moveToNext();
        }
        result=cursor.getString(2);
        result=result.trim();
        return result;
    }

    public int noofemergencycontacts(){
        String query="SELECT * FROM "+E_CONTACT_TABLE;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();

        return cursor.getCount();
    }

    public boolean checkifthisnumberisadded(String number){
        String query="SELECT * FROM "+E_CONTACT_TABLE;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        boolean check=false;
        String temp="";
        while(cursor.isAfterLast()==false){
            temp=cursor.getString(2);
            if(temp.equalsIgnoreCase(number)){
                check=true;
                break;
            }
            cursor.moveToNext();
        }

        db.close();
        return check;
    }

}