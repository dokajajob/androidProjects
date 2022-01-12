package com.dokajajob.checkbox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //private TextView textView;
    private CheckBox Die_Hard;
    private CheckBox Matrix;
    private CheckBox Speed;
    private Button button;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Die_Hard = findViewById(R.id.Die_Hard);
        Matrix = findViewById(R.id.Matrix);
        Speed = findViewById(R.id.Speed);
        resultText = findViewById(R.id.resultText);
        button = findViewById(R.id.button);


        button.setOnClickListener(v -> {
            StringBuilder str = new StringBuilder();
            if(Die_Hard.isChecked() && !Matrix.isChecked() && !Speed.isChecked()) {
                str.append(Die_Hard.getText().toString() + " Status is + " + Die_Hard.isChecked() + "\n");
                Log.d("SB", String.valueOf(str));
            }
            else if(Die_Hard.isChecked() && Matrix.isChecked()) {
                str.append(Die_Hard.getText().toString() + " Status is + " + Die_Hard.isChecked() + "\n");
                str.append(Matrix.getText().toString() + " Status is + " + Matrix.isChecked() + "\n");
                Log.d("SB", String.valueOf(str));
            }
            else if(Speed.isChecked()) {
                str.append(Speed.getText().toString() + " Status is + " + Speed.isChecked() + "\n");
            }
            resultText.setText(str);
        });

    }

}


