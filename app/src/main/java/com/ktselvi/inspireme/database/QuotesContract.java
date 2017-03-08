package com.ktselvi.inspireme.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tkumares on 07-Mar-17.
 */

public class QuotesContract {
    public static final String CONTENT_AUTHORITY = "com.ktselvi.inspireme";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class QuotesTable implements BaseColumns {
        public static final String TABLE_NAME = "quotes";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_QUOTE = "quote";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_TAG = "tag";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }
}
