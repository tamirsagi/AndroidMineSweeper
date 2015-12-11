package com.minesweeper.BL.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbManager extends SQLiteOpenHelper {

    public static final String TAG = "DB MANAGER";


    // Database Name
    public static final String DB_Name = "MineSweeperDb";

    public static final int MAX_PLAYER_NAME_LEENGTH = 10;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase dataBase;
    private final int recordsToSave = 10;

    private static final DateFormat dbDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static DbManager dbManager;

    public enum Tables {
        PLAYERS_RECORDS_BEGINNERS, PLAYERS_RECORDS_INTERMEDIATE, PLAYERS_RECORDS_EXPERT
    }

    //Columns names
    private static final String COL_NAME = "Full_Name";
    private static final String COL_GAME_ROUND_TIME = "Round_Time";
    private static final String COL_LOCATION_Latitude = "Latitude";
    private static final String COL_LOCATION_Longitude = "Longitude";
    private static final String COL_LOCATION_City = "City";
    private static final String COL_LOCATION_Country = "City";
    private static final String COL_DATE = "Date";


    private DbManager(Context context) {
        super(context, DB_Name, null, DATABASE_VERSION);
        this.dataBase = getWritableDatabase();
    }

    public static DbManager getInstance(Context context) {
        if (dbManager == null)
            dbManager = new DbManager(context);

        return dbManager;
    }

    public static String getDate() {
        Date date = new Date();
        return dbDateFormat.format(date);
    }

    public int getRecordsToSave() {
        return recordsToSave;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Tables tableName : Tables.values()) {
            String createTableCommand = "CREATE TABLE "
                    + tableName.toString() + "('_id' INTEGER PRIMARY KEY AUTOINCREMENT," + COL_NAME + " TEXT ,"
                    + COL_GAME_ROUND_TIME + " TEXT,"  + COL_LOCATION_Country + " TEXT," + COL_LOCATION_City + " TEXT,"
                    + COL_LOCATION_Latitude + " TEXT," + COL_LOCATION_Longitude + " TEXT,"
                    + COL_DATE + " TEXT" + ")";
            getDataBase().execSQL(createTableCommand);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Tables tableName : Tables.values()) {
            db.execSQL("DROP TABLE IF EXISTS " + tableName.toString());
        }
        onCreate(db);
    }

    /**
     * add record to table
     *
     * @param table
     * @param playerRecord
     */
    public boolean addPlayerRecord(String table, PlayerRecord playerRecord) {

        try {
            if (count(table) == recordsToSave)
                deleteLastRecord(table);

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_NAME, playerRecord.getFullName());
            values.put(COL_GAME_ROUND_TIME, playerRecord.getRoundTime());
            values.put(COL_LOCATION_City, playerRecord.getCity());
            values.put(COL_LOCATION_Country, playerRecord.getCountry());
            values.put(COL_LOCATION_Latitude, "" + playerRecord.getLatitude());
            values.put(COL_LOCATION_Longitude, "" + playerRecord.getLongitude());
            values.put(COL_DATE, playerRecord.getDate());
            db.insert(table, null, values);
            //sort the table after each insertion

            db.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }

    }

    /**
     * compare the player with the last player
     *
     * @param table
     * @param playerTime
     * @return true if current player's game time is better than the last
     */
    public boolean shouldBeInserted(String table, String playerTime) {
        int rowsCount = count(table);
        //if there are less records than the max records
        if (rowsCount < recordsToSave)
            return true;

        rowsCount = recordsToSave;
        SQLiteDatabase readableDB = this.getReadableDatabase();
        Cursor cursor = gerAllTableByRoundTime(readableDB,table);
        cursor.moveToLast();
        String lastPlayerTime = cursor.getString(cursor.getColumnIndex(COL_GAME_ROUND_TIME));
        //check if the new time is better than the last
        // if it is so the player's record should be updated within the table
        return playerTime.compareTo(lastPlayerTime) < 0;
    }


    /**
     * get top records(Max 10)
     *
     * @param table
     * @return
     */
    public List<PlayerRecord> getRecords(String table) {
        SQLiteDatabase readableDB = this.getReadableDatabase();
        int counter = 1;
        List<PlayerRecord> records = new ArrayList<PlayerRecord>();
        Cursor recordsCursor = gerAllTableByRoundTime(readableDB,table);
        while (recordsCursor.moveToNext()) {
            PlayerRecord playerRecord = new PlayerRecord();
            playerRecord.setId(counter++);
            playerRecord.setFullName(recordsCursor.getString(recordsCursor.getColumnIndex(COL_NAME)));
            playerRecord.setRoundTime(recordsCursor.getString(recordsCursor.getColumnIndex(COL_GAME_ROUND_TIME)));
            playerRecord.setCity(recordsCursor.getString(recordsCursor.getColumnIndex(COL_LOCATION_City)));
            playerRecord.setCountry(recordsCursor.getString(recordsCursor.getColumnIndex(COL_LOCATION_Country)));
            String latitude = recordsCursor.getString(recordsCursor.getColumnIndex(COL_LOCATION_Latitude));
            playerRecord.setLatitude(Double.parseDouble(latitude));
            String longitude = recordsCursor.getString(recordsCursor.getColumnIndex(COL_LOCATION_Longitude));
            playerRecord.setLongitude(Double.parseDouble(latitude));
            playerRecord.setDate(recordsCursor.getString(recordsCursor.getColumnIndex(COL_DATE)));
            //adding to list
            records.add(playerRecord);
        }
        readableDB.close();
        return records;
    }

    private Cursor gerAllTableByRoundTime(SQLiteDatabase db,String table){
        String sqlCommand = "SELECT * FROM " + table + " ORDER BY " + COL_GAME_ROUND_TIME + " ASC";
        return db.rawQuery(sqlCommand, null);
    }

    /**
     * counts number of rows within a table
     *
     * @param table
     * @return
     */
    private int count(String table) {
        return (int) DatabaseUtils.queryNumEntries(getDataBase(), table);
    }

    private SQLiteDatabase getDataBase(){
        if(dataBase == null || !dataBase.isOpen())
            dataBase = getWritableDatabase();
        return dataBase;
    }

    /**
     * Delete last Record within table
     *
     * @param table
     */
    private void deleteLastRecord(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        String command = "DELETE FROM " + table +
                " WHERE " + COL_GAME_ROUND_TIME + " = (SELECT MAX(" + COL_GAME_ROUND_TIME + ") FROM " + table + ")";
        db.execSQL(command);
        db.close();
    }

    public int deleteAll(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table, null, null);
    }

    public void close() {
        getDataBase().close();
    }


}
