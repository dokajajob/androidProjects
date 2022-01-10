package com.dokajajob.moreactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SecondSkipp extends AppCompatActivity {

    //private ToggleButton toggleButtonSkipp2;
    private TextView textView_skipp2Activity;
    private Button buttonAgree2;
    private Button buttonDisagree2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_skipp);

        //toggleButtonSkipp1 = findViewById(R.id.toggleButtonSkipp1);
        textView_skipp2Activity = findViewById(R.id.textView_skipp2Activity);
        buttonAgree2 = findViewById(R.id.buttonAgree2);
        buttonDisagree2 = findViewById(R.id.buttonDisagree2);

        buttonAgree2.setOnClickListener(v -> {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                textView_skipp2Activity.setText(extras.getString("skipp2_name"));
            }
            Intent returnIntentFromSkipp2 = getIntent();
            String res = "Skipp Alex Agree." + "\n" + "To take you to the ride of your life!";
            //Log.d("AAAAAAAAAAA", res);
            returnIntentFromSkipp2.putExtra("skipp2Response", res);
            setResult(RESULT_OK, returnIntentFromSkipp2);

            finish();
        });

        buttonDisagree2.setOnClickListener(v -> {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                textView_skipp2Activity.setText(extras.getString("skipp2_name"));
            }
            Intent returnIntentFromSkipp2 = getIntent();
            String res = "Skipp Alex Disagree." + "\n" + "Keep looking!";
            //Log.d("BBBBBBBB", res);
            returnIntentFromSkipp2.putExtra("skipp2Response", res);
            setResult(RESULT_OK, returnIntentFromSkipp2);

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
        Toast toast = Toast.makeText(SecondSkipp.this, "Going To First Activity", Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();

    }
}