package com.dokajajob.musicbox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.MediaPlayer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageButton imageButtonPlay;
    private ImageButton imageButtonForward;
    private ImageButton imageButtonBackward;
    private SeekBar seekBar;
    private TextView textViewStart;
    private TextView textViewStop;
    private ImageView imageView;
    ArrayList<Integer> playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        playlist = new ArrayList<>();
        playlist.add(R.raw)





    }
}