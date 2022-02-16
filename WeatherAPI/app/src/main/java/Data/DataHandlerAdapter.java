package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.CityItems;
import Util.Constants;

public class DataHandlerAdapter extends SQLiteOpenHelper {

    private Context context;

    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;
    private Bitmap imagetToStoreBitmap;

    public DataHandlerAdapter(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CITY_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_CITY_ITEM + " TEXT,"
                + Constants.KEY_TEMP + " TEXT,"
                + Constants.KEY_DATE + " LONG,"
                //+ Constants.KEY_IMAGE_NAME + " TEXT,"
                + Constants.KEY_IMAGE + " BLOB);";

        db.execSQL(CREATE_CITY_TABLE);
        Log.d("SQL ", CREATE_CITY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);

    }

    public void addCity(CityItems cityItems) {

        SQLiteDatabase db = this.getWritableDatabase();

        //Image bitmap to byte
        imagetToStoreBitmap = cityItems.getImage();
        objectByteArrayOutputStream = new ByteArrayOutputStream();
        imagetToStoreBitmap.compress(Bitmap.CompressFormat.PNG, 100, objectByteArrayOutputStream);
        imageInBytes = objectByteArrayOutputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_CITY_ITEM, cityItems.getCityName());
        values.put(Constants.KEY_TEMP, cityItems.getCityTemperature());
        values.put(Constants.KEY_DATE, System.currentTimeMillis());
        //values.put(Constants.KEY_IMAGE_NAME, cityItems.getImageName());
        values.put(Constants.KEY_IMAGE, imageInBytes);

        //Insert the row
        long checkIfDataRuns = db.insert(Constants.TABLE_NAME, null, values);
        if (checkIfDataRuns != -1) {
            //Toast.makeText(context, "Saving to DB", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(context, "Failed Save To DB ", Toast.LENGTH_LONG).show();
        }

    }

    public List<CityItems> getAllCities() {

        SQLiteDatabase db = this.getReadableDatabase();

        List<CityItems> citiesReturned = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID,
        Constants.KEY_CITY_ITEM, Constants.KEY_TEMP, Constants.KEY_DATE, Constants.KEY_IMAGE}, null,
                null, null, null, Constants.KEY_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                CityItems cityItems = new CityItems();
                cityItems.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                cityItems.setCityName(cursor.getString(cursor.getColumnIndex(Constants.KEY_CITY_ITEM)));
                cityItems.setCityTemperature(cursor.getString(cursor.getColumnIndex(Constants.KEY_TEMP)));
                imageInBytes = cursor.getBlob(cursor.getColumnIndex(Constants.KEY_IMAGE));

                //Convert byte array to bitmap
                imagetToStoreBitmap = BitmapFactory.decodeByteArray(imageInBytes, 0 , imageInBytes.length);
                cityItems.setImage(imagetToStoreBitmap);

                //Convert time stamp
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE))));
                cityItems.setDateItemAdded(formatedDate);

                //Add to list
                citiesReturned.add(cityItems);

            } while (cursor.moveToNext());

        }

        return citiesReturned;

    }
}
