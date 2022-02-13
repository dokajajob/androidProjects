package com.dokajajob.skipphelp.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dokajajob.skipphelp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;

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
    private ImageView buttonCreate;
    private GifImageView loginImage;
    private GifImageView logoutImage;
    private FirebaseFunctions mFunctions;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();
        //database = FirebaseDatabase.getInstance();

        //Auth change
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                //String uID = user.getUid();

                if (user != null) {
                    //user signed in
                    Log.d(TAG , " signed in" );
                    Log.d(TAG, user.getEmail());
                    Toast.makeText(MainActivity.this,  user.getEmail() + " signed in", Toast.LENGTH_LONG).show();
                    //Going to posts activity
                    startActivity(new Intent(MainActivity.this, PostListActivity.class));
                    finish();
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
                                        //Going to posts activity
                                        startActivity(new Intent(MainActivity.this, PostListActivity.class));
                                        finish();

                                    } else {

                                        //Log.d(TAG, userName + " failed to log in");
                                        Toast.makeText(MainActivity.this,  userName + " is failed to log in", Toast.LENGTH_LONG).show();
                                    }


                                }
                            });


                } else {
                    Toast.makeText(MainActivity.this, "Enter credentials first", Toast.LENGTH_LONG).show();
                }
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
/*                                        databaseReference = database.getReference("created");
                                        databaseReference.setValue(userName);*/
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

    public boolean onOptionsItemSelected(MenuItem item) {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        switch (item.getItemId()) {

            case R.id.action_signout:

                if (mUser != null && mAuth != null) {

                    mAuth.signOut();
                    finish();
                }
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //mFunctions = FirebaseFunctions.getInstance("europe-west1");

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //mFunctions = FirebaseFunctions.getInstance("europe-west1");

        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);

        }

    }

}