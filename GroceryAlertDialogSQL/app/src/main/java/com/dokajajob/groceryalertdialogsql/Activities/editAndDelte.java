package com.dokajajob.groceryalertdialogsql.Activities;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dokajajob.groceryalertdialogsql.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import Data.DataHandler;
import Model.Grocery;

public class editAndDelte extends AppCompatActivity {


    private AlertDialog.Builder alertDialog;
    private AlertDialog dialog;
    private List<Grocery> GroceryName;
    private LayoutInflater inflater;

    private EditText editTextGrocery;
    private EditText editTextQty;
    private Button buttonSaveToGroceries;
    private TextView textViewTitle;
    private Button noButton;
    private Button yesButton;

    private Context context;


  /*  public void editGrocery(final Grocery grocery){

        alertDialog = new AlertDialog.Builder(context);

        inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.popup_groceries, null);

        final EditText editTextGrocery = view.findViewById(R.id.editTextGrocery);
        final EditText editTextQty = view.findViewById(R.id.editTextQty);
        final Button buttonSaveToGroceries = view.findViewById(R.id.buttonSaveToGroceries);
        final TextView textViewTitle = view.findViewById(R.id.textViewTitle);

        textViewTitle.setText("Edit Grocery");

        alertDialog.setView(view);
        dialog = alertDialog.create();
        dialog.show();

        buttonSaveToGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataHandler dbHandler = new  DataHandler(context);

                //Update Grocery
                grocery.setName(editTextGrocery.getText().toString());
                grocery.setQty(editTextQty.getText().toString());

                if (!editTextGrocery.getText().toString().isEmpty()
                        && !editTextQty.getText().toString().isEmpty()) {
                    dbHandler.updateGrocery(grocery);

                }else {
                    Snackbar.make(v, "Enter For Update", Snackbar.LENGTH_LONG).show();

                }

                dialog.dismiss();

            }
        });


    }*/

    public void deleteGrocery(final int id){


        alertDialog = new AlertDialog.Builder(this);

        inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.delete_popup, null);

        //textViewTitle = view.findViewById(R.id.textViewTitle);
        noButton = view.findViewById(R.id.noButton);
        yesButton = view.findViewById(R.id.yesButton);

        alertDialog.setView(view);
        dialog = alertDialog.create();
        dialog.show();

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DataHandler dbHandler = new DataHandler(context);
                dbHandler.deleteGrocery(id);


                dialog.dismiss();

            }
        });



    }

}



