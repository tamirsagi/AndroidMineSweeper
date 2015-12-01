package com.minesweeper.BL.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbManager extends SQLiteOpenHelper {

    public static final String TAG = "DB MANAGER";

    // Database Name
    public static final String DB_Name = "MineSweeperDb";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase dataBase;
    private final int recordsToSave = 10;

    public static DbManager dbManager;

    public enum Tables {
        PLAYERS_RECORDS_BEGINNERS, PLAYERS_RECORDS_INTERMEDIATE, PLAYERS_RECORDS_EXPERT
    }

    //Columns names
    private static final String COL_NAME = "Full Name";
    private static final String COL_GAME_ROUND_TIME = "Round Time";
    private static final String COL_LOCATION = "Location";
    private static final String COL_DATE = "Date";
    private static final String COL_FULL_TIME = "Full Time";


    private DbManager(Context context) {
        super(context, DB_Name, null, DATABASE_VERSION);
    }

    public DbManager getInstance(Context context){
        if(dbManager == null)
            dbManager = new DbManager(context);

        return dbManager;
    }

    public DbManager getInstance(){
        return dbManager;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        this.dataBase = db;
        for (Tables tableName : Tables.values()) {
            String createTableCommand = "CREATE TABLE "
                    + tableName.toString() + "(" + COL_NAME + " TEXT PRIMARY KEY," + COL_GAME_ROUND_TIME
                    + " TEXT," + COL_LOCATION + " TEXT," + COL_DATE + " TEXT PRIMARY KEY," +
                    COL_FULL_TIME + "TEXT PRIMARY KEY" + ")";
            dataBase.execSQL(createTableCommand);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Tables tableName : Tables.values()) {
            db.execSQL("DROP TABLE IF EXISTS " + tableName.toString());
        }
        onCreate(db);
    }

    public void insertPlayerRecord(String table, PlayerRecord playerRecord) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, playerRecord.getFullName());
        values.put(COL_GAME_ROUND_TIME, playerRecord.getRoundTime());
        values.put(COL_LOCATION, playerRecord.getLocation());
        values.put(COL_DATE, playerRecord.getDate());
        values.put(COL_FULL_TIME, playerRecord.getFullTime());
        db.insert(table, null, values);

        //sort the table after each insertion
        dataBase.query(table, null, null, null, null, null, COL_GAME_ROUND_TIME + " DESC");
    }

    /**
     * compare the player with the last player
     *
     * @param table
     * @param playerTime
     * @return true if current player's game time is better than the last
     */
    public boolean shouldBeInserted(String table, String playerTime) {
        long rowsCount = count(table);
        if (rowsCount > recordsToSave)
            rowsCount = recordsToSave;

        Cursor cursor = dataBase.rawQuery("SELECT TOP " + rowsCount + " * FROM " + table, null);
        cursor.moveToPosition((int) rowsCount);
        String lastPlayerTime = cursor.getString(cursor.getColumnIndex(COL_GAME_ROUND_TIME));
        //check if the new time is better than the last
        // if it is so the player's record should be updated within the table
        return playerTime.compareTo(lastPlayerTime) > 0;
    }

    public List<PlayerRecord> getTopRecords(String table) {

        String sqlCommand = "SELECT * FROM " + table;
        List<PlayerRecord> records = new ArrayList<PlayerRecord>();
        SQLiteDatabase readableDB = this.getReadableDatabase();
        Cursor rowCursor = readableDB.rawQuery(sqlCommand, null);
        if (rowCursor.moveToFirst()) {
            while (rowCursor.moveToNext()) {
                PlayerRecord playerRecord = new PlayerRecord();
                playerRecord.setFullName(rowCursor.getString(rowCursor.getColumnIndex(COL_NAME)));
                playerRecord.setRoundTime(rowCursor.getString(rowCursor.getColumnIndex(COL_GAME_ROUND_TIME)));
                playerRecord.setLocation(rowCursor.getString(rowCursor.getColumnIndex(COL_LOCATION)));
                playerRecord.setDate(rowCursor.getString(rowCursor.getColumnIndex(COL_DATE)));
                playerRecord.setFullTime(rowCursor.getString(rowCursor.getColumnIndex(COL_FULL_TIME)));

                //adding to list
                records.add(playerRecord);
            }
        }
        return records;
    }

    /**
     * counts number of rows within a table
     *
     * @param table
     * @return
     */
    private long count(String table) {
        return DatabaseUtils.queryNumEntries(dataBase, table);
    }
}
