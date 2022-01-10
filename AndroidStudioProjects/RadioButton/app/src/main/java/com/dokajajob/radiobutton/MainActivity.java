package com.dokajajob.radiobutton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
                switch (radioButton.getId()){
                    case R.id.radioYes:{
                        Log.d("RD", "Yes");

                    }
                    break;

                    case R.id.radioNo:{
                        Log.d("RD", "No");

                    }
                    break;

                    case R.id.radioMaybe:{
                        Log.d("RD", "Maybe");

                    }
                    break;
                }
            }
        });
    }
}