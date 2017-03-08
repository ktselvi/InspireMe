package com.ktselvi.inspireme.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.ktselvi.inspireme.MainActivity;
import com.ktselvi.inspireme.R;

/**
 * Created by tkumares on 06-Mar-17.
 */

public class QuoteWidgetProvider extends AppWidgetProvider {

    public static final String INTENT_ACTION = "INTENT_ACTION_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            Intent intent = new Intent(context, QuoteService.class);
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            rv.setRemoteAdapter(appWidgetIds[i], R.id.widget_content, intent);
            rv.setEmptyView(R.id.widget_content, R.id.empty_view);

            //Intent that handles item click
            Intent openApp = new Intent(context, QuoteWidgetProvider.class);
            openApp.setAction(QuoteWidgetProvider.INTENT_ACTION);
            openApp.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            openApp.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, openApp,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            rv.setPendingIntentTemplate(R.id.widget_content, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(INTENT_ACTION)) {
            Intent i = new Intent(ctx, MainActivity.class);
            i.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(i);
        }
        super.onReceive(ctx, intent);
    }
}