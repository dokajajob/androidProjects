package com.dokajajob.oo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = new User();
        user.name = "Edd";
        System.out.println(user.name);

        AnotherUser anotherUser = new AnotherUser();
        anotherUser.name = "Eddie";
        System.out.println(anotherUser.name);
        user.setUser("Lechtus");
        System.out.println(user.name);
    }
}