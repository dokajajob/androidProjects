package com.dokajajob.groceryalertdialogsql.Activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.dokajajob.groceryalertdialogsql.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import Data.DataHandler;
import Model.Grocery;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
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


    public RecyclerViewAdapter(Context Context, List<Grocery> groceryName) {
        this.GroceryName = groceryName;
        this.context = Context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_grocery, parent, false);
        ViewHolder holder = new ViewHolder(view, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("INFO :", "View holder called " + GroceryName);

        Grocery grocery = GroceryName.get(position);

        holder.textView.setText(grocery.getName());
        holder.textViewQ.setText(grocery.getQty());


    }

    @Override
    public int getItemCount() {
        return GroceryName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;
        TextView textViewQ;
        ImageButton imageButtonE;
        ImageButton imageButtonD;
        RelativeLayout parentLayout;
        public int id;

        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);

            context = ctx;

            textView = view.findViewById(R.id.textViewItem);
            textViewQ = view.findViewById(R.id.textViewQty);
            imageButtonE = view.findViewById(R.id.imageButtonEdit);
            imageButtonD = view.findViewById(R.id.imageButtonDelete);
            parentLayout = view.findViewById(R.id.parentLayout);

            imageButtonE.setOnClickListener(this);
            imageButtonD.setOnClickListener(this);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to Details Activity
                    int position = getAdapterPosition();

                    Grocery grocery = GroceryName.get(position);
                    Intent intent = new Intent(context, GroceriesDetails.class);
                    intent.putExtra("Name", grocery.getName());
                    intent.putExtra("Qty", grocery.getQty());
                    intent.putExtra("Id", grocery.getId());
                    context.startActivity(intent);
                }
            });

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageButtonEdit:

                    int position = getAdapterPosition();
                    Grocery grocery = GroceryName.get(position);

                    editGrocery(grocery);

                    break;

            case R.id.imageButtonDelete:

                position = getAdapterPosition();
                grocery = GroceryName.get(position);
                deleteGrocery(grocery.getId());

                break;

        }
    }

    public void editGrocery(final Grocery grocery){

            alertDialog = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup_groceries, null);

            final EditText editTextGrocery = view.findViewById(R.id.editTextGrocery);
            final EditText editTextQty = view.findViewById(R.id.editTextQty);
            final Button buttonSaveToGroceries = view.findViewById(R.id.buttonSaveToGroceries);
            //final TextView textViewTitle = view.findViewById(R.id.textViewTitle);

            //textViewTitle.setText("Edit Grocery");

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
                        notifyItemChanged(getAdapterPosition(), grocery);
                    }else {
                        Snackbar.make(v, "Enter For Update", Snackbar.LENGTH_LONG).show();

                    }

                    dialog.dismiss();

                }
            });


         }

        public void deleteGrocery(final int id){

            alertDialog = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
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
                    notifyItemChanged(getAdapterPosition(), id);
                    GroceryName.remove(getAdapterPosition());

                    dialog.dismiss();

                }
            });



        }

    }


}
