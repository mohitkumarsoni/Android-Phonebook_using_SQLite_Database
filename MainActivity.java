package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.phonebook.Adapter.ContactAdapter;
import com.example.phonebook.DbHelper.Constants;
import com.example.phonebook.DbHelper.DbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    RecyclerView contactsRv;
    FloatingActionButton fab;
    DbHelper dbHelper;

    private String sortByNameAsc = Constants.COL_NAME+" ASC";
    private String sortByNamedDesc = Constants.COL_NAME+" DESC";
    private String sortByRecent = Constants.COL_ADDED_TIME+" DESC";
    private String sortByOld = Constants.COL_ADDED_TIME+" ASC";

    private String currentSort = sortByNameAsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        contactsRv = findViewById(R.id.contactsRv);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditContact.class);
                intent.putExtra("isEditMode",false);
                startActivity(intent);
            }
        });
        loadData(currentSort);
    }
    public void loadData(String currentSort) {
        ContactAdapter adapter = new ContactAdapter(this, dbHelper.getAllData(currentSort));
        contactsRv.setAdapter(adapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        loadData(currentSort);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAllContact){
            AlertDialog.Builder allContactDeleteWarning = new AlertDialog.Builder(this);

            allContactDeleteWarning.setTitle("are you sure you want to delete all contacts");
            allContactDeleteWarning.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbHelper.deleteAllContacts();
                    onResume();
                }
            });
            allContactDeleteWarning.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onResume();
                }
            });
            allContactDeleteWarning.create().show();
        }
        if (item.getItemId() == R.id.sortContacts){
            sortDialog();
        }
        return true;
    }

    private void sortDialog() {
        String[] options = {"sort by recent","sort by old","sort by name ascending","sort by name descending"};
        AlertDialog.Builder sortingOptions = new AlertDialog.Builder(this);
        sortingOptions.setTitle("choose order");
        sortingOptions.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    loadData(sortByRecent);
                }if (which == 1){
                    loadData(sortByOld);
                }if (which == 2){
                    loadData(sortByNameAsc);
                }if (which == 3){
                    loadData(sortByNamedDesc);
                }
            }
        }).create().show();
    }
}


/*

1. profile image taking with user permission and crop functionality
    1.1 first permission from manifest, check, request permission
    1.2 by clicking imageIv open dialog to choose image
    1.3 pickImage and save in ImageUri variable
    1.4 create activity for crop image in manifest file

2. Create SQLite database and add data
    2.1 create class called "Constants" for database and table filed title

3. ========= steps to show SQLite data in the RecycleView ==========
    3.1 for recycleView item we need a item layout
    3.2 add recycleView in main activity
    3.3 create model class for data
    3.4 create Adapter class to show data in recycleView

    3.5 get data from sql and show data in recycleView by adapter

    3.6 to get data awe need sql command in db helper

4.  read all details of particular contact by just creating new Activity showing "contact detail", and take id from Adapter to fetch detail in Activity

5.  ======== Edit or Update data function with swap layout in RecycleView ========
    5.1 first add dependencies for swap layout
    5.2 after sync project, design swap layout in row_contact_layout, item of RecycleView
    5.3 now modify in ContactAdapter
    5.4 work with Edit option in ContactAdapter
    5.5 now get data to AddEditContact Activity via Intent
    5.6 now create sql command in DbHelper class to update data

6.  ===== Delete all or specific data from database =======
    6.1 create query in the DbHelper
    4.2 set function on delete button
    4.3 for delete all data we add delete button on action bar
        4.3.1 create option menu

7.  ======== Implement search function in app ==============
    7.1 first add search view in menu & get search item from menu in MainActivity,onCreateOptionsMenu()
    7.2 create search query in DbHelper class
    (THERE WAS PROBLEM WITH OUR SEARCH FUNCTION, i need to work on it in future)

8.  Implement sort data function
    8.1 first add sort option in menu

* */