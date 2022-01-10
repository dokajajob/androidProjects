package com.dokajajob.tipcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private EditText textEdit;
    private SeekBar seekBar;
    private TextView textPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textEdit = findViewById(R.id.textEdit);
        seekBar = findViewById(R.id.seekBar);
        textPercent = findViewById(R.id.textPercent);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textPercent.setVisibility(View.VISIBLE);
                textPercent.setText(seekBar.getProgress() + "%");
                String textPercentRes = textPercent.getText().toString();
                Log.d("textPercent", textPercentRes);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //textEdit.setText(null);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                AtomicBoolean flag = new AtomicBoolean(false);
                String stringInput = textEdit.getText().toString();
                for (int i = 0; i < stringInput.length(); i++) {
                    if (Character.isDigit(stringInput.charAt(i))) {
                        flag.set(true);
                    }
                    else {
                       Toast toast = Toast.makeText(MainActivity.this, "Numbers only", Toast.LENGTH_LONG);
                       toast.setGravity(Gravity.TOP, 0, 0);
                       toast.show();

                    }
                }
                if(flag.get()){
                    calculate();
                }
            }



            private void calculate() {
                if (!textEdit.getText().toString().equals("")){
                    String amount = textEdit.getText().toString();
                    int intAmount = Integer.parseInt(amount);
                    intAmount = intAmount + Integer.valueOf(seekBar.getProgress() * intAmount / 100);
                    amount = Integer.toString(intAmount);
                    Log.d("textEdit", amount);
                    textEdit.setText(amount);
                }else {
                    Toast.makeText(MainActivity.this, "You have to put Amount", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}