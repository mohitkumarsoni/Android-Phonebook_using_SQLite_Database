package com.example.phonebook.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.tech.NfcA;

import androidx.annotation.Nullable;

import com.example.phonebook.Model_Classes.ContactModelClass;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+Constants.CREATE_TABLE );
        onCreate(db);
    }

    public long addContact(String image, String name, String phone, String email, String notes,String addedTime, String updatedTime ){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COL_IMAGE, image);
        values.put(Constants.COL_NAME, name);
        values.put(Constants.COL_PHONE, phone);
        values.put(Constants.COL_EMAIL, email);
        values.put(Constants.COL_NOTES, notes);
        values.put(Constants.COL_ADDED_TIME, addedTime);
        values.put(Constants.COL_UPDATED_TIME, updatedTime);

        long id = db.insert(Constants.CONTACT_TABLE, null, values);
        db.close();
        return id;
    }

    public void updateTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(" DROP TABLE IF EXISTS "+Constants.CONTACT_TABLE);
        onCreate(db);
    }

    public ArrayList<ContactModelClass> getAllData(String orderBy){
        ArrayList<ContactModelClass> contactsArr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * FROM "+Constants.CONTACT_TABLE+" ORDER BY "+orderBy, null);
        if(cursor.moveToFirst()){
            do {
                ContactModelClass modelClass = new ContactModelClass(
                        ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COL_ID)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_NAME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_PHONE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_EMAIL)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_NOTES)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_ADDED_TIME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_UPDATED_TIME))
                );
                contactsArr.add(modelClass);
            }while (cursor.moveToNext());
        }
        db.close();
        return contactsArr;
    }

    public void editUpdateContact(String id, String image, String name, String phone, String email, String notes,String addedTime, String updatedTime){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COL_IMAGE,image);
        values.put(Constants.COL_NAME,name);
        values.put(Constants.COL_PHONE,phone);
        values.put(Constants.COL_EMAIL,email);
        values.put(Constants.COL_NOTES,notes);
        values.put(Constants.COL_ADDED_TIME,addedTime);
        values.put(Constants.COL_UPDATED_TIME,updatedTime);

        db.update(Constants.CONTACT_TABLE, values, Constants.COL_ID+" =? ", new String[]{id});
        db.close();

    }

    public void deleteContact(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.CONTACT_TABLE, Constants.COL_ID+" =? ",new String[]{id});
        db.close();
    }
    public void deleteAllContacts(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(" DELETE FROM "+Constants.CONTACT_TABLE);
        db.close();
    }

}
