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

    public void updateBlind(ContentValues contentValues) {
        SQLiteDatabase db = getWritableDatabase();
        BlindsContract.update(db, contentValues);
    }

    public void insertBlind(ContentValues contentValues) {
        SQLiteDatabase db = getWritableDatabase();
        BlindsContract.insert(db, contentValues);
    }

    public void deleteBlind(long id) {
        SQLiteDatabase db = getWritableDatabase();
        BlindsContract.delete(db, id);
    }
}
