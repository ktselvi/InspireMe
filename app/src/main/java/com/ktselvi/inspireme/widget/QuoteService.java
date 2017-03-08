package com.ktselvi.inspireme.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ktselvi.inspireme.R;
import com.ktselvi.inspireme.database.QuotesContract;

/**
 * Created by tkumares on 06-Mar-17.
 */

public class QuoteService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        private Cursor cursor;
        private Context mContext;
        private int mAppWidgetId;

        public StackRemoteViewsFactory(Context context, Intent intent){
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (cursor != null) {
                cursor.close();
            }
            cursor = mContext.getContentResolver().query(QuotesContract.QuotesTable.CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onDestroy() {
            if (cursor != null) {
                cursor.close();
            }
        }

        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            String wQuote = "";
            if (cursor.moveToPosition(i)) {
                wQuote = cursor.getString(cursor.getColumnIndex(QuotesContract.QuotesTable.COLUMN_QUOTE));
            }
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_stack_item);
            rv.setTextViewText(R.id.widget_text, wQuote);

            final Intent fillInIntent = new Intent();
            rv.setOnClickFillInIntent(R.id.widget_text, fillInIntent);
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
