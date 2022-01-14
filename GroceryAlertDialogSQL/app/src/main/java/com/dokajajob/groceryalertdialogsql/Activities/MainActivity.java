package com.dokajajob.groceryalertdialogsql.Activities;

import android.os.Bundle;

import com.dokajajob.groceryalertdialogsql.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dokajajob.groceryalertdialogsql.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Data.DataHandler;
import Model.Grocery;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText editTextGrocery;
    private EditText editTextQty;
    private Button buttonSaveToGroceries;
    private EditText editTextAllGroceries;
    private DataHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //Data Handler
        dbHandler = new DataHandler(this);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                //Call for PopUpDialog
                createPopupDialog();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void createPopupDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_groceries, null);

        editTextGrocery = view.findViewById(R.id.editTextGrocery);
        editTextQty = view.findViewById(R.id.editTextQty);
        buttonSaveToGroceries = view.findViewById(R.id.buttonSaveToGroceries);
        editTextAllGroceries = view.findViewById(R.id.editTextAllGroceries);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        buttonSaveToGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editTextGrocery.getText().toString().isEmpty() && !editTextQty.getText().toString().isEmpty()) {
                    saveToDB(v);

                } else { Toast.makeText(MainActivity.this, "Enter Grocery To Save", Toast.LENGTH_LONG).show(); }

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

            }
        }, 1000; //Delayed 1 second
    }

    }
}