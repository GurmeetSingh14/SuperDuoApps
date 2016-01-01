package barqsoft.footballscores.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Gurmeet on 31-12-2015.
 */
public class FootballScoresSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static FootballScoresSyncAdapter sFootballScoresSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sFootballScoresSyncAdapter == null) {
                sFootballScoresSyncAdapter = new FootballScoresSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sFootballScoresSyncAdapter.getSyncAdapterBinder();
    }
}