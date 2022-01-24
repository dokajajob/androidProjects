package com.dokajajob.groceryalertdialogsql.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dokajajob.groceryalertdialogsql.R;
import com.google.android.material.snackbar.Snackbar;

import Data.DataHandler;
import Model.Grocery;

public class PopUpCreateGrocery extends AppCompatActivity {

    public AlertDialog.Builder dialogBuilder;
    public AlertDialog dialog;
    public EditText editTextGrocery;
    public EditText editTextQty;
    public Button buttonSaveToGroceries;
    public DataHandler dbHandler;
    public TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pop_up_create_grocery);

        createPopupDialog();


    }

    public void createPopupDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_groceries, null);

        editTextGrocery = view.findViewById(R.id.editTextGrocery);
        editTextQty = view.findViewById(R.id.editTextQty);
        buttonSaveToGroceries = view.findViewById(R.id.buttonSaveToGroceries);
        //textViewTitle = view.findViewById(R.id.textViewTitle);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        //textViewTitle.setText("Create New Grocery");

        buttonSaveToGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editTextGrocery.getText().toString().isEmpty() && !editTextQty.getText().toString().isEmpty()) {
                    saveToDB(v);

                } else { Toast.makeText(PopUpCreateGrocery.this, "Enter Grocery To Save", Toast.LENGTH_LONG).show(); }

            }

        });

    }

    public void saveToDB(View v){

        //Insert Grocery
        String newGrocery = editTextGrocery.getText().toString();
        String newQty = editTextQty.getText().toString();

        Grocery grocery = new Grocery();
        grocery.setName(newGrocery);
        grocery.setQty(newQty);

        dbHandler.addGrocery(grocery);

        //Get All Groceries
/*                List<Grocery> groceriesList = dbHandler.getAllGroceries();
                Log.d("groceriesList ", String.valueOf(groceriesList));
                List<String> groceryToPresent = new ArrayList<>();

                if (groceriesList != null){

                    for (Grocery c : groceriesList){
                        Integer id = c.getId();
                        String StringId = id.toString();
                        String log = "ID: " + StringId + "Name: " + c.getName() + "Qty: " + c.getQty();
                        Log.d("Grocery: ", log);
                        groceryToPresent.add(log);
                        Log.d("All Groceries", String.valueOf(groceryToPresent));
                        String joined = TextUtils.join(", ", groceryToPresent);
                        //editTextAllGroceries.setText(joined);
                    }

                }*/

        Snackbar.make(v, "Item saved!", Snackbar.LENGTH_LONG).show();
        Log.d("Groceries Count is: ", String.valueOf(dbHandler.countGroceries()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //Start new activity
                //Start Groceries Activity
                startActivity(new Intent(PopUpCreateGrocery.this, GroceriesCart.class));

            }
        }, 1000); //Delayed 1 second

    }

}