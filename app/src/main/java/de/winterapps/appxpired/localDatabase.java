package de.winterapps.appxpired;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by D062332 on 16.11.2015.
 */
public class localDatabase extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "localDB.db";
    public static final String CREATE_GROCERIES_TABLE = "create table groceries"+
            "(id integer primary key autoincrement, name text, entryDate integer, expireDate integer, position_id integer, amount integer, "+
            "additionalInformation text, template_id integer, category_id integer, deleted integer, household_id integer, createUser_id integer)";
    public static final String CREATE_TEMPLATE_TABLE = "";
    public static final String CREATE_POSITION_TABLE = "";
    public static final String CREATE_CATEGORY_TABLE = "";
    public static final String CREATE_MEASURING_TABLE = "";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GROCERIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS groceries");
        onCreate(db);
    }

    public localDatabase(Context context){
       super(context, DATABASE_NAME, null, 1);
    }

    public boolean addFood(JSONObject foodEntry){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values = cleanseValuesAddFood(foodEntry);
        } catch (JSONException e) {
            e.printStackTrace();
            return  false;
        }
        db.insert("groceries", null, values);
        db.close();
        return true;
    }

    public ContentValues cleanseValuesAddFood(JSONObject foodEntry) throws JSONException {
        ContentValues values = new ContentValues();
        long entryDate = System.currentTimeMillis();
            if(foodEntry.getString("name") != null){
                values.put("name", foodEntry.getString("name"));
            }else {
                values.put("name", "");
            }
            values.put("entryDate", entryDate);
            if (foodEntry.getString("expireDate") != null){
                values.put("expireDate", foodEntry.getString("expireDate"));
            }else{
                values.put("expireDate", 0);
            }
            if (foodEntry.getString("position_id") != null){
                values.put("position_id", foodEntry.getString("position_id"));
            }else{
                values.put("position_id", 0);
            }
            if (foodEntry.getString("amount") != null){
                values.put("amount", foodEntry.getString("amount"));
            }else{
                values.put("amount", 0);
            }
            if (foodEntry.getString("additionalInformation") != null){
                values.put("additionalInformation", foodEntry.getString("additionalInformation"));
            }else{
                values.put("additionalInformation", 0);
            }
            if (foodEntry.getString("template_id") != null){
                values.put("template_id", foodEntry.getString("template_id"));
            }else{
                values.put("template_id", 0);
            }
            values.put("category_id", 0);//dummy entry
            values.put("deleted", 0);
            values.put("household_id", 0);//dummy entry
            values.put("createUser_id", 0);//dummy entry

        return values;
    }

    public JSONArray getFood(){
        JSONArray foodArray = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from groceries", null );
        res.moveToFirst();
        while (res.isAfterLast() == false){
            JSONObject foodEntry = new JSONObject();
            try {
                foodEntry.put("id",res.getInt(res.getColumnIndex("id")));
                foodEntry.put("name",res.getString(res.getColumnIndex("name")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            foodArray.put(foodEntry);
        }
        return foodArray;
    }

    public JSONObject getFood(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from groceries where id="+id, null );
        JSONObject foodEntry = new JSONObject();
        try {
            foodEntry.put("id",res.getInt(res.getColumnIndex("id")));
            foodEntry.put("name",res.getString(res.getColumnIndex("name")));
            foodEntry.put("expireDate",res.getInt(res.getColumnIndex("expireDate")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return foodEntry;
    }
}
