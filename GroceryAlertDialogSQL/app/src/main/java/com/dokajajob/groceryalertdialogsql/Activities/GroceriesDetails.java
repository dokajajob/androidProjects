package com.dokajajob.groceryalertdialogsql.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import com.dokajajob.groceryalertdialogsql.R;

import java.util.List;

import Data.DataHandler;
import Model.Grocery;

public class GroceriesDetails extends AppCompatActivity {

    private TextView textViewGroceryDet;
    private TextView textViewQtyDet;
    private TableRow tableRowDet;
    private Button buttonDetEdit;
    private Button buttonDetDelete;
    private int groceryId;
    private Button buttonGoBack;

    private Context context;
    private AlertDialog.Builder alertDialog;
    private AlertDialog dialog;
    private List<Grocery> GroceryName;
    private LayoutInflater inflater;

    public EditText editTextGrocery;
    public EditText editTextQty;
    public EditText textViewTitle;
    public Button buttonSaveToGroceries;
    private Button noButton;
    private Button yesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries_details);

        textViewGroceryDet = findViewById(R.id.textViewGroceryDet);
        textViewQtyDet = findViewById(R.id.textViewQtyDet);
        tableRowDet = findViewById(R.id.tableRowDet);
        buttonDetEdit = findViewById(R.id.buttonDetEdit);
        buttonDetDelete = findViewById(R.id.buttonDetDelete);
        buttonGoBack = findViewById(R.id.buttonGoBack);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            textViewGroceryDet.setText(bundle.getString("Name"));
            textViewQtyDet.setText(bundle.getString("Qty"));
            groceryId = bundle.getInt("Id");

        }


        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

/*        buttonDetEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAndDelte editAndDelte = new editAndDelte();

                int position = getAdapterPosition();
                Grocery grocery = GroceryName.get(position);

                editAndDelte.editGrocery(grocery);
            }
        });*/

        buttonDetDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Grocery grocery = new Grocery();
                deleteItem(grocery.getId());
            }
        });

    }

    private void deleteItem(final int id) {

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