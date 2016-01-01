package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.R;

/**
 * Created by Gurmeet on 31-12-2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FootballScoreRemoteViewsService extends RemoteViewsService {
        @Override
        public RemoteViewsFactory onGetViewFactory(Intent intent) {
            return new RemoteViewsFactory() {
                private Cursor data = null;

                @Override
                public void onCreate() {
                    // Nothing to do
                }

                @Override
                public void onDataSetChanged() {
                    if (data != null) {
                        data.close();
                    }
                    // This method is called by the app hosting the widget (e.g., the launcher)
                    // However, our ContentProvider is not exported so it doesn't have access to the
                    // data. Therefore we need to clear (and finally restore) the calling identity so
                    // that calls use our process and permission
                    final long identityToken = Binder.clearCallingIdentity();

                    SimpleDateFormat todaysDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String todaysDate = todaysDateFormat.format(new Date());

                    data = getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(),
                            null,
                            DatabaseContract.scores_table.DATE_COL + "=",
                            new String[] {todaysDate},
                            null);
                    Binder.restoreCallingIdentity(identityToken);
                }

                @Override
                public void onDestroy() {
                    if (data != null) {
                        data.close();
                        data = null;
                    }
                }

                @Override
                public int getCount() {
                    return data == null ? 0 : data.getCount();
                }

                @Override
                public RemoteViews getViewAt(int position) {
                    if (position == AdapterView.INVALID_POSITION ||
                            data == null || !data.moveToPosition(position)) {
                        return null;
                    }
                    RemoteViews views = new RemoteViews(getPackageName(),
                            R.layout.collection_widget_list_item);

                    String strHomeName = data.getString(data.getColumnIndex(DatabaseContract.scores_table.HOME_COL));
                    String strAwayName = data.getString(data.getColumnIndex(DatabaseContract.scores_table.AWAY_COL));
                    String strHomeGoals = data.getString(data.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL));
                    String strAwayGoals = data.getString(data.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL));
                    String strTime = data.getString(data.getColumnIndex(DatabaseContract.scores_table.TIME_COL));
                    String strMatchId = data.getString(data.getColumnIndex(DatabaseContract.scores_table.MATCH_ID));

                    views.setTextViewText(R.id.collection_home_name, strHomeName);
                    views.setTextViewText(R.id.collection_away_name, strAwayName);

                    String score = Utilies.getScores(Integer.parseInt(strHomeGoals), Integer.parseInt(strAwayGoals));
                    views.setTextViewText(R.id.collection_score_textview, score);
                    views.setTextViewText(R.id.collection_data_textview, strTime);
                    views.setImageViewResource(R.id.collection_home_crest, Utilies.getTeamCrestByTeamName(strHomeName));
                    views.setImageViewResource(R.id.collection_away_crest, Utilies.getTeamCrestByTeamName(strAwayName));

                    Bundle extras = new Bundle();
                    extras.putString("matchId", strMatchId);
                    final Intent fillInIntent = new Intent();
                    views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                    return views;
                }


                @Override
                public RemoteViews getLoadingView() {
                    return new RemoteViews(getPackageName(), R.layout.collection_widget_list_item);
                }

                @Override
                public int getViewTypeCount() {
                    return 1;
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public boolean hasStableIds() {
                    return true;
                }
            };
        }
    }

