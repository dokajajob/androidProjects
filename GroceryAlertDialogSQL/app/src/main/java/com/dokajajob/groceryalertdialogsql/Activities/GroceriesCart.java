package com.dokajajob.groceryalertdialogsql.Activities;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dokajajob.groceryalertdialogsql.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Data.DataHandler;
import Model.Grocery;

public class GroceriesCart extends AppCompatActivity {

    private ArrayList<String> mGroceries = new ArrayList<>();
    private ArrayList<String> mQty = new ArrayList<>();
    private DataHandler dbHandler;
    private RecyclerView RecyclerGroceries;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Grocery> listGroceries;
    private List<Grocery> ListItems;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    public EditText editTextGrocery;
    public EditText editTextQty;
    public EditText textViewTitle;
    public Button buttonSaveToGroceries;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries_cart);

        //loadGroceriesFrom_getGrocery();
        loadGroceries();

        fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                createPupDialog();


            }
        });



/*    //Example
    private void loadGroceriesFrom_getGrocery(){


        DataHandler dbHandler = new DataHandler(this);

        //Get by specific grocery id
        //List<Grocery>  listGrocery = dbHandler.getGrocery(10);

        //All groceries
        for (int i = 1; i < dbHandler.countGroceries(); i++){
            List<Grocery>  listGrocery = dbHandler.getGrocery(i);

            for (Grocery c : listGrocery){
                String log = "ID: " + c.getId() + " Name : " + c.getName() + " Qty :" + c.getQty();
                String joined = TextUtils.join(", ", Collections.singleton(log));
                Log.d("INFO", String.valueOf(joined));

            }

        }

        initRecyclerView();

    }*/
    }


    //Load from Recycler
    private void loadGroceries(){

        RecyclerGroceries = findViewById(R.id.RecyclerGroceries);
        RecyclerGroceries.setHasFixedSize(true);
        RecyclerGroceries.setLayoutManager(new LinearLayoutManager(this));

        listGroceries = new ArrayList<>();
        ListItems = new ArrayList<>();

        dbHandler = new DataHandler(this);

        //Get all from Database
        listGroceries = dbHandler.getAllGroceries();

        for (Grocery c : listGroceries) {
            Grocery grocery = new Grocery();
            grocery.setName(c.getName());
            grocery.setQty("Qty is: " + c.getQty());
            grocery.setId(c.getId());

            ListItems.add(grocery);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, ListItems);
        RecyclerGroceries.setAdapter(recyclerViewAdapter);
        RecyclerGroceries.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter.notifyDataSetChanged();


    }

    //Add new Grocery
    private void createPupDialog() {

    dialogBuilder = new AlertDialog.Builder(this);
    View view = getLayoutInflater().inflate(R.layout.popup_groceries, null);

        editTextGrocery = view.findViewById(R.id.editTextGrocery);
        editTextQty = view.findViewById(R.id.editTextQty);
        //textViewTitle = view.findViewById(R.id.textViewTitle);
        buttonSaveToGroceries = view.findViewById(R.id.buttonSaveToGroceries);

        textViewTitle.setText("Create New Grocery");

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        buttonSaveToGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToGrocery(v);
            }
        });


    }

    private void saveToGrocery(View v) {

        Grocery grocery = new Grocery();

        String newGrocery = editTextGrocery.getText().toString();
        String newQty = editTextQty.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQty(newQty);

        //Save to DB
        dbHandler.addGrocery(grocery);

        Snackbar.make(v, "Grocery Saved", Snackbar.LENGTH_LONG).show();

        Log.d("ITEM ADDED : ", String.valueOf(grocery));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(GroceriesCart.this, GroceriesCart.class));

            }
        }, 1200);
    }

}