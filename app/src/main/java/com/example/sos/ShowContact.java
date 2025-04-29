package com.example.sos;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowContact extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ContactModel> contactList;
    ContactRecyclerAdapter adapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);

        recyclerView = findViewById(R.id.contactRecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        loadContacts();
    }

    private void loadContacts() {
        contactList.clear();
        Cursor cursor = databaseHelper.getAllData();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String number = cursor.getString(2);

                contactList.add(new ContactModel(id, name, number));
            }
            cursor.close();
        }

        adapter = new ContactRecyclerAdapter(this, contactList);
        recyclerView.setAdapter(adapter);
    }
}
