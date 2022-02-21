package com.dokajajob.weatherapi.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import Data.DataHandlerAdapter;
import Model.CityItems;
import com.dokajajob.weatherapi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import android.graphics.BitmapFactory;

public class MainActivity extends AppCompatActivity {

    private static String URL = "";
    private static String n_query = null;
    private static String mText = null;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private FloatingActionButton floatingActionButton;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private EditText item;
    private TextView title;
    private Button search;
    private static final String LINK_REGEX = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
    private Uri imageFilePath;
    private Bitmap imageToStore;
    private Bitmap image;
    private Boolean flag = true;

    //Toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "test", Toast.LENGTH_LONG).show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


/*        String ip = getLocalIpAddress();
        Log.d("IP: ", ip);
        Locale locale = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);
        String country = locale.getCountry();
        Log.d("Country :", country);*/

        //Floating Action Button
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();

            }
        });

    }


    private void createPopupDialog() {

        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        title = view.findViewById(R.id.title);
        item = view.findViewById(R.id.item);
        search = view.findViewById(R.id.search);

        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!item.getText().toString().isEmpty()){
                    search.setClickable(false);
                    Toast.makeText(MainActivity.this, item.getText().toString() + " Weather Clicked. Please Wait.", Toast.LENGTH_LONG).show();
                    CityItems cityItems = new CityItems();
                    loadWeather(view, cityItems);
                }
            }
        });


    }

    private void loadWeather(View view, CityItems cityItems) {

            //Strict mode for HTTPS
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);


            //Volley library for API extraction
            mRequestQueue = Volley.newRequestQueue(this);

            //City Name Set For API Search
            String cityName = item.getText().toString();
            //String rCityName=cityName.replace(' ','%20');
            String rCityName = cityName.trim().replaceAll(" +", "%20");
            Log.d("rCityName", rCityName);

            if (cityName.contains(" ")){
                      URL = "http://api.weatherstack.com/current?access_key=695ba834935173efb8526a20a5cccd85&query=" + rCityName;
                      Log.d("afterWhiteSpaces: ", URL);}
            else{
                      URL = "http://api.weatherstack.com/current?access_key=695ba834935173efb8526a20a5cccd85&query=" + cityName;
                      Log.d("withoutWiteSpaces", URL);}



            //Get initial Weather API
            mStringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(String response) {
                    Log.d("Response: ", response.toString());
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONObject request = obj.getJSONObject("request");
                        Log.d("Request:", String.valueOf(request));
                        //City Search in API
                        if (!(request.getString("query").contains("request_failed"))) {
                            n_query = request.getString("query");
                            Log.d("query:", n_query);
                            cityItems.setCityName(cityName);



                            //Temperature Search in API
                            JSONObject current = obj.getJSONObject("current");
                            String temperature = current.getString("temperature");
                            Log.d("temperature:", temperature);
                            cityItems.setCityTemperature(temperature);

                            //PNG Search in API
                            JSONObject currentWI = obj.getJSONObject("current");
                            Log.d("current", String.valueOf(current));
                            JSONArray weather_icons = currentWI.getJSONArray("weather_icons");
                            //Log.d("weather_icons", String.valueOf(weather_icons));
                            Log.d("weather_icons", URLDecoder.decode(String.valueOf(weather_icons)));
                            String pngLink = String.valueOf(weather_icons);
                            Log.d("pngLink", pngLink);
                            String png = pngLink.replaceAll("[\\\\<>\\[\\],-]", "");
                            png = png.replaceAll("\"", "");
                            Log.d("png", png);

                            //cityItems.setImage(getBitmapFromURL(png));

                            //String tst = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.pinterest.com%2Fpin%2F709246641298069234%2F&psig=AOvVaw1Re40t77_U3zUQsdWL-f3Q&ust=1643794602637000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCNiMnKqa3vUCFQAAAAAdAAAAABAJ";
                            //tst = tst.replaceAll("\"", "");

/*                            //URI to Bitmap
                            imageFilePath = Uri.parse(png);
                            Log.d("imageFilePath", String.valueOf(imageFilePath));
                            try {
                                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/

                            //Get PNG from Web
                            try {
                                java.net.URL url = new URL(png);
                                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                Log.d("image ", String.valueOf(image));
                            } catch(IOException e) {
                                System.out.println(e);
                            }
                            cityItems.setImage(image);


                            //Date
                            String mDate = LocalDate.now().toString();
                            Log.d("mDate", mDate);
                            cityItems.setDateItemAdded(mDate);

/*                            //Load Intent With (City Name & Temperature & PNG) And Go To RecyclerActivity
                            Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);
                            intent.putExtra("cityName", cityItems.getCityName());
                            intent.putExtra("temperature", cityItems.getCityTemperature());
                            intent.putExtra("png", cityItems.getImageName());
                            intent.putExtra("date", cityItems.getDateItemAdded());
                            //intent.putExtra("cityItemsList", String.valueOf(cityItemsList));
                            startActivity(intent);*/

                            //Save to DB
                            DataHandlerAdapter db = new DataHandlerAdapter(MainActivity.this);
                            db.addCity(cityItems);

                            //Go to RecyclerActivity
                            Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);
                            startActivity(intent);


                        } else {
                            Log.d("WRONG_CITY", cityName);
                            Toast.makeText(MainActivity.this, "Entered Wrong City Name", Toast.LENGTH_LONG).show();
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("Error = ", e.getMessage());
                        Toast.makeText(MainActivity.this, "Wrong URL For Weather Provided", Toast.LENGTH_LONG).show();
                    }

                }
            },new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error = ", error.getMessage());

                }
            });
            mRequestQueue.add(mStringRequest);


        }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}