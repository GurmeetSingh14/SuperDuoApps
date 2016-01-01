package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import barqsoft.footballscores.sync.FootballScoresSyncAdapter;

/**
 * Created by Gurmeet on 31-12-2015.
 */
public class FootballScoresWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, FootballScoresWidgetIntentService.class));
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (FootballScoresSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            context.startService(new Intent(context, FootballScoresWidgetIntentService.class));
        }
    }
}
