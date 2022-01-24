package com.dokajajob.grocerieslisted.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.LocusId;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dokajajob.grocerieslisted.Data.DatabaseHandler;
import com.dokajajob.grocerieslisted.Model.Grocery;
import com.dokajajob.grocerieslisted.R;
import com.google.android.material.snackbar.Snackbar;

public class DetailsActivity extends AppCompatActivity {
    private TextView itemName;
    private TextView quantity;
    private TextView dateAdded;
    private int groceryId;

    //private Context context;

    //private final List<Grocery> groceryItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public Button editButtonDet;
    public Button deleteButtonDet;


    public DetailsActivity(String gname, String gqty, Integer gkey) {
        Gname = gname;
        Gqty = gqty;
        Gkey = gkey;
    }

    public String Gname;
    public String Gqty;
    public Integer Gkey;

    public Integer getGkey() {
        return Gkey;
    }

    public void setGkey(Integer gkey) {
        Gkey = gkey;
    }



    public DetailsActivity() {
    }


    public String getGname() {
        return Gname;
    }

    public void setGname(String gname) {
        Gname = gname;
    }

    public String getGqty() {
        return Gqty;
    }

    public void setGqty(String gqty) {
        Gqty = gqty;
    }



/*    public DetailsActivity(Context context) {
         this.context = context;
    }*/




    public void onCreate(Bundle savedInstanceState) {

        //context = ctx;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemName = (TextView) findViewById(R.id.itemNameDet);
        quantity = (TextView) findViewById(R.id.quantityDet);
        dateAdded = (TextView) findViewById(R.id.dateAddedDet);

        Grocery grocery = new Grocery();



        Log.d("DEBUG: ", String.valueOf(grocery));


        Bundle bundle = getIntent().getExtras();

/*        Log.d("grocery", grocery.getName().toString());
        Log.d("grocery", grocery.getQuantity().toString());*/

        if ( bundle != null ) {
            itemName.setText(bundle.getString("name"));
            quantity.setText(bundle.getString("quantity"));
            dateAdded.setText(bundle.getString("date"));
            groceryId = bundle.getInt("id");
        }

        Log.d("AAAAA:", grocery.toString());



        Gname = bundle.getString("name");
        Gqty = bundle.getString("quantity");

        Log.d("Gname isssss:", Gname.toString());
        Log.d("Gqty isssdss:", Gqty.toString());

        Log.d("DDDDD:", grocery.toString());

        editAnddelete();

    }

    public void editAnddelete(){

        editButtonDet = (Button) findViewById(R.id.editButtonDet);
        deleteButtonDet = (Button) findViewById(R.id.deleteButtonDet);

        //In case of Edit
        editButtonDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Grocery grocery = new Grocery();


                Bundle bundle = getIntent().getExtras();
                grocery.setName(bundle.getString("name"));
                grocery.setQuantity(bundle.getString("quantity"));

                Log.d("DEBUGGGGG: ", String.valueOf(grocery.getName().toString()));

                //DetailsActivity detailsActivity = new DetailsActivity();

                editItem(grocery);
            }
        });

        //In case of Delete
        deleteButtonDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Grocery grocery = new Grocery();
                deleteItem(groceryId);
            }
        });

    }




    public void deleteItem(final int id) {

        //id
        Log.d("DEBUG: ", String.valueOf(id));

        //create an AlertDialog
        alertDialogBuilder = new AlertDialog.Builder(DetailsActivity.this);

        inflater = LayoutInflater.from(DetailsActivity.this);
        View view = inflater.inflate(R.layout.confirmation_dialog, null);

        Button noButton = (Button) view.findViewById(R.id.noButton);
        Button yesButton = (Button) view.findViewById(R.id.yesButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
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
                //delete the item.
                DatabaseHandler db = new DatabaseHandler(DetailsActivity.this);
                //delete item
                db.deleteGrocery(id);
                dialog.dismiss();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                        //Snackbar.make(view, "Deleted Grocery", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(DetailsActivity.this, "Deleted Grocery", Toast.LENGTH_LONG).show();


                    }
                }, 2000);


            }
        });

    }


    public void editItem(Grocery grocery) {


        //DetailsActivity detailsActivity = new DetailsActivity();
        //Bundle bundle = getIntent().getExtras();

/*        detailsActivity.setGname(bundle.getString("name"));
        detailsActivity.setGqty(bundle.getString("quantity"));
        detailsActivity.setGkey(bundle.getInt("id"));*/
        //Gkey = bundle.getInt("id");

/*
        Log.d("detailctivity", detailsActivity.getGname().toString());
        Log.d("detailsActivity", detailsActivity.getGqty().toString());
        Log.d("KEY ISSSS: ", detailsActivity.getGkey().toString());

        //grocery
        Log.d("DEBUG: ", String.valueOf(detailsActivity));*/

        alertDialogBuilder = new AlertDialog.Builder(DetailsActivity.this);

        inflater = LayoutInflater.from(DetailsActivity.this);
        final View view = inflater.inflate(R.layout.popup, null);

        final EditText groceryItem = (EditText) view.findViewById(R.id.groceryItem);
        final EditText quantity = (EditText) view.findViewById(R.id.groceryQty);
        final TextView title = (TextView) view.findViewById(R.id.tile);

        title.setText("Edit Grocery");
        Button saveButton = (Button) view.findViewById(R.id.saveButton);


        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHandler db = new DatabaseHandler(DetailsActivity.this);

                //Update item
                grocery.setName(groceryItem.getText().toString());
                grocery.setQuantity(quantity.getText().toString());
                grocery.setId(groceryId);
                Log.d("IDDDDDDD: ", String.valueOf(grocery.getId()));

                if (!groceryItem.getText().toString().isEmpty()
                        && !quantity.getText().toString().isEmpty()) {
                    Log.d("DEBUGDetailsssss: ", String.valueOf(grocery.getName().toString()));
                    db.updateGrocery(grocery);
                }else {
                    Snackbar.make(v, "Add Grocery and Quantity", Snackbar.LENGTH_LONG).show();
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        dialog.dismiss();
                        startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                        //Snackbar.make(view, "Edited Grocery and Quantity", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(DetailsActivity.this, "Edited Grocery and Quantity", Toast.LENGTH_LONG).show();

                    }
                }, 2000);


            }
        });

    }
}
