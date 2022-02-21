package com.dokajajob.weatherapi.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dokajajob.weatherapi.R;

import java.util.ArrayList;
import java.util.List;

import Data.DataHandlerAdapter;
import Model.CityItems;
import Ui.RecyclerViewAdapter;

public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    public List<CityItems> listItems;
    private String itemName;
    private String temperature;
    private String dateAdded;
    private int cityId;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        //Bundle bundle = getIntent().getExtras();
        CityItems cityItems = new CityItems();

/*        if ( bundle != null ) {
            cityItems.setCityName(bundle.getString("cityName"));
            cityItems.setCityTemperature(bundle.getString("temperature"));
            cityItems.setImageName(bundle.getString("png"));
            cityItems.setDateItemAdded(bundle.getString("date"));
        }
        listItems.add(cityItems);*/

        //Get from DB
        DataHandlerAdapter db = new DataHandlerAdapter(RecyclerActivity.this);
        listItems = db.getAllCities();
        Log.d("listItems: ", String.valueOf(listItems));

        //Pass to Recycler
        recyclerViewAdapter = new RecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        //Go back to Main
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecyclerActivity.this, MainActivity.class));
            }
        });


    }


}