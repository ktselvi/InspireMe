package com.ktselvi.inspireme.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by tkumares on 07-Mar-17.
 */

public class QuoteProvider extends ContentProvider {

    private MySQLiteHelper openDbHelper;
    private static final int FAV_QUOTES_LIST = 123;
    private static final int FAV_QUOTE_ITEM = 456;
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(QuotesContract.CONTENT_AUTHORITY,
                "quotes",
                FAV_QUOTES_LIST);
        URI_MATCHER.addURI(QuotesContract.CONTENT_AUTHORITY,
                "quotes/#",
                FAV_QUOTE_ITEM);
    }

    @Override
    public boolean onCreate() {
        openDbHelper = new MySQLiteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (URI_MATCHER.match(uri)) {
            //All quotes
            case FAV_QUOTES_LIST: {
                retCursor = openDbHelper.getReadableDatabase()
                        .query(QuotesContract.QuotesTable.TABLE_NAME, projection, selection, selectionArgs,
                                null, null, sortOrder);
                return retCursor;
            }
            // Individual quote based on Id selected
            case FAV_QUOTE_ITEM: {
                retCursor = openDbHelper.getReadableDatabase()
                        .query(QuotesContract.QuotesTable.TABLE_NAME, projection,
                                QuotesContract.QuotesTable._ID + " = ?",
                                new String[] { String.valueOf(ContentUris.parseId(uri)) }, null, null, sortOrder);
                return retCursor;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case FAV_QUOTES_LIST:
                return QuotesContract.QuotesTable.CONTENT_DIR_TYPE;
            case FAV_QUOTE_ITEM:
                return QuotesContract.QuotesTable.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            return itemUri;
        }
        throw new SQLException(
                "Problem while inserting into uri: " + uri);
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (URI_MATCHER.match(uri) != FAV_QUOTES_LIST) {
            throw new IllegalArgumentException(
                    "Unsupported URI for insertion: " + uri);
        }
        SQLiteDatabase db = openDbHelper.getWritableDatabase();
        if (URI_MATCHER.match(uri) == FAV_QUOTES_LIST) {
            long id = db.insert(QuotesContract.QuotesTable.TABLE_NAME, null, contentValues);
            getContext().getContentResolver().notifyChange(uri, null);
            return getUriForId(id, uri);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = openDbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int numDeleted;
        switch (match) {
            case FAV_QUOTES_LIST:
                numDeleted = db.delete(QuotesContract.QuotesTable.TABLE_NAME, selection, selectionArgs);

                break;
            case FAV_QUOTE_ITEM:
                numDeleted = db.delete(QuotesContract.QuotesTable.TABLE_NAME,
                        QuotesContract.QuotesTable._ID + " = ?",
                        new String[] { String.valueOf(ContentUris.parseId(uri)) });
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        //As we do not support update operation for quotes
        throw new UnsupportedOperationException("Unsupported operation: " + uri);
    }
}
