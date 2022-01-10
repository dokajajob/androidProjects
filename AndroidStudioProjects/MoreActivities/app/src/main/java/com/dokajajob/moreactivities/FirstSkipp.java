package com.dokajajob.moreactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FirstSkipp extends AppCompatActivity {

    //private ToggleButton toggleButtonSkipp1;
    private TextView textView_skipp1Activity;
    private Button buttonAgree1;
    private Button buttonDisagree1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_skipp);

        //toggleButtonSkipp1 = findViewById(R.id.toggleButtonSkipp1);
        textView_skipp1Activity = findViewById(R.id.textView_skipp1Activity);
        buttonAgree1 = findViewById(R.id.buttonAgree1);
        buttonDisagree1 = findViewById(R.id.buttonDisagree1);

        buttonAgree1.setOnClickListener(v -> {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                textView_skipp1Activity.setText(extras.getString("skipp1_name"));
            }
            Intent returnIntentFromSkipp1 = getIntent();
            String res = "Skipp Emma Agree." + "\n" + "To take you to the ride of your life!";
            //Log.d("AAAAAAAAAAA", res);
            returnIntentFromSkipp1.putExtra("skipp1Response", res);
            setResult(RESULT_OK, returnIntentFromSkipp1);

            finish();
        });

        buttonDisagree1.setOnClickListener(v -> {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                textView_skipp1Activity.setText(extras.getString("skipp1_name"));
            }
            Intent returnIntentFromSkipp1 = getIntent();
            String res = "Skipp Emma Disagree." + "\n" + " Keep looking!";
            //Log.d("BBBBBBBB", res);
            returnIntentFromSkipp1.putExtra("skipp1Response", res);
            setResult(RESULT_OK, returnIntentFromSkipp1);

            finish();
        });



/*
        toggleButtonSkipp1.setOnCheckedChangeListener((buttonView, isChecked) -> {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                textView_skipp1Activity.setText(extras.getString("skipp1_name"));
            }
            Intent returnIntentFromSkipp1 = getIntent();
            Boolean ToggleButtonState = toggleButtonSkipp1.isChecked();
            String res = String.valueOf(toggleButtonSkipp1);
            Log.d("AAAAAAAAAAA", res);
            returnIntentFromSkipp1.putExtra("skipp1Response", res);
            setResult(RESULT_OK, returnIntentFromSkipp1);

            finish();
        });
*/


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast toast = Toast.makeText(FirstSkipp.this, "Going To First Activity", Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();

    }
}