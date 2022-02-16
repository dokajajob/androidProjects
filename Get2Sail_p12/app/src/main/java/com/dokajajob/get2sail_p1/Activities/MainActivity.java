package com.dokajajob.get2sail_p1.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
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
import androidx.core.app.ActivityCompat;

import com.dokajajob.get2sail_p1.BuildConfig;
import com.dokajajob.get2sail_p1.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.util.Date;

import Utils.GPS;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity  {

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
    protected Context context;
    protected boolean gps_enabled, network_enabled;
    public double Latitude;
    public double Longitude;
    public GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    //--- Location Related ---//

    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 600000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 300000;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    private Boolean startedLocationUpdates;
    public Boolean permissionsGranted;

    public String coordinates;
    public String updatedOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check permissions
        if (checkPermissions()) {
            //permissionsGranted = true;
            Toast.makeText(MainActivity.this, "Location permissions granted", Toast.LENGTH_LONG).show();

        } else {
            requestPermission();
            Log.d(TAG, "Requesting location permissions");

        }

        ButterKnife.bind(this);

        // initialize the necessary libraries
        init();

        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        //database = FirebaseDatabase.getInstance();


        /**
         * Auth State
         */
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

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        finish();
                    }, 1000);

                    //init from signed in user
                    //init();

                    //Going to posts activity
                    if (permissionsGranted) {

                        //Going to maps activity
                        GPS gps = new GPS();
                        toMaps(gps);

                    }

                } else {
                    Log.d(TAG , " signed out");
                    Toast.makeText(MainActivity.this, "User signed out", Toast.LENGTH_LONG).show();
                }

            }
        };

        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        loginImage = findViewById(R.id.loginImage);


        /**
         * Login
         */
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

                                    //request permissions for GPS
                                    //requestPermission();
                                    //onResume();

                                    //Going to maps activity
    /*                                if (mRequestingLocationUpdates) {

                                        GPS gps = new GPS();

                                        Intent intent = new Intent(MainActivity.this, Maps_Activity.class);

                                        Log.d("latMain : ", String.valueOf(gps.getLat()));
                                        Log.d("latMain : ", String.valueOf(gps.getLng()));

                                        intent.putExtra("lat", gps.getLat());
                                        intent.putExtra("lng", gps.getLng());

                                        startActivity(intent);
                                        finish();

                                    }*/


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



        /**
         * Create User
         */
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

                                        //request permissions for GPS
                                        //requestPermission();

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

    private void toMaps(GPS gps) {

        //GPS gps = new GPS();

        Intent intent = new Intent(MainActivity.this, Maps_Activity.class);

        Log.d("latMainAuth : ", String.valueOf(gps.getLat()));
        Log.d("lngMainAuth : ", String.valueOf(gps.getLng()));

        intent.putExtra("lat", gps.getLat());
        intent.putExtra("lng", gps.getLng());

        startActivity(intent);
        finish();
    }

    /**
     * GPS Permissions Request
     */
    public void requestPermission() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        permissionsGranted = true;
                        //init();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    /**
     * Init Location
     */
    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                Log.d("mCurrentLocation : ", String.valueOf(mCurrentLocation));
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();

            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }

    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            coordinates = ("Lat: " + mCurrentLocation.getLatitude() + ", " + "Lng: " + mCurrentLocation.getLongitude());
            Log.d("coordinates : ", coordinates);

            GPS gps = new GPS();
            gps.setLat(mCurrentLocation.getLatitude());
            gps.setLng(mCurrentLocation.getLongitude());

            Log.d("selLatMain : ", String.valueOf(gps.getLat()));
            Log.d("setLngMain : ", String.valueOf(gps.getLng()));


            // location last updated time
            updatedOn = ("Last updated on: " + mLastUpdateTime);
            Log.d("updatedOn : ", updatedOn);

            // start location updates
            if (!startedLocationUpdates) {
                startLocationUpdates();

            }

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.getMainLooper());

                        startedLocationUpdates = true;
                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "Pending Intent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    /**
     * Resume location updates
     */
    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
            Toast.makeText(MainActivity.this, "Updates resumed", Toast.LENGTH_LONG).show();
        }

/*        Handler handler = new Handler();
        handler.postDelayed(() -> {
            finish();
        }, 1000);*/

        updateLocationUI();
    }

    /**
     * Check permissions. returns only if granted
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsGranted = true;
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Pause location updates
     */
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    /**
     * Stop location updates
     */
    public void stopLocationUpdates() {
        // Removing location updates
        mRequestingLocationUpdates = false;
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    /**
     * Show in menu
     */
    public void showLastKnownLocation() {
        if (mCurrentLocation != null) {
            Toast.makeText(getApplicationContext(), "Lat: " + mCurrentLocation.getLatitude()
                    + ", Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Last known location is not available!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    /**
     * Menu builder
     */
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

            case R.id.action_show_location:

                if (mCurrentLocation != null) {

                    showLastKnownLocation();
                }
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Start
     */
    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    /**
     * Stop
     */
    @Override
    protected void onStop() {
        super.onStop();

        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);

        }

    }


}