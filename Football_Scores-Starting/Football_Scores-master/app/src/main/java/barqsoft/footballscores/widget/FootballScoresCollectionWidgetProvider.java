package barqsoft.footballscores.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import barqsoft.footballscores.sync.FootballScoresSyncAdapter;
import barqsoft.footballscores.R;

/**
 * Created by Gurmeet on 31-12-2015.
 */
public class FootballScoresCollectionWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_WIDGET_ITEM_SELECTED = "barqsoft.footballscores.app.ACTION_WIDGET_ITEM_SELECTED";
    public static String LOG_TAG = FootballScoresCollectionWidgetProvider.class.getSimpleName();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId :appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.collection_widget);

            Intent intent = new Intent(context, FootballScoreRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            intent.setAction(ACTION_WIDGET_ITEM_SELECTED);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                views.setRemoteAdapter(appWidgetId, R.id.collection_widget_scores_list, intent);
            } else {
                views.setRemoteAdapter(0, R.id.collection_widget_scores_list, intent);
            }

            Intent toastIntent = new Intent(context, FootballScoresCollectionWidgetProvider.class);

            toastIntent.setAction(FootballScoresCollectionWidgetProvider.ACTION_WIDGET_ITEM_SELECTED);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.collection_widget_scores_list, toastPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (FootballScoresSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.collection_widget_scores_list);
        }

        if(intent.getAction().equals(ACTION_WIDGET_ITEM_SELECTED)) {
            String matchId = intent.getExtras().getString("matchId");
            Log.v(LOG_TAG, "Match Id: " + matchId);
            Toast toast = Toast.makeText(context, "Match Id: " + matchId, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
