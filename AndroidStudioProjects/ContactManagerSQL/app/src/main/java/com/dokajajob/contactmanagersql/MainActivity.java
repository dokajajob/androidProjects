package com.dokajajob.contactmanagersql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Data.DatabaseHandler;
import Model.Contact;

public class MainActivity extends AppCompatActivity {

    private TextView textViewPersonName;
    private TextView textViewPersonPhone;
    private Button buttonSave;
    private Button buttonShow;
    private EditText editTextTextMultiLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPersonName = findViewById(R.id.editTextPersonName);
        textViewPersonPhone = findViewById(R.id.editTextPersonPhone);
        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);
        buttonSave = findViewById(R.id.buttonSave);
        buttonShow = findViewById(R.id.buttonShow);

        DatabaseHandler db = new DatabaseHandler(this);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Insert contacts
                Log.d("Insert: ", "Insertin...");
                db.addContact(new Contact(textViewPersonName.getText().toString(), textViewPersonPhone.getText().toString()));

            }
        });

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Read all contacts
                Log.d("Reading: ", "Reading all contacts...");
                List<Contact> contactList = db.getAllContacts();

                List<String> listToPresent = new ArrayList<String>();

                for (Contact c : contactList){
                    String log = "ID: " + c.getId() + " , Name: " + c.getName() + " , Phone: " + c.getPhone();
                    Log.d("Name: ", log);
                    listToPresent.add(log);
                    Log.d("logArray: ", String.valueOf(listToPresent));
                    String joined = TextUtils.join(", ", listToPresent);
                    editTextTextMultiLine.setText(joined);

                }

            }
        });


    }
}