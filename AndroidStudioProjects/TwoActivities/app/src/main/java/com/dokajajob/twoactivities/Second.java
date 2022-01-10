package com.dokajajob.twoactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Second extends AppCompatActivity {

    public String message;
    private Button buttonSecond;

    private TextView textViewSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textViewSecond = findViewById(R.id.textViewSecond);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            textViewSecond.setText(extras.getString("m1"));
            message = extras.getString("m1");

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                finish();
            }, 5000);
        }

        buttonSecond = findViewById(R.id.buttonSecond);

        buttonSecond.setOnClickListener(v -> {
            Intent returnIntent = getIntent();
            returnIntent.putExtra("returnM", "from second");
            setResult(RESULT_OK, returnIntent);
       });

    }
}