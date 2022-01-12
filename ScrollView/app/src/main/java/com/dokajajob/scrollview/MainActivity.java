package com.dokajajob.scrollview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CheckBox checkBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBox = findViewById(R.id.checkBox);

}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);



        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable(){

            @Override
            public void run() {
                checkBox.setVisibility(View.INVISIBLE);

            }
        }, 2000);
        return super.onTouchEvent(event);
    }
}
