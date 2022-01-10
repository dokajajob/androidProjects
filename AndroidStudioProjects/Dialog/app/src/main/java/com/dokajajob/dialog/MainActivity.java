package com.dokajajob.dialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private AlertDialog.Builder alertDialog;
    private TextView textView;
    private int count = 0;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);


        button.setOnClickListener(v -> {

            count++;

            textView = findViewById(R.id.textView);
            textView.setVisibility(View.INVISIBLE);

            imageView = findViewById(R.id.imageView);
            imageView.setVisibility(View.INVISIBLE);

            alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle(R.string.titleDialog);
            alertDialog.setMessage(R.string.messageDialog);
            alertDialog.setCancelable(false);
            alertDialog.setIcon(R.drawable.ic_launcher_background);
            if (count<=2) {
                alertDialog.setPositiveButton(getResources().getString(R.string.yes),
                        (dialog, which) ->
                                MainActivity.this.textViewMethod());
            }else{
                alertDialog.setPositiveButton(getResources().getString(R.string.yes),
                        (dialog, which) ->
                                MainActivity.this.pictureMethod());

            }


            alertDialog.setNegativeButton(getResources().getString(R.string.no),
                    (dialog, which) -> dialog.cancel());


            AlertDialog dialog = alertDialog.create();
            dialog.show();
        });

    }

    private void pictureMethod() {
        //imageView = findViewById(R.id.imageView);
        imageView.setVisibility(View.VISIBLE);

    }

    private void textViewMethod() {
        //textView = findViewById(R.id.textView);
        textView.setVisibility(View.VISIBLE);
        textView.setText(R.string.haha_no_picture);
    }

}