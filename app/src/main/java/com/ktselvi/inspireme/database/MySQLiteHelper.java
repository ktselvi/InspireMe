package com.ktselvi.inspireme.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tkumares on 07-Mar-17.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_QUOTES = "quotes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_QUOTE = "quote";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TAG = "tag";

    private static final String DATABASE_NAME = "quotes.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_QUOTES + "(" + COLUMN_ID
            + " integer primary key autoincrement," + COLUMN_QUOTE
            + " text not null," + COLUMN_AUTHOR
            + " text not null," + COLUMN_TAG
            + " text not null);";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUOTES);
        onCreate(sqLiteDatabase);
    }
}
