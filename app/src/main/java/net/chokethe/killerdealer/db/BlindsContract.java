package net.chokethe.killerdealer.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class BlindsContract {
    private BlindsContract() {
    }

    public class BlindsEntry implements BaseColumns {
        static final String TABLE_NAME = "blinds";
        public static final String COLUMN_SMALL_BLIND = "smallBlind";
        public static final String COLUMN_BIG_BLIND = "bigBlind";
        public static final String COLUMN_RISE_TIME = "riseTime";
    }

    private static final String SQL_CREATE_BLINDS_TABLE = "CREATE TABLE " + BlindsEntry.TABLE_NAME + " (" +
            BlindsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            BlindsEntry.COLUMN_SMALL_BLIND + " INTEGER NOT NULL, " +
            BlindsEntry.COLUMN_BIG_BLIND + " INTEGER NOT NULL, " +
            BlindsEntry.COLUMN_RISE_TIME + " INTEGER NOT NULL); ";

    static void createTable(SQLiteDatabase db) {
        db.execSQL(BlindsContract.SQL_CREATE_BLINDS_TABLE);
    }

    private static final int[][] DEFAULT_BLINDS = {
            {5, 10, 30}, {10, 25, 30}, {25, 50, 20}, {50, 75, 20}, {75, 100, 20},
            {100, 150, 15}, {125, 200, 15}, {150, 250, 15}, {200, 350, 10}, {250, 500, 10}};

    static void populateDefaults(SQLiteDatabase db) {
        for (int[] DEFAULT_BLIND : DEFAULT_BLINDS) {
            ContentValues insertValues = new ContentValues();
            insertValues.put(BlindsEntry.COLUMN_SMALL_BLIND, DEFAULT_BLIND[0]);
            insertValues.put(BlindsEntry.COLUMN_BIG_BLIND, DEFAULT_BLIND[1]);
            insertValues.put(BlindsEntry.COLUMN_RISE_TIME, DEFAULT_BLIND[2]);
            db.insert(BlindsEntry.TABLE_NAME, null, insertValues);
        }
    }

    static Cursor selectAll(SQLiteDatabase db) {
        return db.query(BlindsContract.BlindsEntry.TABLE_NAME, null, null, null, null, null, BlindsContract.BlindsEntry._ID);
    }

    static void update(SQLiteDatabase db, ContentValues contentValues) {
        db.update(BlindsEntry.TABLE_NAME, contentValues, BlindsEntry._ID + "=" + contentValues.getAsString(BlindsEntry._ID), null);
    }

    static long insert(SQLiteDatabase db, ContentValues contentValues) {
        return db.insert(BlindsEntry.TABLE_NAME, null, contentValues);
    }

    static int delete(SQLiteDatabase db, long id) {
        return db.delete(BlindsEntry.TABLE_NAME, BlindsEntry._ID + "=" + id, null);
    }
}
