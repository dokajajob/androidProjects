package com.dokajajob.meterstoinches;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private TextView input;
    private ImageView contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (TextView) findViewById(R.id.input);
        contract = (ImageView)  findViewById(R.id.contract);



        contract.setOnClickListener(v -> {
            AtomicBoolean flag = new AtomicBoolean(false);
            String stringInput = input.getText().toString();
            for (int i = 0; i < stringInput.length(); i++){
                if (Character.isDigit(stringInput.charAt(i))){
                    flag.set(true);
                }
                else {
                    input.setText("Only Numbers!");
                }
            }
            if (flag.get()){
                String res = calc(stringInput);
                input.setText(res);
            }

        });

    }

    public String calc(String toInches) {
        double calculatedInches = Double.parseDouble(toInches) * 39.37;
        toInches = Double.toString(calculatedInches);
        return(toInches);

    }
}