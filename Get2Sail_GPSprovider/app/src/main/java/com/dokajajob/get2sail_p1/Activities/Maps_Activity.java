package com.dokajajob.get2sail_p1.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dokajajob.get2sail_p1.databinding.ActivityMapsBinding;
import com.dokajajob.get2sail_p1.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.GPS;

public class Maps_Activity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    public GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final String TAG = "MAPS";

    //GPS Location variables
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected boolean gps_enabled,network_enabled;
    public String lat;
    public String lng;
    private DatabaseReference mPostDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog mProgress;
    public String mUID;
    private Boolean userType;
    private Boolean mapReady;
    private String UserIs;

    private FirebaseDatabase mDatabase;
    private List<GPS> gpsList;
    private List<GPS> listItems;
    private DatabaseReference mDatabaseReference;
    Location mLastLocation;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;




    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //GPS Location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Log.d("locationManager", String.valueOf(locationManager));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //Get user type
        UserIs = getUserType();
        Log.d("UserIs : ", UserIs);


        //Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUID = mUser.getUid();


        //Realtime database
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child(UserIs);
        Log.d("mDatabaseReference: ", mDatabaseReference.toString());
        mDatabaseReference.keepSynced(true);



    }


    /** get user location **/
    @SuppressLint("MissingPermission")
    public void getLocation() {

        try {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            GPS gps = new GPS();

                            MarkerOptions mp = new MarkerOptions();
                            mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

                            gps.setLat(Double.toString(location.getLatitude()));
                            gps.setLng(Double.toString(location.getLongitude()));

                            lat = gps.getLat();
                            lng = gps.getLng();

                            Log.d("DeviceLat : ", String.valueOf(lat));
                            Log.d("DeviceLat : ", String.valueOf(lng));

                            mp.title("My Position");

                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                mMap.addMarker(mp);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 16));
                            }, 1000);

                            //uploadToBackend(gps);

                            if (location != null) {
                                Log.d("noDeviceLocation", "noDeviceLocation");
                            }


        }
    });

        } catch (Exception e) {
            Log.d("Exception : ", e.toString());
        }
    }




    /** get user type **/
    private String getUserType() {

        GPS gps = new GPS();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            userType = bundle.getBoolean("userType");
            Log.d("userType", userType.toString());

            if (userType) {
                gps.setUser_is("skipper");
                return UserIs = "skipper";

            } else {
                gps.setUser_is("guest");
                return UserIs = "guest";

            }


        }
        return null;
    }

    /** Upload to backend **/
    private void uploadToBackend(GPS gps) {

/*        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Posting ... ");
        mProgress.show();*/


        //mAuth = FirebaseAuth.getInstance();
        //mUser = mAuth.getCurrentUser();
        //mUID = mUser.getUid();

        UserIs = getUserType();
        mPostDatabase = FirebaseDatabase.getInstance().getReference().child(UserIs);
        DatabaseReference newPost = mPostDatabase.push();

        //uid
        gps.setUid(mUID);

        Map<String, String> dataToSave = new HashMap<>();
        dataToSave.put("lat", gps.getLat());
        dataToSave.put("lng", gps.getLng());
        dataToSave.put("uid", gps.getUid());
        //dataToSave.put("user_is", gps.getuser_is());

        Log.d("dataToSave", dataToSave.toString());
        newPost.setValue(dataToSave);
       // mProgress.dismiss();


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapReady = true;
        clearMap();


        //Display my position
        getLocation();


       //Display other positions of same  user type
       addMarkers();


    }



    /** Add Markers according to user type **/
    private void addMarkers() {

/*        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUID = mUser.getUid();*/

        MarkerOptions mp = new MarkerOptions();

        UserIs = getUserType();
        if (UserIs == "guest") {
            mp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else {
            mp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }

        //mDatabase = FirebaseDatabase.getInstance();
        //mPostDatabase = FirebaseDatabase.getInstance().getReference().child(UserIs).child(mUID);
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                gpsList = new ArrayList<>();
                //listItems = new ArrayList<>();

                GPS gps = snapshot.getValue(GPS.class);
                //Log.d("post", gps.toString());
                gpsList.add(gps);
                //Collections.reverse(gpsList);
                //Log.d("postList ", gpsList.toString());


/*                ListIterator listItr = gpsList.listIterator();
                while(listItr.hasNext())
                {
                    System.out.println(listItr.next());
                }*/

                for (GPS c : gpsList) {

                    gps.setLat(c.getLat());
                    //System.out.println(lat);
                    gps.setLng(c.getLng());
                    //System.out.println(lng);
                    gps.setUid(c.getUid());
                    System.out.println("uidFromDb " + gps.getUid());
                    System.out.println("uidUser " + mUID);

                    if (!gps.getUid().equals(mUID)) {

                        lat = gps.getLat();
                        lng = gps.getLng();

                        Log.d("latFromFirebase : ", String.valueOf(lat));
                        Log.d("latFromFirebase : ", String.valueOf(lng));


                        mp.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

                        mp.title(lat + " : " + lng + "\n" + gps.getUid());

                        mMap.addMarker(mp);
/*                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 16));*/

                    } else {
                        Log.d("uidCheck ", "same uid " + mUID);
                    }


                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    /** clear Map **/
    private void clearMap() {
        mMap.clear();


    }

    /** Menu **/
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    public boolean onOptionsItemSelected(MenuItem item) {


            switch (item.getItemId()) {

                case R.id.action_add:
                    startProfile();
                    break;

                case  R.id.action_signout:
                    mAuth.signOut();
                    startActivity(new Intent(Maps_Activity.this, MainActivity.class));
                    finish();
                    break;

                case  R.id.action_show_only_skippers:
                    UserIs = "skipper";
                    showOnly(UserIs);
                    break;

                case  R.id.action_show_only_guests:
                    UserIs = "guest";
                    showOnly(UserIs);
                    break;

            }

        return super.onOptionsItemSelected(item);


    }




    /** Show Only View **/
    private void showOnly(String UserTypeToDisplay) {
        clearMap();

        MarkerOptions mp = new MarkerOptions();

/*        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUID = mUser.getUid();*/

        mDatabaseReference = mDatabase.getReference().child(UserTypeToDisplay);

        if (UserTypeToDisplay == "skipper") {
            mp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        } else {
            mp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }


        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                gpsList = new ArrayList<>();
                //listItems = new ArrayList<>();

                GPS gps = snapshot.getValue(GPS.class);
                //Log.d("post", gps.toString());
                gpsList.add(gps);
                Collections.reverse(gpsList);
                //Log.d("postList ", gpsList.toString());

/*                ListIterator listItr = gpsList.listIterator();
                while(listItr.hasNext())
                {
                    System.out.println(listItr.next());
                }*/

                for (GPS c : gpsList) {

                    gps.setLat(c.getLat());
                    //System.out.println(lat);
                    gps.setLng(c.getLng());
                    //System.out.println(lng);

                    lat = gps.getLat();
                    lng = gps.getLng();

                    Log.d("latFromFirebase : ", String.valueOf(lat));
                    Log.d("latFromFirebase : ", String.valueOf(lng));


                    mp.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

                    mp.title(lat + " : " + lng);

                    mMap.addMarker(mp);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 16));



                }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    /** To Do **/
    private void startProfile() {
    }


    /** onLocationChanged **/
    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(@NonNull Location location) {

        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, this);

        GPS gps = new GPS();


        //mMap.clear();


        MarkerOptions mp = new MarkerOptions();
        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

        gps.setLat(Double.toString(location.getLatitude()));
        gps.setLng(Double.toString(location.getLongitude()));

        lat = gps.getLat();
        lng = gps.getLng();

        Log.d("DeviceUpdateLat : ", String.valueOf(lat));
        Log.d("DeviceUpdateLat : ", String.valueOf(lng));

        mp.title(lat + " : " + lng);

       Handler handler = new Handler();
        handler.postDelayed(() -> {
            mMap.addMarker(mp);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 16));
        }, 1000);



       uploadToBackend(gps);


    }



    /**
     * onStatusChanged
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");

    }

    /**
     * onProviderEnabled
     */
    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.d("Latitude","enable");

    }

    /**
     * onProviderDisabled
     */
    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.d("Latitude","disable");

    }


}