package com.dokajajob.recycler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mNames = new ArrayList<>();
    //private ArrayList<Integer> mImagesURLS = new ArrayList<>();
    ArrayList<Bitmap> mImagesURLS = new ArrayList<Bitmap>();
    private final int REQUEST_CODE_1 = 1;
    private TextView skipp1_response;
    private ConstraintLayout LayoutMain;
    SQLiteDatabase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  initImageBitmaps();

    //SQL DB
    //database created
    DB = this.openOrCreateDatabase("myDB", Context.MODE_PRIVATE,null);
    //table created
    DB.execSQL("create table if not exists imageTb ( a blob )");

    //Call save image
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

/*    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("/home/dokaja/Downloads/edd1.jpg");
        System.out.println(file.exists());
        Scanner scan = new Scanner(file);
    }*/

    //Save Image to DB
    public void save() throws IOException{
        //Uri path = Uri.parse("android.resource://com.dokajajob.recycler/" + R.drawable.edd1);
        File file1 = new File("/sdcard/DCIM/My/edd1.jpg");
        System.out.println(new File(String.valueOf(file1)).getAbsolutePath());
        String file = "/sdcard/edd/edd1.jpg";
        System.out.println("Check me! '" + file + "'");
        FileInputStream fis = new FileInputStream(file.trim());
        byte[] image = new byte[fis.available()];
        fis.read(image);
        ContentValues values = new ContentValues();
        values.put("a", image);
        DB.insert("imageTB", null, values);
        fis.close();
        Toast.makeText(this, "file saved to DB", Toast.LENGTH_SHORT).show();
        get();
        
    }

    //Get Image from DB
    public void get() {
        Cursor c = DB.rawQuery("SELECT * FROM imageTB", null);
        if (c.moveToNext()){
            byte[] image = c.getBlob(0);
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0 , image.length);
            mImagesURLS.add(bmp);
            initRecyclerView();
            Toast.makeText(this, "Image passed to Recycler", Toast.LENGTH_SHORT).show();

        }

    }


/*    private void initImageBitmaps(){
            mImagesURLS.add(R.drawable.e1);
            mNames.add("Endurance near Arran");

            mImagesURLS.add(R.drawable.ic_launcher_foreground);
            mNames.add("Endurance near Largs");

            initRecyclerView();
    }*/

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.RecyclerId);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImagesURLS);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_1){
            if (resultCode == RESULT_OK){
                skipp1_response = findViewById(R.id.skipp1_response);
                Log.d("DEBUG","InMain");
                skipp1_response.setText(data.getStringExtra("skipp1Response"));
                Log.d("DEBUG", skipp1_response.getText().toString());
            }
        }
        if (skipp1_response.getText().toString() != null){
            Toast toast = Toast.makeText(this, "Got Response", Toast.LENGTH_LONG);
        }
        else {
            //Toast toast = Toast.makeText(this, "FAILED To Get Response", Toast.LENGTH_LONG);
            LayoutMain = findViewById(R.id.LayoutMain);
            LayoutMain.setVisibility(View.INVISIBLE);
        }

    }


}