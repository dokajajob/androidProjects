package com.dokajajob.twoactivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewMain;
    private Button button;
    private final int REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        textViewMain = findViewById(R.id.textViewMain);
        textViewMain.setText(R.string.stam);
        String name = "Edd";

        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Second.class);
            intent.putExtra("m1", name);
            //startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE);
        });

/*        Second second = new Second();
        textViewMain.setText(second.message);
        String mes = textViewMain.getText().toString();
        Log.d("LOOOOOOOOOOOOOOOOG", mes);*/

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String res = data.getStringExtra("returnM");
                textViewMain.setText(res);


            }

        }

    }
}