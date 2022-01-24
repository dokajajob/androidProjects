package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;

import java.util.ArrayList;
import java.util.List;

import Model.Grocery;
import Util.Util;

public class DataHandler extends SQLiteOpenHelper {
    private Context ctx;
    public DataHandler(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase dbHandler) {

        //SQL
        String CREATE_GROCERY_TABLE = "CREATE TABLE "
                + Util.DATABASE_TABLE + "("
                + Util.KEY_Id + " INTEGER PRIMARY KEY,"
                + Util.KEY_NAME + " TEXT,"
                + Util.KEY_Qty + " TEXT" + ")";

        dbHandler.execSQL(CREATE_GROCERY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase dbHandler, int oldVerdsion, int newVersion) {

        //Drop Table
        dbHandler.execSQL("DROP TABLE IF EXISTS " + Util.DATABASE_TABLE);
        //Create Again
        onCreate(dbHandler);

    }

    public void addGrocery(Grocery grocery){

        //DB Write
        SQLiteDatabase dbHandler = this.getWritableDatabase();

        //Values
        ContentValues value = new ContentValues();
        value.put(Util.KEY_NAME, grocery.getName());
        value.put(Util.KEY_Qty, grocery.getQty());

        //Insert Row
        dbHandler.insert(Util.DATABASE_TABLE, null, value);

        //DB Close
        //dbHandler.close();

    }

    public List<Grocery> getGrocery(int id){

        //DB Read
        SQLiteDatabase dbHandler = this.getReadableDatabase();

        //SQL
        Cursor cursor = dbHandler.query(Util.DATABASE_TABLE, new String[]
                {Util.KEY_Id, Util.KEY_NAME, Util.KEY_Qty}, Util.KEY_Id + "=?",
                new String[] {String.valueOf(id)}, null, null, null);

        if (cursor != null)

            cursor.moveToFirst();

            //Save to constructor
            //Grocery grocery = new Grocery(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));

            Grocery grocery = new Grocery();
            grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_Id))));
            grocery.setName(cursor.getString(cursor.getColumnIndex(Util.KEY_NAME)));
            grocery.setQty(cursor.getString(cursor.getColumnIndex(Util.KEY_Qty)));

            List<Grocery> groceryList= new ArrayList<>();
            groceryList.add(grocery);


        return groceryList;

    }

    public List<Grocery> getAllGroceries(){

        //DB Read
        SQLiteDatabase dbHandler = this.getReadableDatabase();
        List<Grocery> ListOfAllGroceries = new ArrayList<>();

        //SQL All Groceries
        String SelectAll = "SELECT * FROM " + Util.DATABASE_TABLE;
        Cursor cursor = dbHandler.rawQuery(SelectAll, null);

        //Loop through all groceries
        if (cursor.moveToFirst()){
            do {
                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_Id))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Util.KEY_NAME)));
                grocery.setQty(cursor.getString(cursor.getColumnIndex(Util.KEY_Qty)));

                //Save to list
                ListOfAllGroceries.add(grocery);

            }while (cursor.moveToNext());

        }

        return ListOfAllGroceries;

    }

    public int updateGrocery(Grocery grocery){
        SQLiteDatabase dbHandler = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(Util.KEY_NAME, grocery.getName());
        value.put(Util.KEY_Qty, grocery.getQty());

        //Update row
        return dbHandler.update(Util.DATABASE_TABLE, value, Util.KEY_Id + "=?", new String[] {String.valueOf(grocery.getId())});
    }

    public void deleteGrocery(int id){
        SQLiteDatabase dbHandler = this.getWritableDatabase();
        dbHandler.delete(Util.DATABASE_TABLE, Util.KEY_Id + "=?", new String[] {String.valueOf(id)});
        dbHandler.close();
    }

    public int countGroceries(){
        SQLiteDatabase dbHandler = this.getReadableDatabase();
        String getAll = "SELECT * FROM " + Util.DATABASE_TABLE;
        Cursor cursor = dbHandler.rawQuery(getAll, null);

        return cursor.getCount();
    }


}

