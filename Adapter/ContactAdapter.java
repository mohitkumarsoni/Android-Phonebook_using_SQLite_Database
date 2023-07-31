package com.example.phonebook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phonebook.AddEditContact;
import com.example.phonebook.ContactDetailActivity;
import com.example.phonebook.DbHelper.DbHelper;
import com.example.phonebook.MainActivity;
import com.example.phonebook.Model_Classes.ContactModelClass;
import com.example.phonebook.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    Context context;
    ArrayList<ContactModelClass> contactList;
    private DbHelper dbHelper;
    LayoutInflater inflater;
    public ContactAdapter(Context context, ArrayList<ContactModelClass> contactList){
        this.context = context;
        this.contactList = contactList;
        inflater = LayoutInflater.from(context);
        dbHelper = new DbHelper(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contact_rows_recycle_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ContactModelClass modelClass = contactList.get(position);
        String id = modelClass.getId();
        String name =  modelClass.getName();
        String image = modelClass.getImage();
        String phone = modelClass.getPhone();
        String email = modelClass.getEmail();
        String notes = modelClass.getNotes();
        String addedTime = modelClass.getAddedTime();
        String updatedTime = modelClass.getUpdatedTime();

        if(image.equals("")){
            holder.contactImage.setImageResource(R.drawable.ic_person);
        }else {
            holder.contactImage.setImageURI(Uri.parse(image));
        }

        if(name.equals("")){
            holder.contactName.setText(R.string.unknown_number);
        }else {
            holder.contactName.setText(name);
        }

        holder.contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContactDetailActivity.class);
                intent.putExtra("contactId", id);
                context.startActivity(intent);
            }
        });

        holder.contactEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddEditContact.class);
                intent.putExtra("ID",id);
                intent.putExtra("NAME",name);
                intent.putExtra("IMAGE",image);
                intent.putExtra("PHONE",phone);
                intent.putExtra("EMAIL",email);
                intent.putExtra("NOTES",notes);
                intent.putExtra("ADDEDTIME",addedTime);
                intent.putExtra("UPDATEDTIME",updatedTime);

                intent.putExtra("isEditMode", true);
                context.startActivity(intent);

            }
        });

        holder.contactDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteContact(id);
                ((MainActivity)context).onResume();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView contactImage,contactNumberDial;
        TextView contactName, contactEdit, contactDelete;
        RelativeLayout relativeLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            contactImage = itemView.findViewById(R.id.contactImage);
            contactName = itemView.findViewById(R.id.contactName);
            contactNumberDial = itemView.findViewById(R.id.contactNumberDial);
            contactEdit = itemView.findViewById(R.id.contactEdit);
            contactDelete = itemView.findViewById(R.id.contactDelete);
            relativeLayout = itemView.findViewById(R.id.mainLayout);

        }
    }

}
