package com.dokajajob.skipphelp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private static final String TAG = "MAIN";
    public EditText user;
    public EditText password;
    public Button button;
    public String userName;
    private String userPasswd;
    private Button buttonSignOut;
    private Button buttonCreate;
    private GifImageView loginImage;
    private GifImageView logoutImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("message");
        databaseReference = database.getReference("ok");

        databaseReference.setValue("Hello, World!");
        databaseReference.setValue("ed");

        //Write to DB
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Toast.makeText(MainActivity.this, value, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Auth change
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user signed in
                    Log.d(TAG , " signed in" );
                    Log.d(TAG, user.getEmail());
                } else {
                    Log.d(TAG , " signed out");
                    Toast.makeText(MainActivity.this, "User signed out", Toast.LENGTH_LONG).show();
                }

            }
        };

        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        loginImage = findViewById(R.id.loginImage);

        //Log-in
        loginImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = user.getText().toString();
                userPasswd = password.getText().toString();

                if (!userName.equals("") && !userPasswd.equals("")) {
                    firebaseAuth.signInWithEmailAndPassword(userName, userPasswd)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //Log.d(TAG, userName + " is logged in");
                                        Toast.makeText(MainActivity.this,   userName + " is logged in", Toast.LENGTH_LONG).show();
                                        Log.d(TAG, firebaseAuth.getCurrentUser().toString());

                                    } else {

                                        //Log.d(TAG, userName + " failed to log in");
                                        Toast.makeText(MainActivity.this,  userName + " is failed to log in", Toast.LENGTH_LONG).show();
                                    }


                                }
                            });


                } else {
                    Toast.makeText(MainActivity.this, "Enter credetials first", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Log-out
        logoutImage = findViewById(R.id.logoutImage);
        logoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth.signOut();

            }
        });

        //Create User
        buttonCreate = findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                userName = user.getText().toString();
                userPasswd = password.getText().toString();

                if (!userName.equals("") && !userPasswd.equals("")) {

                    firebaseAuth.createUserWithEmailAndPassword(userName, userPasswd)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        databaseReference = database.getReference("created");
                                        databaseReference.setValue(userName);
                                        Toast.makeText(MainActivity.this, userName + " created", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, userName + " failed to create user", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(MainActivity.this, "enter credentials first", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);

        }

    }

}