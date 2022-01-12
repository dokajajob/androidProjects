package com.dokajajob;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private TextView myText;
    private EditText enterName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layout);

            enterName = (EditText) findViewById(R.id.enterName);
            mButton = (Button) findViewById(R.id.myButton);
            mButton.setText(R.string.button_name);

            //myText = (TextView) findViewById(R.id.myText);
            //myText.setText(R.string.myText);

            mButton.setOnClickListener(v -> {
                String nameString;
                nameString = enterName.getText().toString();
                myText = (TextView) findViewById(R.id.myText);
                myText.setText(nameString);
                //myText.setText(R.string.Text);
            });



    }

//    public void showText(View view) {
/*        myText = (TextView) findViewById(R.id.myText);
        myText.setText(R.string.Text);*/
//    }
}