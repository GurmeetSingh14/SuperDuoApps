package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.R;

/**
 * Created by Gurmeet on 31-12-2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FootballScoresWidgetIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FootballScoresWidgetIntentService(String name) {
        super(name);
    }

    public FootballScoresWidgetIntentService() {
        super("FootballScoresWidgetIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                FootballScoresWidgetProvider.class));

        SimpleDateFormat todaysDateFormat = new SimpleDateFormat("yyyy-MM--dd");
        String todaysDate = todaysDateFormat.format(new Date());

        Cursor data = getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null,
                DatabaseContract.scores_table.DATE_COL + "=", new String[]{todaysDate}, null);

        if (data == null) {
            return;
        }

        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        String strHomeName = data.getString(data.getColumnIndex(DatabaseContract.scores_table.HOME_COL));
        String strAwayName = data.getString(data.getColumnIndex(DatabaseContract.scores_table.AWAY_COL));
        String strHomeGoals = data.getString(data.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL));
        String strAwayGoals = data.getString(data.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL));
        String strTime = data.getString(data.getColumnIndex(DatabaseContract.scores_table.TIME_COL));

        data.close();

        for (int appWidgetId : appWidgetIds) {

            int layoutId = R.layout.football_score_widget;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            views.setTextViewText(R.id.home_name, strHomeName);
            views.setTextViewText(R.id.away_crest, strAwayName);

            String score = Utilies.getScores(Integer.parseInt(strHomeGoals), Integer.parseInt(strAwayGoals));
            views.setTextViewText(R.id.score_textview, score);
            views.setTextViewText(R.id.time_textview, strTime);
            views.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(strHomeName));
            views.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(strAwayName));

            //Launch Main Activity when clicked on the widget
            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                    mainActivityIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_football, pendingIntent);

            //Perform an update on Current App Widgets
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
