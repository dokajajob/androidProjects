package com.dokajajob.get2sail_p1.Activities;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dokajajob.get2sail_p1.databinding.ActivityMapsBinding;
import com.dokajajob.get2sail_p1.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private Boolean showOnlyFlag = false;

    public LocationRequest mLocationRequest;
    private Date dateToConvert;
    private Date dateFromDB;
    public long resDate;
    private ZonedDateTime dateIs = null;
    private ZonedDateTime dateIsForMarker = null;
    private String receivedDate = null;
    private String uID_previous = "";
    private int count = 0;

    private Handler handlerMain = new Handler(Looper.getMainLooper());
    //private Handler handlerNew = new Handler(Looper.myLooper());

    private Handler handlerNew;
    private HandlerThread mHandlerThread;

    private long UPDATE_INTERVAL = 1800000;  /* 30 min */
    private long FASTEST_INTERVAL = 900000; /* 15 min */
    private long UPDATE_MARKERS_INTERVAL = 300000; /* 5 min */



    /**
     * updates each 15 min
     * update < 5 min taken and added to map each 1 minute
     */
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Main thread
        Log.d("Threading", "Test class thread is         .     .     >"+Thread.currentThread().getName());

        //GPS Location Google API
        startLocationUpdates();


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



    /**
     * CurrentDateTime Class
     * @return
     */
    public ZonedDateTime getCurrentDate() {
        ZonedDateTime date = ZonedDateTime.now();
        System.out.println("date : " + date);

        return date;

    }



    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }


    /**
     * localDate to date
     */
    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(String.valueOf(dateToConvert));
    }


    /**
     * startLocationUpdates Google API
     */
    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // Getting location
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }


    /**
     * onLocationChanged Google API. Start upload to backend
     */
    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        GPS gps = new GPS();


        MarkerOptions mp = new MarkerOptions();
        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

        gps.setLat(Double.toString(location.getLatitude()));
        gps.setLng(Double.toString(location.getLongitude()));

        lat = gps.getLat();
        lng = gps.getLng();

        Log.d("DeviceUpdateLat : ", String.valueOf(lat));
        Log.d("DeviceUpdateLat : ", String.valueOf(lng));

        UserIs = getUserType();
        Log.d("getLastLocation", UserIs);

        mp.title(lat + " : " + lng + " : " + UserIs);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            mMap.addMarker(mp);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 16));
        }, 1000);


        uploadToBackend(gps);

    }

    /**
     * getLastLocation Google API
     */
    @SuppressLint("MissingPermission")
    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);

                            //Clear map
                            clearMap();

                            //Add my position marker
                            GPS gps = new GPS();

                            MarkerOptions mp = new MarkerOptions();
                            mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

                            gps.setLat(Double.toString(location.getLatitude()));
                            gps.setLng(Double.toString(location.getLongitude()));

                            lat = gps.getLat();
                            lng = gps.getLng();

                            Log.d("DeviceLat : ", String.valueOf(lat));
                            Log.d("DeviceLat : ", String.valueOf(lng));


                            mp.title(lat + " : " + lng);

                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                mMap.addMarker(mp);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 16));
                            }, 1000);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
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


        UserIs = getUserType();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(UserIs);
        DatabaseReference newPost = mDatabaseReference.push();

        //uid
        gps.setUid(mUID);

        //Current Date
        dateIs = getCurrentDate();
        System.out.println("dateIs " + dateIs);
        gps.setDate(dateIs.toString());

        Map<String, String> dataToSave = new HashMap<>();
        dataToSave.put("lat", gps.getLat());
        dataToSave.put("lng", gps.getLng());
        dataToSave.put("uid", gps.getUid());
        dataToSave.put("date", gps.getDate());

        //Date comparison for Dups check//
        //Current Date
        dateIsForMarker = getCurrentDate();
        System.out.println("dateIsForMarker " + dateIsForMarker);

        //Get date from db
        receivedDate = gps.getDate();
        Log.d("receivedDateBeforeUpload : ", receivedDate);

        //String to ZoneDateTime
        ZonedDateTime receivedDateInDate = ZonedDateTime.parse(receivedDate);
        System.out.println("receivedDateInDate " + receivedDateInDate);

        //Get diff
        long timeDiff = ChronoUnit.MINUTES.between(receivedDateInDate, dateIsForMarker);
        System.out.println("timeDiffBeforeUpload " + timeDiff);


        if (timeDiff < 1) {
             count = count + 1;
            System.out.println("count : " + count);
        }

        //Upload if no dups
        if (count < 2) {
            Log.d("dataToSave", dataToSave.toString());
            newPost.setValue(dataToSave);

        }

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

        //Clear map and get time
        //clearMap();
        getCurrentDate();

        //Get device location on map
        //getLastLocation();

        //Get first markers
        addMarkers();

        //Display other positions of same  user type
        //start new loop thread
        Log.d("addMarkersLoop : ", "started addMarkersLoop : ");

        scheduledAddMarkersLoop();


    }


    /**
     * scheduledAddMarkersLoop thread
     */
    private void scheduledAddMarkersLoop() {

        mHandlerThread = new HandlerThread("addMarkersThread");
        mHandlerThread.start();
        handlerNew = new Handler(mHandlerThread.getLooper());
        handlerNew.post(markersRunnable);

    }


    /**
     * markersRunnable
     */
    private Runnable markersRunnable = new Runnable() {
        @Override
        public void run() {
            // addMarkers
            Log.d("Threading","Running markersRunnable in the Thread  -- > " + Thread.currentThread().getName());
            addMarkers();

            // Repeat every UPDATE_MARKERS_INTERVAL
            handlerNew.postDelayed(markersRunnable, UPDATE_MARKERS_INTERVAL);
        }
    };




    /** Add Markers according to user type **/
    private void addMarkers() {

        //clear map
        clearMapNotMain();
        //mMap.clear();
        System.out.println("map cleared");


        MarkerOptions mp = new MarkerOptions();

        UserIs = getUserType();
        System.out.println("UserIs addMarkers : " + UserIs);

        if (UserIs.equals("guest")) {
            mp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mDatabaseReference = mDatabase.getReference().child("skipper");
            System.out.println("guest loge_in");
        } else {
            mp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mDatabaseReference = mDatabase.getReference().child("guest");
            System.out.println("skipper loge_in");
        }

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                gpsList = new ArrayList<>();

                //listItems = new ArrayList<>();
                List<Marker> AllMarkers = new ArrayList<Marker>();

                GPS gps = snapshot.getValue(GPS.class);
                System.out.println("snapshotAddMarkers " + snapshot);
                //System.out.println("snapshot " + snapshot.getValue());
                gpsList.add(gps);

                //Current Date
                dateIsForMarker = getCurrentDate();
                System.out.println("dateIsForMarker " + dateIsForMarker);

                for (GPS c : gpsList) {

                    gps.setLat(c.getLat());
                    //System.out.println(lat);
                    gps.setLng(c.getLng());
                    //System.out.println(lng);
                    gps.setUid(c.getUid());


                    System.out.println("uidFromDb " + gps.getUid());
                    System.out.println("uidUser " + mUID);

                    //Date comparison//
                    //Get date from db
                    gps.setDate(c.getDate());
                    receivedDate = gps.getDate();
                    Log.d("receivedDate : ", receivedDate);

                    //String to ZoneDateTime
                    ZonedDateTime receivedDateInDate = ZonedDateTime.parse(receivedDate);
                    System.out.println("receivedDateInDate " + receivedDateInDate);

                    //Get diff
                    long timeDiff = ChronoUnit.MINUTES.between(receivedDateInDate, dateIsForMarker);
                    System.out.println("timeDiff " + timeDiff);


                    int sum = 0;
                    //Add marker if user is not local and updated last 5 minutes
                    if (!gps.getUid().equals(mUID) && timeDiff < 5) {

                        sum = sum + 1;

                        lat = gps.getLat();
                        lng = gps.getLng();

                        Log.d("latFromFirebase : ", String.valueOf(lat));
                        Log.d("latFromFirebase : ", String.valueOf(lng));


                        mp.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

                        mp.title(lat + " : " + lng + " : "  + gps.getUid());

                        //mMap.addMarker(mp);
                        Marker mLocationMarker = mMap.addMarker(mp);
                        AllMarkers.add(mLocationMarker);



                    } else {
                        Log.d("uidCheck ", "same uid " + mUID);
                    }
                    System.out.println("sum of added markers on addMarker on addMarkers " + sum);


                }
                gpsList.clear();

/*                //Move camera to last marker
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 16));*/


/*                //start new loop thread
                Log.d("addMarkersLoop : ", "started addMarkersLoop : ");
                Handler handler = new Handler();
                handler.postDelayed(() -> {

                   //removeAllMarkers(AllMarkers);

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                    Date date = new Date(System.currentTimeMillis());
                    Log.d("Threading", "Test class thread is         .     .     >" +Thread.currentThread().getName());
                    System.out.println("addMarkersON : " + formatter.format(date));
                    //Going back
                    addMarkers();

                }, UPDATE_MARKERS_INTERVAL); //every 5 minute*/




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

    /** clear Markers **/
    private void removeAllMarkers(List<Marker> allMarkers) {
        for (Marker mLocationMarker: allMarkers) {
            mLocationMarker.remove();
            Log.d("mLocationMarker ", "mLocationMarker");
        }
        allMarkers.clear();

        //Call add markers again if not called from showOnly
        addMarkers();

    }



    /** clear Map **/
    private void clearMap() {
        mMap.clear();


    }

    /** clear Map Not From Main Thread **/
    //@Override
    public void clearMapNotMain() {
        Maps_Activity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.clear();
            }
        });
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

                case R.id.action_show_location:
                    getLastLocation();
                    break;

            }

        return super.onOptionsItemSelected(item);


    }



    /** Show Only View **/
    private void showOnly(String UserTypeToDisplay) {
        clearMap();

        MarkerOptions mp = new MarkerOptions();

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
                List<Marker> AllMarkers = new ArrayList<Marker>();

                GPS gps = snapshot.getValue(GPS.class);

                System.out.println("snapshot " + snapshot);
                //Log.d("post", gps.toString());
                gpsList.add(gps);
                Collections.reverse(gpsList);

                //Current Date
                dateIsForMarker = getCurrentDate();
                System.out.println("dateIsForShowOnly " + dateIsForMarker);


                for (GPS c : gpsList) {

                    gps.setLat(c.getLat());
                    //System.out.println(lat);
                    gps.setLng(c.getLng());
                    //System.out.println(lng);
                    gps.setUid(c.getUid());
                    System.out.println("uidFromDb " + gps.getUid());
                    System.out.println("uidUser " + mUID);

                    //Date comparison//

                    //Get date from db
                    gps.setDate(c.getDate());
                    receivedDate = gps.getDate();
                    Log.d("receivedDate : ", receivedDate);

                    //String to ZoneDateTime
                    ZonedDateTime receivedDateInDate = ZonedDateTime.parse(receivedDate);
                    System.out.println("receivedDateInDate " + receivedDateInDate);

                    //Get diff
                    long timeDiff = ChronoUnit.MINUTES.between(receivedDateInDate, dateIsForMarker);
                    System.out.println("timeDiff " + timeDiff);


                    int sum = 0;

                    if (!gps.getUid().equals(mUID) && timeDiff < 5) {

                        sum = sum + 1;

                        lat = gps.getLat();
                        lng = gps.getLng();

                        Log.d("latFromFirebase : ", String.valueOf(lat));
                        Log.d("latFromFirebase : ", String.valueOf(lng));


                        mp.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

                        mp.title(lat + " : " + lng + " : " + gps.getUid());

                        //mMap.addMarker(mp);
                        Marker mLocationMarker = mMap.addMarker(mp); // add to map
                        AllMarkers.add(mLocationMarker); // add to list


                    } else {
                        Log.d("uidCheck ", "same uid " + mUID);
                    }
                    System.out.println("sum of added markers after showOnly " + sum);


                }
                gpsList.clear();

                //Move camera to last lat lng
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 16));



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



}