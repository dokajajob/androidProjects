package com.dokajajob.moreactivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView skipp1;
    private ImageView skipp2;
    private final int REQUEST_CODE_S1 = 1;
    private final int REQUEST_CODE_S2 = 2;
    private TextView skipp1_response;
    private TextView skipp2_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        skipp1 = findViewById(R.id.skipp1);
        skipp2 = findViewById(R.id.skipp2);
        skipp1_response = findViewById(R.id.skipp1_response);
        skipp1_response.setText(getString(R.string.def_value));
        skipp2_response = findViewById(R.id.skipp2_response);
        skipp2_response.setText(getString(R.string.def_value2));

        skipp1.setOnClickListener(v -> {
            Intent intentSkipp1 = new Intent(MainActivity.this, FirstSkipp.class);
            intentSkipp1.putExtra("skipp1_name", "Emma");
            startActivityForResult(intentSkipp1, REQUEST_CODE_S1);
        });

        skipp2.setOnClickListener(v -> {
            Intent intentSkipp2 = new Intent(MainActivity.this, SecondSkipp.class);
            intentSkipp2.putExtra("skipp2_name", "Alex");
            startActivityForResult(intentSkipp2, REQUEST_CODE_S2);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_S1){
            if (resultCode == RESULT_OK){
                skipp1_response = findViewById(R.id.skipp1_response);
                skipp1_response.setText(data.getStringExtra("skipp1Response"));
            }
        }
        if (requestCode == REQUEST_CODE_S2){
            if (resultCode == RESULT_OK){
                skipp2_response = findViewById(R.id.skipp2_response);
                String responseSkipp2str = data.getStringExtra("skipp2Response");
                skipp2_response.setText(responseSkipp2str);
            }
        }
    }
}