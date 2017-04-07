package net.chokethe.killerdealer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KillerDealerDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "killerdealer.db";
    private static final int DATABASE_VERSION = 1;

    public KillerDealerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        BlindsContract.createTable(db);
        BlindsContract.populateDefaults(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing to do...
    }

    public Cursor getAllBlinds() {
        SQLiteDatabase db = getReadableDatabase();
        return BlindsContract.selectAll(db);
    }

    public void updateBlind(long id, int smallBlind, int bigBlind, int riseTime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BlindsContract.BlindsEntry._ID, id);
        contentValues.put(BlindsContract.BlindsEntry.COLUMN_SMALL_BLIND, smallBlind);
        contentValues.put(BlindsContract.BlindsEntry.COLUMN_BIG_BLIND, bigBlind);
        contentValues.put(BlindsContract.BlindsEntry.COLUMN_RISE_TIME, riseTime);
        BlindsContract.update(db, contentValues);
    }

    public void insertBlind(int smallBlind, int bigBlind, int riseTime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BlindsContract.BlindsEntry.COLUMN_SMALL_BLIND, smallBlind);
        contentValues.put(BlindsContract.BlindsEntry.COLUMN_BIG_BLIND, bigBlind);
        contentValues.put(BlindsContract.BlindsEntry.COLUMN_RISE_TIME, riseTime);
        BlindsContract.insert(db, contentValues);
    }

    public void deleteBlind(long id) {
        SQLiteDatabase db = getWritableDatabase();
        BlindsContract.delete(db, id);
    }
}
