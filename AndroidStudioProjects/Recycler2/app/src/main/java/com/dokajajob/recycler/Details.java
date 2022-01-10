package com.dokajajob.recycler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Details extends AppCompatActivity {

    private TextView textViewDetails;
    private Button buttonDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        textViewDetails = findViewById(R.id.textViewDetails);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            textViewDetails.setText(extras.getString("ImageName"));
        }

        buttonDetails = findViewById(R.id.buttonDetails);
        buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = getIntent();
                Log.d("DEBUG", extras.getString("ImageName"));
                returnIntent.putExtra("skipp1Response", extras.getString("ImageName" + "Returned"));
                setResult(RESULT_OK, returnIntent);

                finish();
            }
        });



    }
}