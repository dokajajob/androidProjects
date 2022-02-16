package com.dokajajob.get2sail_p1.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dokajajob.get2sail_p1.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;



import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener {

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
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    public LocationRequest mLocationRequest;
    protected Context context;
    protected boolean gps_enabled, network_enabled;
    public double Latitude;
    public double Longitude;
    public GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //Location service instantiate
    mGoogleApiClient = new GoogleApiClient.Builder(this)
            // The next two lines tell the new client that “this” current class will handle connection stuff
            .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
            .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
            //fourth line adds the LocationServices API endpoint from GooglePlayServices
            .addApi(LocationServices.API)
            .build();

    mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000)        // 10 seconds, in milliseconds
            .setFastestInterval(1 * 1000); // 1 second, in milliseconds



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
                startActivity(new Intent(MainActivity.this, Maps_Activity.class));
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

                                    //Set Location Service
                                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                                    //Request permission if none
                                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        requestPermission();
                                    }
                                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);

                                    //Going to posts activity
                                    startActivity(new Intent(MainActivity.this, Maps_Activity.class));
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


    //Get GPS Persmission
    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

    }

    //Location Management
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    //Menu Management
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

    //Start
    @Override
    protected void onStart() {
        super.onStart();

        //mFunctions = FirebaseFunctions.getInstance("europe-west1");

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    //Stop
    @Override
    protected void onStop() {
        super.onStop();

        //mFunctions = FirebaseFunctions.getInstance("europe-west1");

        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);

        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }


/*    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MainActivity.this);
            mGoogleApiClient.disconnect();
        }


    }*/
}