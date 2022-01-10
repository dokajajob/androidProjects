package com.dokajajob.randomcolor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button myButton;
    private View linearlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myButton = findViewById(R.id.myButton);
        linearlayout = findViewById(R.id.linearlayout);

        int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.BLACK};

        Random rnd = new Random();



        myButton.setOnClickListener(v -> {

            int int_random = rnd.nextInt(colors.length);
            linearlayout.setBackgroundColor(colors[int_random]);

        });

    }
}