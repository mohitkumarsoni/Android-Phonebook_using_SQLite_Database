package com.example.phonebook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phonebook.DbHelper.Constants;
import com.example.phonebook.DbHelper.DbHelper;

import java.util.Calendar;
import java.util.Locale;

public class ContactDetailActivity extends AppCompatActivity {
    private ImageView contactIv;
    private TextView nameTv, phoneTv, emailTv, notesTv, addedTimeTv, updatedTimeTv;
    private String id;
    private DbHelper dbHelper;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        dbHelper = new DbHelper(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("contactId");

        actionBar = getSupportActionBar();
        actionBar.setTitle("Contact Detail");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        contactIv = findViewById(R.id.contactIv);
        nameTv = findViewById(R.id.nameTv);
        phoneTv = findViewById(R.id.phoneTv);
        emailTv = findViewById(R.id.emailTv);
        notesTv = findViewById(R.id.notesTv);
        addedTimeTv = findViewById(R.id.addedTimeTv);
        updatedTimeTv = findViewById(R.id.updatedTimeTv);

        loadDataById();
    }

    private void loadDataById() {
        String selectQuery = " SELECT * FROM "+ Constants.CONTACT_TABLE +" WHERE "+ Constants.COL_ID +" =\""+ id +"\"";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_NAME));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_IMAGE));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_PHONE));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_EMAIL));
                String notes = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_NOTES));
                String addTime = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_ADDED_TIME));
                String updateTime = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COL_UPDATED_TIME));

                //convert date and time into dd/mm/yy & hh:mm:aa format
                Calendar calendar = Calendar.getInstance(Locale.getDefault());

                calendar.setTimeInMillis(Long.parseLong(addTime));
                String timeAdd = ""+DateFormat.format("dd/mm/yy  hh:mm:aa",calendar);

                calendar.setTimeInMillis(Long.parseLong(updateTime));
                String timeUpdate = ""+DateFormat.format("dd/mm/yy  hh:mm:aa",calendar);

                if(image.equals("null")){
                    contactIv.setImageResource(R.drawable.ic_person);
                }else {
                    contactIv.setImageURI(Uri.parse(image));
                }

                nameTv.setText(name);
                phoneTv.setText(phone);
                emailTv.setText(email);
                notesTv.setText(notes);
                addedTimeTv.setText(timeAdd);
                updatedTimeTv.setText(timeUpdate);

            }while (cursor.moveToNext());
        }


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}