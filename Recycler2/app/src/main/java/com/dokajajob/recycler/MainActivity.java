package com.dokajajob.recycler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImagesURLS = new ArrayList<>();
    private final int REQUEST_CODE_1 = 1;
    private TextView skipp1_response;
    private ConstraintLayout LayoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initImageBitmaps();

    }

    private void initImageBitmaps(){
            mImagesURLS.add(R.drawable.e1);
            mNames.add("Endurance near Arran");

            mImagesURLS.add(R.drawable.ic_launcher_foreground);
            mNames.add("Endurance near Largs");

            initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.RecyclerId);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImagesURLS);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_1){
            if (resultCode == RESULT_OK){
                skipp1_response = findViewById(R.id.skipp1_response);
                Log.d("DEBUG","InMain");
                skipp1_response.setText(data.getStringExtra("skipp1Response"));
                Log.d("DEBUG", skipp1_response.getText().toString());
            }
        }
        if (skipp1_response.getText().toString() != null){
            Toast toast = Toast.makeText(this, "Got Response", Toast.LENGTH_LONG);
        }
        else {
            //Toast toast = Toast.makeText(this, "FAILED To Get Response", Toast.LENGTH_LONG);
            LayoutMain = findViewById(R.id.LayoutMain);
            LayoutMain.setVisibility(View.INVISIBLE);
        }

    }


}