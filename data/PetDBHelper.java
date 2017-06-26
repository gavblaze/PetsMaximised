package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by Gavin on 26-Jun-17.
 */

public class PetDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pets.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PetEntry.TABLE_NAME + " (" +
            PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PetEntry.PET_NAME + " TEXT," +
                    PetEntry.PET_BREED + " INTEGER," +
                    PetEntry.PET_GENDER + " INTEGER," +
                    PetEntry.PET_WEIGHT + " INTEGER );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PetEntry.TABLE_NAME;

    public PetDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
