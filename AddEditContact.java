package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phonebook.DbHelper.Constants;
import com.example.phonebook.DbHelper.DbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddEditContact extends AppCompatActivity {

    private CircleImageView contactIv;
    private EditText nameEt,phoneEt,notesEt,emailEt;
    private FloatingActionButton fab;

    //=========
    private String id, image, name, phone, notes, email, addedTime, updatedTime;
    private Boolean isEditMode;
    private ActionBar actionBar;

    //=========

    private String[] cameraPermission;
    private String[] storagePermission;

    //=========
    public static final int CAMERA_PERMISSION_CODE = 100;
    public static final int STORAGE_PERMISSION_CODE = 200;
    public static final int IMAGE_FROM_STORAGE_CODE = 300;
    public static final int IMAGE_FROM_CAMERA_CODE = 400;

    //==========
    Uri imageUri;
    private DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        dbHelper = new DbHelper(this);

        //===========
        actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //========
        contactIv = findViewById(R.id.contactIv);
        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);
        notesEt = findViewById(R.id.notesEt);
        emailEt = findViewById(R.id.emailEt);
        fab = findViewById(R.id.fab);

        //=========
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);
        if(isEditMode){
            setTitle("Edit contact");

            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            image = intent.getStringExtra("IMAGE");
            phone = intent.getStringExtra("PHONE");
            email = intent.getStringExtra("EMAIL");
            notes = intent.getStringExtra("NOTES");
            addedTime = intent.getStringExtra("ADDEDTIME");
            updatedTime = intent.getStringExtra("UPDATEDTIME");

            nameEt.setText(name);
            phoneEt.setText(phone);
            emailEt.setText(email);
            notesEt.setText(notes);
            imageUri = Uri.parse(image);
            if(image.equals("")){
                contactIv.setImageResource(R.drawable.ic_person);
            }else {
                contactIv.setImageURI(imageUri);
            }


        }else {
            setTitle("Add new contact");
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save data
                saveContact();
                finish();
            }
        });

        contactIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickerDialogue();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void saveContact() {
        name = nameEt.getText().toString();
        phone = phoneEt.getText().toString();
        email = emailEt.getText().toString();
        notes = notesEt.getText().toString();

        String timeStamp = ""+System.currentTimeMillis();

        if( !name.isEmpty() || !phone.isEmpty() || email.isEmpty() || !notes.isEmpty()){
            if(isEditMode){
                //edit contact
                dbHelper.editUpdateContact(
                        ""+id,
                        ""+imageUri,
                        ""+name,
                        ""+phone,
                        ""+email,
                        ""+notes,
                        ""+addedTime,
                        ""+timeStamp
                );
                finish();
                Toast.makeText(this, "Updated Successfully....", Toast.LENGTH_SHORT).show();
            }else {
                //add new contact
                //save data
                long id = dbHelper.addContact(
                        imageUri+"",
                        name+"",
                        phone+"",
                        email+"",
                        notes+"",
                        timeStamp+"",
                        timeStamp+""
                );
                Toast.makeText(this, "Inserted Successfully.."+id, Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Fields can't be empty !", Toast.LENGTH_SHORT).show();
        }

    }

    private void imagePickerDialogue() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else {
                        pickFromStorage();
                    }
                }

            }
        }).create().show();

    }

    private void pickFromStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_FROM_STORAGE_CODE);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "IMAGE_TITLE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "IMAGE_DETAIL");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(cameraIntent, IMAGE_FROM_CAMERA_CODE);
    }

    private void requestStoragePermission() {
        if(storagePermission == null){
            storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_PERMISSION_CODE);
    }

    private void requestCameraPermission() {
        if(cameraPermission == null){
            cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_PERMISSION_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return result;
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result1 =  ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return result & result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }else {
                        Toast.makeText(this, "Camera permission required !", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_PERMISSION_CODE:
                if(grantResults.length > 0){
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromStorage();
                    }else {
                        Toast.makeText(this, "Storage permission required !", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_FROM_STORAGE_CODE){
                assert data != null;
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
                
            } else if (requestCode == IMAGE_FROM_CAMERA_CODE) {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
                
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                contactIv.setImageURI(imageUri);
                
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "something went wrong !", Toast.LENGTH_SHORT).show();
            }
        }

    }
}