package com.project.paulo.bpapp.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "pressureDataStorage";

    // Contacts table name
    private static final String TABLE_DATA = "data";
    private static final String TABLE_FEATURE = "feature";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TIME = "time";
    private static final String KEY_PRESSURE = "pressure";

    private static final String KEY_SYSTOLIC = "systolicPressure";
    private static final String KEY_DIASTOLIC = "diastolicPressure";
    private static final String KEY_MEAN = "meanPressure";
    private static final String KEY_MAXPRESSURE = "maxRateOfPressureChange";
    private static final String KEY_HEART = "heartRate";
    private static final String KEY_DICROTICNOTCH = "dicroticNotchPressure";
    private static final String KEY_DICROTICPEAK = "dicroticPeakPressure";
    private static final String KEY_ABNORMALITIES = "abnormalities";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_DATA + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TIME + " TEXT,"
                + KEY_PRESSURE + " TEXT" + ")";
        db.execSQL(CREATE_DATA_TABLE);

        String CREATE_FEATURE_TABLE = "CREATE TABLE " + TABLE_FEATURE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TIME + " TEXT,"
                + KEY_SYSTOLIC + " TEXT,"
                + KEY_DIASTOLIC + " TEXT,"
                + KEY_MEAN + " TEXT,"
                + KEY_MAXPRESSURE + " TEXT,"
                + KEY_HEART + " TEXT,"
                + KEY_DICROTICNOTCH + " TEXT,"
                + KEY_DICROTICPEAK + " TEXT,"
                + KEY_ABNORMALITIES + " TEXT" + ")";
        db.execSQL(CREATE_FEATURE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEATURE);

        // Create tables again
        onCreate(db);
    }

    // Adding new data
    public void addData(ChartValueDB chartValueDB) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TIME, chartValueDB.getTime());
        values.put(KEY_PRESSURE, chartValueDB.getPressure());

        // Inserting Row
        db.insert(TABLE_DATA, null, values);
        db.close(); // Closing database connection
    }

    // Adding new data
    public void addFeatureData(FeatureValueDB featureValueDB) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TIME, featureValueDB.get_time());
        values.put(KEY_SYSTOLIC, featureValueDB.get_systolicPressure());
        values.put(KEY_DIASTOLIC, featureValueDB.get_diastolicPressure());
        values.put(KEY_MEAN, featureValueDB.get_meanPressure());
        values.put(KEY_MAXPRESSURE, featureValueDB.get_maxRateOfPressureChange());
        values.put(KEY_HEART, featureValueDB.get_heartRate());
        values.put(KEY_DICROTICNOTCH, featureValueDB.get_dicroticNotchPressure());
        values.put(KEY_DICROTICPEAK, featureValueDB.get_dicroticPeakPressure());
        values.put(KEY_ABNORMALITIES, featureValueDB.get_abnormalities());

        // Inserting Row
        db.insert(TABLE_FEATURE, null, values);
        db.close(); // Closing database connection
    }

    // Getting single data
    public ChartValueDB getChartValueDB(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DATA, new String[] { KEY_ID,
                        KEY_TIME, KEY_PRESSURE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ChartValueDB chartValueDB = new ChartValueDB(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return data
        return chartValueDB;
    }

    // Getting All Data
    public List<ChartValueDB> getAllData() {
        List<ChartValueDB> dataList = new ArrayList<ChartValueDB>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChartValueDB chartValueDB = new ChartValueDB();
                chartValueDB.setID(Integer.parseInt(cursor.getString(0)));
                chartValueDB.setTime(cursor.getString(1));
                chartValueDB.setPressure(cursor.getString(2));
                // Adding data to list
                dataList.add(chartValueDB);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dataList;
    }

    // Getting data Count
    public int getDataCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_DATA;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();

        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_DATA);
        db.close();
        return (int) count;
    }

    public int getDataCount2() {
//        String countQuery = "SELECT  * FROM " + TABLE_DATA;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();

        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_FEATURE);
        db.close();
        return (int) count;
    }

    public Cursor raw() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_DATA , new String[]{});

        return res;
    }

    public Cursor raw2() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_FEATURE , new String[]{});

        return res;
    }
}
